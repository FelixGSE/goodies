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

##@ Quality

.PHONY: fmt
fmt: ## Format every Kotlin file with ktlint
	$(GRADLE) ktlintFormat

.PHONY: lint
lint: ## Check formatting with ktlint, lint with detekt, test the baseline check
	$(GRADLE) --continue ktlintCheck detektMain detektTest
	scripts/lint-detekt-baseline-test.sh

.PHONY: check
check: lint ## Done gate: lint, tests, coverage above MIN (e.g. make check MIN=0.6)
	$(GRADLE) check jacocoTestReport jacocoTestCoverageVerification -PcoverageMinimum=$(MIN)

.PHONY: baseline
baseline: ## Rewrite the detekt baselines after fixing legacy findings; they may only shrink
	$(GRADLE) detektBaselineMain detektBaselineTest

##@ Hygiene

.PHONY: hooks
hooks: ## Install the git hooks (prek) into this clone or worktree
	prek install --hook-type pre-commit --hook-type commit-msg

.PHONY: hygiene
hygiene: ## Run every hook on every file, as CI does
	prek run --all-files

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
