# LIrAs

LIrAs is a Domain-Specific Language (DSL) for specifying interaction patterns among multi-agent systems. It automatically translates these specifications into SHA (Symbolic Hybrid Automata) models and enables SMC (Statistical Model Checking) verification.

## Requirements

Before getting started, ensure you have the following tools installed:

- **Eclipse IDE** (version 2024-06 or compatible)
- **UPPAAL** (version 5.0.0) - for viewing generated SHA models and performing verification

## Getting Started

### Installation

1. Clone or download the LIrAs project
2. Open Eclipse IDE
3. Import the LIrAs project into your Eclipse workspace

### Running the Project

1. In Eclipse, right-click on the LIrAs project
2. Select **Run As** → **Eclipse Application**
3. The LIrAs interface will open, allowing you to specify interaction patterns using the DSL

## Project Structure

- **`runtime/`** - Contains example specifications and generated models
  - Example LIrAs code specifications in the robotic domain
  - **`src-gen/`** - Generated SHA models and base templates
    - Contains automatically generated SHA models from your LIrAs specifications
    - Includes base templates used to build the models (customize these according to your system layout and application-specific requirements)

## Workflow

1. **Specify Pattern** - Use the LIrAs interface to define interaction patterns for your multi-agent system
2. **Generate SHA** - The DSL automatically translates your specifications into SHA models (found in `src-gen/`)
3. **Customize Templates** - Modify the base templates in `src-gen/` to match your specific system layout and application needs
4. **Verify** - Load the generated SHA models in UPPAAL 5.0.0 to perform statistical model checking verification

## Examples

Example specifications in the robotic domain can be found in the `runtime/` folder to help you get started with defining your own patterns.

## License

This project is licensed under the MIT License - see the LICENSE file for details.