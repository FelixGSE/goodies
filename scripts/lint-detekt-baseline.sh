#!/usr/bin/env bash
# Fails when a detekt baseline gains an entry its base ref does not have:
# baselines only shrink. Base is BASELINE_BASE, else the merge-base with
# origin/main, else HEAD. A baseline absent at the base passes.
# Usage: lint-detekt-baseline.sh [baseline.xml...]
set -euo pipefail

cd "$(git rev-parse --show-toplevel)"

if (( $# == 0 )); then
  set -- config/detekt/baseline-main.xml config/detekt/baseline-test.xml
fi

if [[ -n "${BASELINE_BASE:-}" ]]; then
  base="$BASELINE_BASE"
elif git rev-parse --verify --quiet origin/main >/dev/null; then
  base="$(git merge-base HEAD origin/main)"
else
  base=HEAD
fi

ids() {
  grep -o '<ID>[^<]*</ID>' | sed -e 's#^<ID>##' -e 's#</ID>$##' | LC_ALL=C sort -u
}

status=0
for f in "$@"; do
  git cat-file -e "$base:$f" 2>/dev/null || continue
  added="$(LC_ALL=C comm -13 \
    <(git show "$base:$f" | ids) \
    <(if [[ -f "$f" ]]; then ids < "$f"; fi))"
  if [[ -n "$added" ]]; then
    echo "$f: baseline grew against $base; fix these findings or suppress them with a DESIGN: reason:" >&2
    sed 's/^/  /' <<< "$added" >&2
    status=1
  fi
done

exit $status
