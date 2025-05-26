#!/bin/bash
set -euo pipefail

echo "Triggering ALTERNARIA verification pipeline via API..."

curl --fail --silent --show-error --request POST \
  --header "JOB-TOKEN: $CI_JOB_TOKEN" \
  --form ref="${MAIN_BRANCH}" \
  --form "variables[VERSION]=${VERSION}" \
  --form "variables[PURPOSE]=verify" \
  "https://gitlab.juba.no/api/v4/projects/${ALTERNARIA_PROJECT_ID}/trigger/pipeline"


#!/bin/bash
set -euo pipefail

# Usage:
# ./trigger_model_verification.sh <project_id> <ref> <version> <purpose>

PROJECT_ID="$1"
REF="$2"
VERSION="$3"
PURPOSE="$4"

API_URL="https://gitlab.nibio.no/api/v4"

echo "Triggering pipeline for project $PROJECT_ID on ref '$REF'..."

# Trigger the pipeline
PIPELINE_JSON=$(curl --fail --silent --show-error --request POST \
  --header "JOB-TOKEN: $CI_JOB_TOKEN" \
  --form ref="$REF" \
  --form "variables[VERSION]=$VERSION" \
  --form "variables[PURPOSE]=$PURPOSE" \
  "$API_URL/projects/${PROJECT_ID}/trigger/pipeline")

# Extract pipeline ID
PIPELINE_ID=$(echo "$PIPELINE_JSON" | jq -r '.id')

if [[ -z "$PIPELINE_ID" || "$PIPELINE_ID" == "null" ]]; then
  echo "❌ Failed to get pipeline ID from response."
  echo "$PIPELINE_JSON"
  exit 1
fi

echo "✅ Pipeline triggered: ID=$PIPELINE_ID"
echo "🔄 Waiting for pipeline to complete..."

# Poll for status
while true; do
  STATUS=$(curl --fail --silent --header "JOB-TOKEN: $CI_JOB_TOKEN" \
    "$API_URL/projects/${PROJECT_ID}/pipelines/${PIPELINE_ID}" | jq -r '.status')

  echo "🔍 Status: $STATUS"
  case "$STATUS" in
    success)
      echo "✅ Downstream pipeline succeeded."
      exit 0
      ;;
    failed|canceled|skipped|manual)
      echo "❌ Downstream pipeline ended with status: $STATUS"
      exit 1
      ;;
    running|pending)
      sleep 5
      ;;
    *)
      echo "⚠️ Unexpected status: $STATUS"
      exit 1
      ;;
  esac
done
