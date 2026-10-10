#!/usr/bin/env bash
# Conventional Commits check for the commit-msg hook. Usage: lint-commit-msg.sh <file>
set -euo pipefail

file="$1"
subject="$(grep -v '^#' "$file" | sed '/^$/d' | head -1 || true)"
types='build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test'
pattern="^($types)(\([a-z0-9._/-]+\))?!?: [^ ].{0,71}$"

if [[ -z "$subject" ]]; then
  echo "commit message is empty" >&2
  exit 1
fi
if [[ "$subject" =~ ^(Merge|Revert|fixup!|squash!) ]]; then
  exit 0
fi
if ! [[ "$subject" =~ $pattern ]]; then
  cat >&2 <<EOF
commit subject does not follow Conventional Commits:
  $subject
expected: <type>[(scope)][!]: <description>, max 72 chars, lower-case type
types:    ${types//|/, }
EOF
  exit 1
fi
if [[ "$subject" =~ \.$ ]]; then
  echo "commit subject must not end with a period: $subject" >&2
  exit 1
fi
allowed_trailers='^(BREAKING CHANGE|Refs|Fixes|Closes|Reverts): '
if grep -v '^#' "$file" | grep -E '^[A-Za-z][A-Za-z-]+: ' | grep -vE "$allowed_trailers" | grep -qv "^$subject$"; then
  echo "no trailers except BREAKING CHANGE, Refs, Fixes, Closes, Reverts; the author line is the only attribution" >&2
  exit 1
fi
if grep -v '^#' "$file" | grep -oE 'https?://[^ )>]+' | grep -qvE '^https?://(github\.com|docs\.oracle\.com|kotlinlang\.org|processing\.org|en\.wikipedia\.org)/'; then
  echo "links in commit messages only to github.com, kotlinlang.org, docs.oracle.com, processing.org, wikipedia" >&2
  exit 1
fi
