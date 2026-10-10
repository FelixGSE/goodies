#!/usr/bin/env bash
# Ignore-file check for the pre-commit hook: every entry in .trivyignore.yaml
# names an id, a statement (the reason) and an expired_at date between today
# and 30 days out, so an accepted finding comes back for review.
# Usage: lint-trivyignore.sh <file>...
set -euo pipefail

horizon_days=30
today="$(date -u +%F)"
latest="$(date -u -d "$today + $horizon_days days" +%F)"
status=0

fail() {
  echo "$1: $2" >&2
  status=1
}

for f in "$@"; do
  while IFS=$'\x1f' read -r id statement expired_at; do
    entry="$f: ${id:-<no id>}"
    [[ -n "$id" ]] || fail "$entry" "id is empty"
    [[ -n "$statement" ]] || fail "$entry" "statement is missing (why the finding is acceptable)"
    if [[ -z "$expired_at" ]]; then
      fail "$entry" "expired_at is missing (YYYY-MM-DD)"
    elif ! [[ "$expired_at" =~ ^[0-9]{4}-[0-9]{2}-[0-9]{2}$ ]] \
        || [[ "$(date -u -d "$expired_at" +%F 2>/dev/null)" != "$expired_at" ]]; then
      fail "$entry" "expired_at $expired_at is not a date (YYYY-MM-DD)"
    elif [[ "$expired_at" < "$today" ]]; then
      fail "$entry" "expired_at $expired_at has passed; fix the finding or renew the entry"
    elif [[ "$expired_at" > "$latest" ]]; then
      fail "$entry" "expired_at $expired_at is more than $horizon_days days out (latest $latest)"
    fi
  done < <(awk '
    function value(line) {
      sub(/^[^:]*:[ \t]*/, "", line)
      sub(/[ \t]+#.*$/, "", line)
      gsub(/^["\x27]|["\x27]$/, "", line)
      return line
    }
    function flush() { if (open) printf "%s\x1f%s\x1f%s\n", id, statement, expired; open = 0 }
    /^[ \t]*- id:/ { flush(); open = 1; id = value($0); statement = ""; expired = ""; next }
    open && /^[ \t]+statement:/ { statement = value($0); next }
    open && /^[ \t]+expired_at:/ { expired = value($0); next }
    /^[^ \t#-]/ { flush() }
    END { flush() }
  ' "$f")
done

exit "$status"
