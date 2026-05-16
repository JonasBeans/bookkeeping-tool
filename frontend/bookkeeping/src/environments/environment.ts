// Angular development environment configuration
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080',
  keycloak: {
    url: 'http://keycloak.local',
    realm: 'production',
    clientId: 'bookkeeping'
  }
};
