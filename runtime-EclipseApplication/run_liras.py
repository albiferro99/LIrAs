from pathlib import Path
import subprocess
import re
import csv
import statistics
import time


# ============================================================
# CONFIGURATION
# ============================================================

BASE_DIR = Path(
    "/Users/alberto_tagliaferro/Library/CloudStorage/"
    "OneDrive-PolitecnicodiMilano/Desktop/"
    "LIRAs_Generation/runtime-EclipseApplication"
)

CLI = BASE_DIR / "liras-cli.sh"

VERIFYTA = Path(
    "/Applications/UPPAAL-5.0.0.app/"
    "Contents/Resources/uppaal/bin/verifyta"
)

BENCHMARK_DIR = BASE_DIR / "scalability_benchmark"
INPUT_DIR = BENCHMARK_DIR / "liras"
OUTPUT_DIR = BENCHMARK_DIR / "xml"
QUERY_DIR = BENCHMARK_DIR / "queries"

INPUT_DIR.mkdir(parents=True, exist_ok=True)
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
QUERY_DIR.mkdir(parents=True, exist_ok=True)


# Number of independent repetitions for both generation
# and verification.
REPEATS = 15

# UPPAAL SMC time horizon.
HORIZON = 700

# Maximum wall-clock time allowed for one verifyta invocation.
VERIFY_TIMEOUT_SECONDS = 300


# Controlled experiments:
# scale = total number of action occurrences.
CONTROLLED_VALUES = [
    2,
    4,
    8,
    16,
    32,
    64,
    128,
]

# Combined stress test:
# robots = scale
# subsequences per robot = scale
# total actions = scale^2
COMBINED_VALUES = [
    2,
    4,
    8,
    16,
    32,
    64,
]


FAMILY_VALUES = {
    "agents": CONTROLLED_VALUES,
    "actions": CONTROLLED_VALUES,
    "subsequences": CONTROLLED_VALUES,
    "combined": COMBINED_VALUES,
}


# ============================================================
# OUTPUT FILES
# ============================================================

GENERATION_RUNS_CSV = (
    BENCHMARK_DIR / "generation_runs.csv"
)

VERIFICATION_RUNS_CSV = (
    BENCHMARK_DIR / "verification_runs.csv"
)

SUMMARY_CSV = (
    BENCHMARK_DIR / "benchmark_summary.csv"
)


# ============================================================
# BENCHMARK GENERATION
# ============================================================

def create_benchmark(family: str, scale: int):
    """
    Create one synthetic LIrAs scalability benchmark.

    agents:
        scale robots
        1 subsequence per robot
        1 pick per robot
        total actions = scale

    actions:
        2 robots
        1 subsequence per robot
        scale/2 picks per robot
        total actions = scale

    subsequences:
        2 robots
        scale/2 subsequences per robot
        1 pick per subsequence
        total actions = scale

    combined:
        scale robots
        scale subsequences per robot
        1 pick per subsequence
        total actions = scale^2
    """

    if (
        family in (
            "agents",
            "actions",
            "subsequences",
        )
        and scale % 2 != 0
    ):
        raise ValueError(
            f"Controlled benchmark scale must be even: {scale}"
        )

    if family == "agents":
        n_robots = scale
        actions_per_robot = 1
        subsequences_per_robot = 1

    elif family == "actions":
        n_robots = 2
        actions_per_robot = scale // 2
        subsequences_per_robot = 1

    elif family == "subsequences":
        n_robots = 2
        actions_per_robot = scale // 2
        subsequences_per_robot = scale // 2

    elif family == "combined":
        n_robots = scale
        actions_per_robot = scale
        subsequences_per_robot = scale

    else:
        raise ValueError(
            f"Unknown family: {family}"
        )

    total_action_occurrences = (
        n_robots * actions_per_robot
    )

    pattern_name = (
        f"bench_{family}_{scale}"
    )

    robots = " , ".join(
        f"Robot{i}"
        for i in range(
            1,
            n_robots + 1
        )
    )

    lines = [
        pattern_name,
        "agents:",
        f"robots: {robots}",
        "resources: coffee",
        "",
    ]

    for robot_id in range(
        1,
        n_robots + 1
    ):
        lines.append(
            f"Robot{robot_id}:"
        )

        if family == "agents":
            lines.append(
                "1: pick coffee"
            )

        elif family == "actions":
            # All actions are sequentially contained
            # in the SAME subsequence.
            lines.append(
                "1: pick coffee"
            )

            for _ in range(
                1,
                actions_per_robot
            ):
                lines.append(
                    "pick coffee"
                )

        elif family == "subsequences":
            # One action per distinct subsequence.
            for subsequence in range(
                1,
                subsequences_per_robot + 1
            ):
                lines.append(
                    f"{subsequence}: pick coffee"
                )

        elif family == "combined":
            # Both robots and subsequences grow
            # simultaneously.
            for subsequence in range(
                1,
                subsequences_per_robot + 1
            ):
                lines.append(
                    f"{subsequence}: pick coffee"
                )

        lines.append("")

    input_path = (
        INPUT_DIR /
        f"{pattern_name}.LIRAs"
    )

    input_path.write_text(
        "\n".join(lines),
        encoding="utf-8"
    )

    metadata = {
        "family": family,
        "scale": scale,
        "robots": n_robots,
        "actions_per_robot":
            actions_per_robot,
        "subsequences_per_robot":
            subsequences_per_robot,
        "total_action_occurrences":
            total_action_occurrences,
    }

    return (
        input_path,
        pattern_name,
        metadata,
    )


# ============================================================
# BENCHMARK-SPECIFIC MODEL PREPARATION
# ============================================================

def prepare_benchmark_model(
    xml_path: Path
):
    """
    Place all robots at the resource position.

    This happens AFTER generation, therefore it is
    excluded from the LIrAs generation-time measurement.
    """

    text = xml_path.read_text(
        encoding="utf-8"
    )

    pattern = re.compile(
        r"robPositionX\[id-1\]\s*=\s*"
        r"INTERSECTIONS\[R_start_nodes\[id-1\]\]\[0\];"
        r"\s*"
        r"robPositionY\[id-1\]\s*=\s*"
        r"INTERSECTIONS\[R_start_nodes\[id-1\]\]\[1\];"
    )

    replacement = (
        "robPositionX[id-1] = resources_X[0];\n"
        "    robPositionY[id-1] = resources_Y[0];"
    )

    text, occurrences = (
        pattern.subn(
            replacement,
            text
        )
    )

    if occurrences == 0:
        raise RuntimeError(
            "Could not find robot "
            f"initialization in {xml_path.name}"
        )

    xml_path.write_text(
        text,
        encoding="utf-8"
    )

    return occurrences


# ============================================================
# LIrAs GENERATOR
# ============================================================

def run_generator(
    input_path: Path
):
    """
    Run LIrAs standalone generation.

    The returned time is the internal generation time
    printed by the generator itself.
    """

    result = subprocess.run(
        [
            str(CLI),
            str(input_path),
            str(OUTPUT_DIR),
        ],
        capture_output=True,
        text=True
    )

    if result.returncode != 0:
        print()
        print("Generator STDOUT:")
        print(result.stdout)

        print()
        print("Generator STDERR:")
        print(result.stderr)

        raise RuntimeError(
            "Generation failed for "
            f"{input_path.name}"
        )

    match = re.search(
        r"Generation completed in "
        r"(\d+) ms",
        result.stdout
    )

    if not match:
        raise RuntimeError(
            "Generation time not found "
            f"for {input_path.name}"
        )

    return int(
        match.group(1)
    )


# ============================================================
# UPPAAL QUERY
# ============================================================

def create_query(
    pattern_name: str
):
    query_path = (
        QUERY_DIR /
        f"{pattern_name}.q"
    )

    query = (
        f"Pr[<={HORIZON}] "
        f"(<> {pattern_name}.end)\n"
    )

    query_path.write_text(
        query,
        encoding="utf-8"
    )

    return query_path


# ============================================================
# VERIFYTA OUTPUT PARSER
# ============================================================

NUMBER_RE = (
    r"[+-]?"
    r"(?:"
    r"\d+(?:\.\d*)?"
    r"|"
    r"\.\d+"
    r")"
    r"(?:[eE][+-]?\d+)?"
)


def parse_verifyta_output(
    stdout: str
):
    """
    Parse output such as:

    (72/72 runs) Pr(<> ...) in [0.950056,1] (95% CI)
    """

    result = {
        "formula_satisfied": False,
        "smc_completed_runs": None,
        "smc_total_runs": None,
        "ci_lower": None,
        "ci_upper": None,
        "confidence_percent": None,
        "seed": None,
    }

    result["formula_satisfied"] = (
        "-- Formula is satisfied."
        in stdout
    )

    seed_match = re.search(
        r"Seed is\s+(\d+)",
        stdout
    )

    if seed_match:
        result["seed"] = int(
            seed_match.group(1)
        )

    ci_pattern = re.compile(
        rf"\("
        rf"(\d+)\s*/\s*(\d+)"
        rf"\s+runs"
        rf"\)"
        rf".*?"
        rf"\s+in\s+"
        rf"\[\s*"
        rf"({NUMBER_RE})"
        rf"\s*,\s*"
        rf"({NUMBER_RE})"
        rf"\s*\]"
        rf"\s*"
        rf"\(\s*"
        rf"({NUMBER_RE})"
        rf"%\s*CI\s*"
        rf"\)",
        re.DOTALL
    )

    match = ci_pattern.search(
        stdout
    )

    if match:
        result["smc_completed_runs"] = (
            int(match.group(1))
        )

        result["smc_total_runs"] = (
            int(match.group(2))
        )

        result["ci_lower"] = float(
            match.group(3)
        )

        result["ci_upper"] = float(
            match.group(4)
        )

        result["confidence_percent"] = (
            float(match.group(5))
        )

    return result


# ============================================================
# VERIFYTA
# ============================================================

def run_verifyta(
    xml_path: Path,
    query_path: Path
):
    """
    Run one independent verifyta invocation.

    Wall-clock time is measured using perf_counter.

    STDERR is discarded intentionally because verifyta
    emits thousands of non-critical duplicate-ID warnings
    for the generated models.
    """

    start = time.perf_counter()

    try:
        result = subprocess.run(
            [
                str(VERIFYTA),
                str(xml_path),
                str(query_path),
            ],
            stdout=subprocess.PIPE,
            stderr=subprocess.DEVNULL,
            text=True,
            encoding="utf-8",
            errors="replace",
            timeout=VERIFY_TIMEOUT_SECONDS,
        )

        elapsed = (
            time.perf_counter()
            - start
        )

        parsed = (
            parse_verifyta_output(
                result.stdout
            )
        )

        return {
            "verification_time_s":
                elapsed,

            "returncode":
                result.returncode,

            "timed_out":
                False,

            **parsed,
        }

    except subprocess.TimeoutExpired:
        elapsed = (
            time.perf_counter()
            - start
        )

        return {
            "verification_time_s":
                elapsed,

            "returncode":
                None,

            "timed_out":
                True,

            "formula_satisfied":
                False,

            "smc_completed_runs":
                None,

            "smc_total_runs":
                None,

            "ci_lower":
                None,

            "ci_upper":
                None,

            "confidence_percent":
                None,

            "seed":
                None,
        }


# ============================================================
# STATISTICS
# ============================================================

def stats(values):
    values = [
        value
        for value in values
        if value is not None
    ]

    if not values:
        return {
            "mean": None,
            "std": None,
            "median": None,
            "min": None,
            "max": None,
        }

    if len(values) > 1:
        std = statistics.stdev(
            values
        )
    else:
        std = 0.0

    return {
        "mean":
            statistics.mean(values),

        "std":
            std,

        "median":
            statistics.median(values),

        "min":
            min(values),

        "max":
            max(values),
    }


# ============================================================
# CSV
# ============================================================

GENERATION_FIELDS = [
    "family",
    "scale",
    "robots",
    "actions_per_robot",
    "subsequences_per_robot",
    "total_action_occurrences",
    "run",
    "generation_time_ms",
]


VERIFICATION_FIELDS = [
    "family",
    "scale",
    "robots",
    "actions_per_robot",
    "subsequences_per_robot",
    "total_action_occurrences",
    "horizon",
    "run",
    "verification_time_s",
    "returncode",
    "timed_out",
    "formula_satisfied",
    "seed",
    "smc_completed_runs",
    "smc_total_runs",
    "ci_lower",
    "ci_upper",
    "confidence_percent",
]


SUMMARY_FIELDS = [
    "family",
    "scale",
    "robots",
    "actions_per_robot",
    "subsequences_per_robot",
    "total_action_occurrences",

    "generation_repeats",
    "generation_mean_ms",
    "generation_std_ms",
    "generation_median_ms",
    "generation_min_ms",
    "generation_max_ms",

    "liras_size_bytes",
    "liras_lines",
    "xml_size_bytes",
    "patched_initializers",

    "horizon",

    "verification_repeats",
    "verification_completed_runs",
    "verification_timeout_runs",
    "verification_error_runs",
    "verification_parsed_runs",

    "verification_mean_s",
    "verification_std_s",
    "verification_median_s",
    "verification_min_s",
    "verification_max_s",

    "smc_runs_mean",
    "smc_runs_std",
    "smc_runs_min",
    "smc_runs_max",

    "ci_lower_mean",
    "ci_lower_min",
    "ci_lower_max",

    "ci_upper_mean",
    "ci_upper_min",
    "ci_upper_max",

    "confidence_percent_mean",

    "all_formula_satisfied",
]


def write_csv(
    path: Path,
    rows,
    fieldnames
):
    with path.open(
        "w",
        newline="",
        encoding="utf-8"
    ) as f:

        writer = csv.DictWriter(
            f,
            fieldnames=fieldnames
        )

        writer.writeheader()
        writer.writerows(rows)


# ============================================================
# MAIN BENCHMARK
# ============================================================

def main():
    if not CLI.exists():
        raise RuntimeError(
            f"LIrAs CLI not found: {CLI}"
        )

    if not VERIFYTA.exists():
        raise RuntimeError(
            f"verifyta not found: {VERIFYTA}"
        )

    generation_raw = []
    verification_raw = []
    summary_rows = []

    total_configurations = sum(
        len(values)
        for values
        in FAMILY_VALUES.values()
    )

    configuration_index = 0

    print()
    print("=" * 72)
    print("LIrAs SCALABILITY BENCHMARK")
    print("=" * 72)
    print(
        f"Configurations: "
        f"{total_configurations}"
    )
    print(
        f"Generation runs/config: "
        f"{REPEATS}"
    )
    print(
        f"Verification runs/config: "
        f"{REPEATS}"
    )
    print(
        f"SMC horizon: {HORIZON}"
    )
    print(
        f"verifyta timeout: "
        f"{VERIFY_TIMEOUT_SECONDS} s"
    )

    for family, values in (
        FAMILY_VALUES.items()
    ):
        print()
        print("=" * 72)
        print(
            f"FAMILY: {family.upper()}"
        )
        print("=" * 72)

        for scale in values:
            configuration_index += 1

            (
                input_path,
                pattern_name,
                metadata,
            ) = create_benchmark(
                family,
                scale
            )

            print()
            print(
                f"[{configuration_index}/"
                f"{total_configurations}] "
                f"{pattern_name}"
            )

            print(
                f"  robots: "
                f"{metadata['robots']}"
            )

            print(
                f"  actions/robot: "
                f"{metadata['actions_per_robot']}"
            )

            print(
                f"  subsequences/robot: "
                f"{metadata['subsequences_per_robot']}"
            )

            print(
                f"  total actions: "
                f"{metadata['total_action_occurrences']}"
            )

            # =================================================
            # GENERATION
            # =================================================

            print()
            print("  Generation:")

            generation_times = []

            for run in range(
                1,
                REPEATS + 1
            ):
                generation_time_ms = (
                    run_generator(
                        input_path
                    )
                )

                generation_times.append(
                    generation_time_ms
                )

                generation_raw.append({
                    **metadata,
                    "run": run,
                    "generation_time_ms":
                        generation_time_ms,
                })

                print(
                    f"    "
                    f"{run:02d}/{REPEATS}: "
                    f"{generation_time_ms} ms"
                )

            generation_stats = stats(
                generation_times
            )

            # =================================================
            # FINAL XML + PATCH
            # =================================================

            xml_path = (
                OUTPUT_DIR /
                f"{pattern_name}.xml"
            )

            if not xml_path.exists():
                raise RuntimeError(
                    "Expected generated XML "
                    f"not found: {xml_path}"
                )

            patched_initializers = (
                prepare_benchmark_model(
                    xml_path
                )
            )

            # Size is intentionally measured AFTER
            # benchmark preparation.
            xml_size_bytes = (
                xml_path.stat().st_size
            )

            liras_size_bytes = (
                input_path.stat().st_size
            )

            liras_lines = len(
                input_path.read_text(
                    encoding="utf-8"
                ).splitlines()
            )

            # =================================================
            # QUERY
            # =================================================

            query_path = (
                create_query(
                    pattern_name
                )
            )

            # =================================================
            # VERIFICATION
            # =================================================

            print()
            print("  Verification:")

            config_verification_rows = []

            for run in range(
                1,
                REPEATS + 1
            ):
                verification = (
                    run_verifyta(
                        xml_path,
                        query_path
                    )
                )

                row = {
                    **metadata,
                    "horizon": HORIZON,
                    "run": run,
                    **verification,
                }

                verification_raw.append(
                    row
                )

                config_verification_rows.append(
                    row
                )

                if verification[
                    "timed_out"
                ]:
                    print(
                        f"    "
                        f"{run:02d}/{REPEATS}: "
                        f"TIMEOUT after "
                        f"{verification['verification_time_s']:.2f} s"
                    )

                elif verification[
                    "returncode"
                ] != 0:
                    print(
                        f"    "
                        f"{run:02d}/{REPEATS}: "
                        f"ERROR "
                        f"(return code "
                        f"{verification['returncode']}) "
                        f"after "
                        f"{verification['verification_time_s']:.2f} s"
                    )

                elif verification[
                    "ci_lower"
                ] is None:
                    print(
                        f"    "
                        f"{run:02d}/{REPEATS}: "
                        f"{verification['verification_time_s']:.2f} s "
                        f"| result not parsed"
                    )

                else:
                    print(
                        f"    "
                        f"{run:02d}/{REPEATS}: "
                        f"{verification['verification_time_s']:.2f} s "
                        f"| CI=["
                        f"{verification['ci_lower']:.6f}, "
                        f"{verification['ci_upper']:.6f}"
                        f"] "
                        f"| SMC="
                        f"{verification['smc_completed_runs']}/"
                        f"{verification['smc_total_runs']}"
                    )

            # =================================================
            # VERIFICATION STATISTICS
            # =================================================

            completed_rows = [
                row
                for row
                in config_verification_rows
                if (
                    not row["timed_out"]
                    and row["returncode"] == 0
                )
            ]

            parsed_rows = [
                row
                for row
                in completed_rows
                if (
                    row["ci_lower"]
                    is not None
                    and
                    row["ci_upper"]
                    is not None
                )
            ]

            timeout_runs = sum(
                1
                for row
                in config_verification_rows
                if row["timed_out"]
            )

            error_runs = sum(
                1
                for row
                in config_verification_rows
                if (
                    not row["timed_out"]
                    and
                    row["returncode"] != 0
                )
            )

            verification_stats = stats([
                row["verification_time_s"]
                for row
                in completed_rows
            ])

            smc_stats = stats([
                row["smc_total_runs"]
                for row
                in parsed_rows
            ])

            ci_lower_stats = stats([
                row["ci_lower"]
                for row
                in parsed_rows
            ])

            ci_upper_stats = stats([
                row["ci_upper"]
                for row
                in parsed_rows
            ])

            confidence_stats = stats([
                row["confidence_percent"]
                for row
                in parsed_rows
            ])

            all_formula_satisfied = (
                len(completed_rows)
                == REPEATS
                and all(
                    row["formula_satisfied"]
                    for row
                    in completed_rows
                )
            )

            # =================================================
            # SUMMARY ROW
            # =================================================

            summary = {
                **metadata,

                "generation_repeats":
                    REPEATS,

                "generation_mean_ms":
                    generation_stats["mean"],

                "generation_std_ms":
                    generation_stats["std"],

                "generation_median_ms":
                    generation_stats["median"],

                "generation_min_ms":
                    generation_stats["min"],

                "generation_max_ms":
                    generation_stats["max"],

                "liras_size_bytes":
                    liras_size_bytes,

                "liras_lines":
                    liras_lines,

                "xml_size_bytes":
                    xml_size_bytes,

                "patched_initializers":
                    patched_initializers,

                "horizon":
                    HORIZON,

                "verification_repeats":
                    REPEATS,

                "verification_completed_runs":
                    len(completed_rows),

                "verification_timeout_runs":
                    timeout_runs,

                "verification_error_runs":
                    error_runs,

                "verification_parsed_runs":
                    len(parsed_rows),

                "verification_mean_s":
                    verification_stats["mean"],

                "verification_std_s":
                    verification_stats["std"],

                "verification_median_s":
                    verification_stats["median"],

                "verification_min_s":
                    verification_stats["min"],

                "verification_max_s":
                    verification_stats["max"],

                "smc_runs_mean":
                    smc_stats["mean"],

                "smc_runs_std":
                    smc_stats["std"],

                "smc_runs_min":
                    smc_stats["min"],

                "smc_runs_max":
                    smc_stats["max"],

                "ci_lower_mean":
                    ci_lower_stats["mean"],

                "ci_lower_min":
                    ci_lower_stats["min"],

                "ci_lower_max":
                    ci_lower_stats["max"],

                "ci_upper_mean":
                    ci_upper_stats["mean"],

                "ci_upper_min":
                    ci_upper_stats["min"],

                "ci_upper_max":
                    ci_upper_stats["max"],

                "confidence_percent_mean":
                    confidence_stats["mean"],

                "all_formula_satisfied":
                    all_formula_satisfied,
            }

            summary_rows.append(
                summary
            )

            # =================================================
            # CHECKPOINT
            # =================================================

            # Save after every configuration so that partial
            # results survive an interruption.
            write_csv(
                GENERATION_RUNS_CSV,
                generation_raw,
                GENERATION_FIELDS
            )

            write_csv(
                VERIFICATION_RUNS_CSV,
                verification_raw,
                VERIFICATION_FIELDS
            )

            write_csv(
                SUMMARY_CSV,
                summary_rows,
                SUMMARY_FIELDS
            )

            # =================================================
            # CONFIGURATION SUMMARY
            # =================================================

            print()
            print(
                "  Result:"
            )

            print(
                f"    Generation: "
                f"{generation_stats['mean']:.2f} "
                f"± "
                f"{generation_stats['std']:.2f} ms"
            )

            if (
                verification_stats["mean"]
                is not None
            ):
                print(
                    f"    Verification: "
                    f"{verification_stats['mean']:.3f} "
                    f"± "
                    f"{verification_stats['std']:.3f} s"
                )

            print(
                f"    Completed verification runs: "
                f"{len(completed_rows)}/{REPEATS}"
            )

            print(
                f"    Parsed SMC results: "
                f"{len(parsed_rows)}/{REPEATS}"
            )

            print(
                f"    Timeouts: "
                f"{timeout_runs}"
            )

            print(
                f"    Errors: "
                f"{error_runs}"
            )

            if (
                ci_lower_stats["mean"]
                is not None
            ):
                print(
                    f"    Mean CI: ["
                    f"{ci_lower_stats['mean']:.6f}, "
                    f"{ci_upper_stats['mean']:.6f}"
                    f"]"
                )

            print(
                f"    Prepared XML size: "
                f"{xml_size_bytes} bytes"
            )

    # =========================================================
    # FINISHED
    # =========================================================

    print()
    print("=" * 72)
    print(
        "BENCHMARK COMPLETED"
    )
    print("=" * 72)

    print(
        f"Generation runs:   "
        f"{GENERATION_RUNS_CSV}"
    )

    print(
        f"Verification runs: "
        f"{VERIFICATION_RUNS_CSV}"
    )

    print(
        f"Summary:           "
        f"{SUMMARY_CSV}"
    )


if __name__ == "__main__":
    main()