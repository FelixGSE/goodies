# A thin façade over ./gradlew. Every target delegates — nothing here
# reimplements build logic, so Gradle stays the source of truth.
# Run `make` (or `make help`) for the list.

SHELL := /bin/bash
GRADLE := ./gradlew

# `make test T=CircleTests` narrows to one test class.
T ?=
TEST_FILTER := $(if $(T),--tests "*$(T)*",)

# `make coverage MIN=0.6` fails when line coverage is below 60%.
MIN ?= 0.0

# `make scan TRIVY_ARGS="--format sarif --output trivy.sarif"` for CI reports.
# Trivy reads TRIVY_CACHE_DIR itself: point it at a cached vulnerability DB.
TRIVY_ARGS ?=

##@ Build

.PHONY: build
build: ## Compile everything and run the tests
	$(GRADLE) build

.PHONY: compile
compile: ## Compile main and test sources, skip the tests
	$(GRADLE) classes testClasses

.PHONY: test
test: ## Run the tests (make test T=CircleTests for one class)
	$(GRADLE) test $(TEST_FILTER)

.PHONY: coverage
coverage: ## Run the tests with coverage, fail below MIN (e.g. make coverage MIN=0.6)
	$(GRADLE) test jacocoTestReport jacocoTestCoverageVerification -PcoverageMinimum=$(MIN) $(TEST_FILTER)
	@echo "Report: build/reports/jacoco/test/html/index.html"

.PHONY: clean
clean: ## Delete build output
	$(GRADLE) clean

##@ Hygiene

.PHONY: hooks
hooks: ## Install the git hooks (prek) into this clone or worktree
	prek install --hook-type pre-commit --hook-type commit-msg

.PHONY: hygiene
hygiene: ## Run every hook on every file, as CI does
	prek run --all-files

##@ Security

.PHONY: scan
scan: ## Scan dependencies and secrets, fail on HIGH/CRITICAL (config: trivy.yaml)
	@command -v trivy > /dev/null || { echo "trivy not found: use the dev container or https://trivy.dev/latest/getting-started/installation/" >&2; exit 1; }
	trivy fs $(TRIVY_ARGS) .

.PHONY: lock
lock: ## Refresh gradle.lockfile after a dependency or plugin change
	$(GRADLE) resolveAndLockAll --write-locks --quiet
	@status="$$(git status --porcelain -- gradle.lockfile)" && { [[ -z "$$status" ]] && echo "gradle.lockfile unchanged" || echo "gradle.lockfile changed: commit it"; }

##@ Processing library

.PHONY: lib
lib: ## Assemble the installable library folder in build/processing/
	$(GRADLE) processingLibrary

.PHONY: dist
dist: ## Zip the library for distribution into build/distributions/
	$(GRADLE) processingLibraryZip

.PHONY: install
install: ## Install into the local Processing sketchbook
	$(GRADLE) installLibrary

# Alias, in case that is what you reach for.
.PHONY: to-processing
to-processing: install

##@ Help

.PHONY: help
help: ## Show this help
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage: make <target>\n"} \
		/^[a-zA-Z0-9_ -]+:.*?##/ { printf "  \033[36m%-16s\033[0m %s\n", $$1, $$2 } \
		/^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) }' $(MAKEFILE_LIST)

.DEFAULT_GOAL := help
