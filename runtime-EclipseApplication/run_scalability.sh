#!/bin/bash

set -e

BASE_DIR="/Users/alberto_tagliaferro/Library/CloudStorage/OneDrive-PolitecnicodiMilano/Desktop/LIRAs_Generation/runtime-EclipseApplication"

cd "$BASE_DIR"

echo "Removing previous benchmark results..."
rm -rf scalability_benchmark

echo "Creating benchmark directory..."
mkdir -p scalability_benchmark

echo "Starting LIrAs scalability benchmark..."
echo "Output will also be saved to scalability_benchmark/run.log"
echo

caffeinate -dimsu python run_liras.py \
  2>&1 | tee scalability_benchmark/run.log