#!/usr/bin/env bash
# Ignore-file check for the pre-commit hook: every entry in .trivyignore.yaml
# names an id, a statement (the reason) and an expired_at date between today
# and 30 days out, so an accepted finding comes back for review.
# Usage: lint-trivyignore.sh <file>...  (GNU date: the dev container and CI)
set -euo pipefail
[[ $# -gt 0 ]] || { echo "usage: $0 <file>..." >&2; exit 2; }

horizon_days=30
today="$(date -u +%F)"
latest="$(date -u -d "$today + $horizon_days days" +%F)"
status=0

fail() {
  echo "$1: $2" >&2
  status=1
}

for f in "$@"; do
  while IFS=$'\x1f' read -r kind id statement expired_at; do
    case "$kind" in
      flow) fail "$f" "flow-style entry ({...}); write one key per line, block style"; continue ;;
      empty) fail "$f" "entry under a [] list; replace [] with the entries"; continue ;;
    esac
    entry="$f: ${id:-<no id>}"
    [[ -n "$id" ]] || fail "$entry" "id is missing"
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
      sub(/^[ \t]*(-[ \t]+)?[A-Za-z_]+:[ \t]*/, "", line)
      sub(/[ \t]+#.*$/, "", line)
      gsub(/^["\x27]|["\x27]$/, "", line)
      return line
    }
    function indent(line) { match(line, /^[ \t]*/); return RLENGTH }
    function key(line) {
      if (line ~ /^[ \t]*(-[ \t]+)?id:/) id = value(line)
      else if (line ~ /^[ \t]*(-[ \t]+)?statement:/) statement = value(line)
      else if (line ~ /^[ \t]*(-[ \t]+)?expired_at:/) expired = value(line)
    }
    function flush() { if (open) printf "entry\x1f%s\x1f%s\x1f%s\n", id, statement, expired; open = 0 }
    /^[ \t]*(#|$)/ { next }
    /^[^ \t-]/ { flush(); empty = ($0 ~ /:[ \t]*\[[ \t]*\][ \t]*(#.*)?$/); item = -1; next }
    /^[ \t]*-([ \t]|$)/ && (item < 0 || indent($0) == item) {
      flush()
      item = indent($0)
      if (empty) { print "empty\x1f\x1f\x1f"; next }
      if ($0 ~ /^[ \t]*-[ \t]*\{/) { print "flow\x1f\x1f\x1f"; next }
      open = 1; id = ""; statement = ""; expired = ""
      key($0)
      next
    }
    open && indent($0) > item { key($0) }
    END { flush() }
  ' "$f")
done

exit "$status"
