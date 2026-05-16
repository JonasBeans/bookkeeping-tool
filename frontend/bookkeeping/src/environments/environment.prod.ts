// Angular production environment configuration
// Set apiBaseUrl to empty string to use same-origin backend when deployed.
// Override at build time by editing this file or using a CI replacement step.
export const environment = {
  production: true,
  apiBaseUrl: 'http://home.bytesbyboon.be:21595/bookkeeping', // e.g. 'https://api.example.com' if backend hosted separately
  keycloak: {
    url: 'http://keycloak.local',
    realm: 'production',
    clientId: 'bookkeeping'
  }
};
