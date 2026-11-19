#!/bin/sh
    set -eu
    if command -v mvn >/dev/null 2>&1; then exec mvn "$@"; fi
    BASE=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
    VERSION=3.9.11
    TOOL="$BASE/.tools/apache-maven-$VERSION"
    if [ ! -x "$TOOL/bin/mvn" ]; then
      mkdir -p "$BASE/.tools"
      URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$VERSION/apache-maven-$VERSION-bin.tar.gz"
      curl -fsSL "$URL" -o "$BASE/.tools/maven.tar.gz"
      curl -fsSL "$URL.sha512" -o "$BASE/.tools/maven.sha512"
      EXPECTED=$(tr -d '[:space:]' < "$BASE/.tools/maven.sha512")
      ACTUAL=$(shasum -a 512 "$BASE/.tools/maven.tar.gz" | cut -d ' ' -f 1)
      [ "$EXPECTED" = "$ACTUAL" ] || { echo "Maven checksum mismatch" >&2; exit 1; }
      tar -xzf "$BASE/.tools/maven.tar.gz" -C "$BASE/.tools"
    fi
    exec "$TOOL/bin/mvn" "$@"
