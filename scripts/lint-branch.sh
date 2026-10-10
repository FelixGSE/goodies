#!/usr/bin/env bash
# Branch name check for the pre-commit hook: <type>/<slug>.
# Commits on main are refused (main changes through pull requests);
# ALLOW_MAIN=1 overrides that for deliberate bootstrap commits.
set -euo pipefail

branch="$(git symbolic-ref --quiet --short HEAD 2>/dev/null || echo detached)"
pattern='^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)/[a-z0-9]+(-[a-z0-9]+)*$'

case "$branch" in
  detached) exit 0 ;;
  main)
    [[ "${ALLOW_MAIN:-}" == "1" ]] && exit 0
    echo "no direct commits on main; start a branch with tools/new-task.sh (or ALLOW_MAIN=1 for a bootstrap commit)" >&2
    exit 1 ;;
esac
if ! [[ "$branch" =~ $pattern ]]; then
  cat >&2 <<EOF
branch name does not follow <type>/<slug>: $branch
example: feat/flow-fields   (lower-case, digits and dashes only)
EOF
  exit 1
fi
