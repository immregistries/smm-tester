#!/bin/bash
# DEMO ONLY. Hot-deploys smm-web/target/smm.war to a local Tomcat 10 with the dev login bypass
# (smm.dev.login=true) switched on for this one application. The flag is written into a META-INF/context.xml
# inside the *deployed copy* of the WAR, so the WAR built from the branch stays safe and other webapps
# (aart, mart, step) are not affected.
# Turn the bypass off: run this script with "off" (redeploys without the flag), or remove smm.war from webapps.
set -e
HERE="$(cd "$(dirname "$0")/.." && pwd)"
WEBAPPS="${WEBAPPS:-/var/lib/tomcat10/webapps}"
WAR="$HERE/smm-web/target/smm.war"
TMP="$(mktemp -d)"
cp "$WAR" "$TMP/smm.war"
if [ "$1" != "off" ]; then
  mkdir -p "$TMP/META-INF"
  cat > "$TMP/META-INF/context.xml" <<'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<Context>
  <!-- DEV ONLY: auto sign-in as demo@localhost. Remove this file (or redeploy with "off") to disable. -->
  <Parameter name="smm.dev.login" value="true" override="false"/>
</Context>
EOF
  (cd "$TMP" && python3 -c "import zipfile;z=zipfile.ZipFile('smm.war','a');z.write('META-INF/context.xml');z.close()")
  echo "dev login ON"
else
  echo "dev login OFF"
fi
cp "$TMP/smm.war" "$WEBAPPS/smm.war.new" && mv "$WEBAPPS/smm.war.new" "$WEBAPPS/smm.war"
rm -rf "$TMP"
echo "deployed to $WEBAPPS/smm.war"
