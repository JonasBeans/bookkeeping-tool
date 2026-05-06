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
