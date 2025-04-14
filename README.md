# Physchem API for rapidtox   

## Building this project
From the root of the repo issue the following commands:

```bash
./mvnw clean  
./mvnw package  
```

This will create war file in target folder of this repo which can be used to 
deploy to Tomcat.

## Expected/Required Environment Variables
* RAPIDTOX_CONNECTION_STRING  # JDBC connection string for the RapidTox Data Mart
* RAPIDTOX_USER  # Username for the RapidTox Data Mart connection
* RAPIDTOX_PASSWORD  # Password for the RapidTox Data Mart connection
* RAPIDTOX_ALT_CONNECTION_STRING  # JDBC connection string to the BINGO/Similarity Search DB
* RAPIDTOX_ALT_USER  # Username for the BINGO/Similarity Search DB
* RAPIDTOX_ALT_PASSWORD  # Password for the BINGO/Similarity Search DB
* RTPLOT_API_ENDPOINT  # URI for the RTPlot scatter-plot endpoint
* OAUTH_ISSUER_URI   # URI for the OAuth2 Issuer endpoint
* OAUTH_JWT_SET_URI   # URI for the OAuth2 JWT "set URI" endpoint
* CLOWDER_BASE_URL   # URI for Clowder service used as file store
