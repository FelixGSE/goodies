#!/usr/bin/env bash
# Generic file hygiene for the pre-commit hook. Fixes what can be fixed
# (trailing whitespace, CRLF, missing final newline) and fails on what cannot
# (merge markers, files over 512 KB). Usage: hygiene.sh <file>...
set -euo pipefail

max_kb=512
status=0

for f in "$@"; do
  [[ -f "$f" ]] || continue
  case "$f" in
    gradlew.bat|*.jar|*.png|*.jpg|*.gif|*.pdf|*.zip) continue ;;
  esac

  size_kb=$(( $(stat -c %s "$f") / 1024 ))
  if (( size_kb > max_kb )); then
    echo "$f: ${size_kb} KB exceeds ${max_kb} KB; large files do not belong in the repo" >&2
    status=1
  fi
  if grep -nE '^(<{7}|={7}|>{7})( |$)' "$f" >/dev/null; then
    echo "$f: merge conflict markers" >&2
    status=1
  fi

  tmp="$(mktemp)"
  sed -e 's/\r$//' -e 's/[[:space:]]*$//' "$f" > "$tmp"
  if [[ -s "$tmp" && "$(tail -c1 "$tmp" | od -An -c | tr -d ' ')" != '\n' ]]; then
    printf '\n' >> "$tmp"
  fi
  if ! cmp -s "$f" "$tmp"; then
    cat "$tmp" > "$f"
    echo "$f: fixed whitespace or line endings; re-stage and commit again" >&2
    status=1
  fi
  rm -f "$tmp"
done

exit $status
