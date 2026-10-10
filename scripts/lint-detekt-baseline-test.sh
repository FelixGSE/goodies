#!/usr/bin/env bash
# Cases for lint-detekt-baseline.sh, each in its own throwaway git repo.
# Usage: lint-detekt-baseline-test.sh
set -euo pipefail
unset BASELINE_BASE

script="$(cd "$(dirname "$0")" && pwd)/lint-detekt-baseline.sh"
main_file=config/detekt/baseline-main.xml
test_file=config/detekt/baseline-test.xml
file="$main_file"
failures=0
name=""
absent=""
dirs=()

cleanup() {
  local status=$?
  if (( status != 0 )) && [[ -n "$name" ]]; then
    echo "ABORT $name: setup failed" >&2
  fi
  if (( ${#dirs[@]} > 0 )); then
    rm -rf "${dirs[@]}"
  fi
}
trap cleanup EXIT

baseline() {
  mkdir -p "$(dirname "$file")"
  {
    echo "<?xml version='1.0' encoding='UTF-8'?>"
    echo '<SmellBaseline>'
    echo '  <ManuallySuppressedIssues/>'
    echo '  <CurrentIssues>'
    for id in "$@"; do
      echo "    <ID>$id</ID>"
    done
    echo '  </CurrentIssues>'
    echo '</SmellBaseline>'
  } > "$file"
}

start() {
  name="$1"
  file="$main_file"
  absent=""
  local dir
  dir="$(mktemp -d)"
  dirs+=("$dir")
  cd "$dir"
  git init -q -b main
  git config user.email test@example.invalid
  git config user.name test
  git config core.hooksPath /dev/null
  git config commit.gpgsign false
  git commit -q --allow-empty -m "chore: start"
}

commit_base() {
  git add -A
  git commit -q --allow-empty -m "chore: base"
  if [[ "${1:-}" != "--no-origin" ]]; then
    git update-ref refs/remotes/origin/main HEAD
  fi
  git switch -q -c chore/case
}

expect() {
  local want_status="$1" want_output="$2" output status=0
  shift 2
  output="$("$script" "$@" 2>&1)" || status=$?
  if [[ "$status" != "$want_status" ]] \
    || { [[ -z "$want_output" ]] && [[ -n "$output" ]]; } \
    || ! grep -qF -- "$want_output" <<< "$output" \
    || { [[ -n "$absent" ]] && grep -qF -- "$absent" <<< "$output"; }; then
    echo "FAIL $name: exit $status, want $want_status; output: $output" >&2
    failures=$((failures + 1))
  else
    echo "ok   $name"
  fi
}

start "base file absent passes"
commit_base
baseline 'MagicNumber:A.kt:a'
expect 0 ""

start "identical passes"
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
commit_base
expect 0 ""

start "one ID removed passes"
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
commit_base
baseline 'MagicNumber:A.kt:a'
expect 0 ""

start "one ID added fails and names it"
baseline 'MagicNumber:A.kt:a'
commit_base
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
absent='MagicNumber:A.kt:a'
expect 1 "LongMethod:B.kt:b"

start "swap fails"
baseline 'MagicNumber:A.kt:a'
commit_base
baseline 'LongMethod:B.kt:b'
absent='MagicNumber:A.kt:a'
expect 1 "LongMethod:B.kt:b"

start "empty vs empty passes"
baseline
commit_base
baseline
expect 0 ""

start "no origin/main compares to HEAD"
baseline 'MagicNumber:A.kt:a'
commit_base --no-origin
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
expect 1 "against HEAD"

start "main shrinking after the branch point passes"
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
commit_base
git switch -q main
baseline 'MagicNumber:A.kt:a'
git commit -q -am "refactor: shrink"
git update-ref refs/remotes/origin/main HEAD
git switch -q chore/case
expect 0 ""

start "default test baseline grown fails and names it"
file="$test_file"
baseline 'MagicNumber:ATest.kt:a'
commit_base
baseline 'MagicNumber:ATest.kt:a' 'LongMethod:BTest.kt:b'
expect 1 "LongMethod:BTest.kt:b"

start "both default baselines, only the test one grown, fails and names it"
baseline 'MagicNumber:A.kt:a'
file="$test_file"
baseline 'MagicNumber:ATest.kt:a'
commit_base
baseline 'MagicNumber:ATest.kt:a' 'LongMethod:BTest.kt:b'
absent='MagicNumber:A.kt:a'
expect 1 "LongMethod:BTest.kt:b"

start "baseline deleted in the working tree passes"
baseline 'MagicNumber:A.kt:a'
commit_base
rm "$file"
expect 0 ""

start "explicit file argument is checked"
file=config/detekt/other.xml
baseline 'MagicNumber:A.kt:a'
commit_base
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
expect 1 "LongMethod:B.kt:b" "$file"

start "BASELINE_BASE overrides the base ref"
baseline 'MagicNumber:A.kt:a'
commit_base
base_sha="$(git rev-parse HEAD)"
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
git commit -q -am "chore: grow"
git update-ref refs/remotes/origin/main HEAD
export BASELINE_BASE="$base_sha"
expect 1 "$base_sha"
unset BASELINE_BASE
name=""

if (( failures > 0 )); then
  echo "$failures case(s) failed" >&2
  exit 1
fi
