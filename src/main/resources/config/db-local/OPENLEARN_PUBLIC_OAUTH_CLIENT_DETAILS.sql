INSERT INTO OAUTH_CLIENT_DETAILS (CLIENT_ID, RESOURCE_IDS, CLIENT_SECRET, SCOPE, AUTHORIZED_GRANT_TYPES, WEB_SERVER_REDIRECT_URI, AUTHORITIES, ACCESS_TOKEN_VALIDITY, REFRESH_TOKEN_VALIDITY, ADDITIONAL_INFORMATION, AUTOAPPROVE) VALUES ('openlearnapp', 'res_openlearn', 'my-secret-token-to-change-in-production', 'read,write', 'password,refresh_token,authorization_code,implicit', '', 'ROLE_ADMIN,ROLE_STUDENT', 1800, 2000, '{}', 'true');
