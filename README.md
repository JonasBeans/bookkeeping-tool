# Book keeping tool 

## Local development test data

Run the application with the `local` Spring profile to load sample development data through Liquibase:

```bash
SPRING_PROFILES_ACTIVE=local
```

The Docker Compose Spring Boot service already sets this profile for local runs.

The local profile enables the Liquibase `local` context, which runs
`src/main/resources/db/changelog/local-test-data.yaml`.
It seeds balance sub-posts and transactions for multiple bookyears so the bookyear selector, transaction grouping,
cost/income center totals, and charts can be tested locally.

## AI cost-center prefilter

Excel uploads can optionally prefill transaction cost centers with Anthropic Claude. The classifier uses the
transaction description, other party, message, amount, and the configured cost-center list. It is disabled by default
because transaction text may contain personal data and is sent to Anthropic when enabled.

Set these environment variables to enable it:

```bash
AI_COST_CENTER_ENABLED=true
ANTHROPIC_API_KEY=your-api-key
ANTHROPIC_MODEL=claude-3-5-haiku-latest
AI_COST_CENTER_CONFIDENCE_THRESHOLD=0.75
```

Predictions are only applied when Claude returns an existing cost center with confidence at or above the threshold.
If the API call fails, returns invalid JSON, predicts an unknown cost center, or has low confidence, the upload
continues and the transaction remains available for manual assignment.
