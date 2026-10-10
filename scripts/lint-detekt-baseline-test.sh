#!/usr/bin/env bash
# Cases for lint-detekt-baseline.sh, each in its own throwaway git repo.
# Usage: lint-detekt-baseline-test.sh
set -euo pipefail

script="$(cd "$(dirname "$0")" && pwd)/lint-detekt-baseline.sh"
file=config/detekt/baseline-main.xml
failures=0

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

repo() {
  local dir
  dir="$(mktemp -d)"
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
  local name="$1" want_status="$2" want_output="$3" output status=0
  output="$("$script" 2>&1)" || status=$?
  if [[ "$status" != "$want_status" ]] || ! grep -qF -- "$want_output" <<< "$output"; then
    echo "FAIL $name: exit $status, want $want_status; output: $output" >&2
    failures=$((failures + 1))
  else
    echo "ok   $name"
  fi
  rm -rf "$PWD"
}

repo
commit_base
baseline 'MagicNumber:A.kt:a'
expect "base file absent passes" 0 ""

repo
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
commit_base
expect "identical passes" 0 ""

repo
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
commit_base
baseline 'MagicNumber:A.kt:a'
expect "one ID removed passes" 0 ""

repo
baseline 'MagicNumber:A.kt:a'
commit_base
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
expect "one ID added fails and names it" 1 "LongMethod:B.kt:b"

repo
baseline 'MagicNumber:A.kt:a'
commit_base
baseline 'LongMethod:B.kt:b'
expect "swap fails" 1 "LongMethod:B.kt:b"

repo
baseline
commit_base
baseline
expect "empty vs empty passes" 0 ""

repo
baseline 'MagicNumber:A.kt:a'
commit_base --no-origin
baseline 'MagicNumber:A.kt:a' 'LongMethod:B.kt:b'
expect "no origin/main compares to HEAD" 1 "against HEAD"

if (( failures > 0 )); then
  echo "$failures case(s) failed" >&2
  exit 1
fi
