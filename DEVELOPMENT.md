# SDK Development Notes


## Profile and Authentication

The Apigee SDK can be pre-configured using a profile and auth context. These 2 items have been separated because an authentication context can be used across multiple orgs and environments as long as all are setup in the same account.

The profile settings will allow the user to configure environment specific default values such as organization, environment or management api endpoint.

The credentials configuration on the other hand will track not only a static auth context but also manage the access and refresh tokens used to transact against the management api. 

## Profile Settings File

A profile in the profile settings file can be activated by either setting the environment variable `APIGEE_PROFILE` or Java system property `-Dapigee.profile` to the name of the profile the user wishes to activate.

Example:
 
On Linux based operating systems use the following command to set the active profile to exampleProfile:

```bash
export APIGEE_PROFILE="exampleProfile"
```

On Windows the following command will activate the exampleProfile:

```cmd
set APIGEE_PROFILE="exampleProfile"
```

| Parameter         | Description                                                                          |
|-------------------|--------------------------------------------------------------------------------------|
| `output_format`   | the output used to return command results either `json` or `text`                    |
| `api_url`         | the url of the management api endpoint                                               |
| `api_version`     | the api version to use                                                               |
| `organization`    | the organization to connect to                                                       |
| `environment`     | the environment within an organization to connect to                                 |


The profile settings file default location is `~/.apigee/config`. However an alternative location of the file can be set using either environment or Java system properties. The SDK will locate the profile settings file in the following order:

1\. Environment Variables

  The SDK will read the file specified in environment variable `APIGEE_PROFILES_FILE`.

2\. Java system properties

  The SDK will read the Java system property `-Dapigee.profiles.file`

3\. Default profile file

  The SDK will read the file in default location `~/.apigee/config`


## Credentials Profile File

### Format

The credentials auth file will store auth credentials that can be used to authenticate against the Apigee management API. The format of the file depends on what authentication mechanism is defined in the profile settings.

| Parameter         | Use             | Description                                                             |
|-------------------|-----------------|-------------------------------------------------------------------------|
| `auth_type`       | `basic`,`oauth` | the authentication type of the user account                             |
| `auth_token_url`  | `basic`,`oauth` | the authentication token service url to request access tokens from      |
| `auth_username`   | `basic`,`oauth` | a valid Apigee portal username                                          |
| `auth_password`   | `basic`         | the password of the username                                            |
| `access_token`    | `basic`,`oauth` | access token that can be used to access the gateway apis                |
| `refresh_token`   | `basic`,`oauth` | refresh token that can be used to get another access token              |
| `client_id`       | `basic`,`oauth` | the client id to use when interacting with the auth and management apis |
| `client_secret`   | `basic`,`oauth` | the client secret for the oauth client                                  |


Example:

```ini
[default]
auth_type=basic
auth_user=Fred.Flintstone@example.com
auth_password=topsecret
access_token=eyJhbGciOiJSUzI1NiJ9.eyJd...xUQ
refresh_token=eyJhbGciOiJSUzI1NiJ9.eyJqdGk...fGrL

[profileWithOAuth]
auth_type=oauth
auth_user=Wilma.Flintstone@example.com
access_token=eyJhbGciOiJSUzI1NiJ9.eyJd...xUQ
refresh_token=eyJhbGciOiJSUzI1NiJ9.eyJqdGk...fGrL
```


### Setting an Alternate Credentials Profile

The Apigee SDK uses the default profile by default, but there are ways to customize which profile is sourced from the credentials file.

You can use the AWS Profile environment variable to change the profile loaded by the SDK.

On Linux based operating systems use the following command to set the active profile to exampleProfile:

```bash
export APIGEE_PROFILE="exampleProfile"
```

On Windows the following command will activate the exampleProfile:

```cmd
set APIGEE_PROFILE="exampleProfile"
```

Setting the `APIGEE_PROFILE` environment variable affects credential loading for the Apigee SDK, CLI and maven plugin. To change the profile for a Java application, you can also use the system property `apigee.profile`.



The credentials profile file location will be set in the following order:

1. Environment Variables

`APIGEE_CREDENTIAL_PROFILES_FILE`

2. Java system properties

`-Dapigee.credential.profiles.file`

3. Default credentials profile file

`~/.apigee/credentials`

