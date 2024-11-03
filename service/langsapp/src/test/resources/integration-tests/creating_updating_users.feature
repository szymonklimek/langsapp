Feature: Creating and updating users test

  Scenario: 1. Fresh user can be created, updated and returned correctly
    # Creating user
    Given authenticated user with id "user-id-coming-from-oauth-identity-provider"
    * request method "POST"
    * request path "/user"
    * request body
      """
      {
        "id": "unique_user_id"
      }
      """
    When request is sent
    Then response code is "200"
    # Getting profile of the user
    * request method "GET"
    * request path "/profile"
    When request is sent
    Then response code is "200"
    * response body is
      """
      {"user_profile":{"id":"user-id-coming-from-oauth-identity-provider","following_count":0,"followers_count":0,"name":null,"avatar_url":null,"language_settings":null}}
      """
    # Updating user data
    * request method "PATCH"
    * request path "/user"
    * request body
      """
      {
        "username": "Unique username"
      }
      """
    When request is sent
    Then response code is "200"
    # Updating user data
    * request method "PATCH"
    * request path "/user"
    * request body
      """
      {
        "language_settings": {
          "learn_language": {
            "code": "en"
          },
          "base_language": {
            "code": "pl"
          },
          "support_language": {
            "code": "de"
          }
        }
      }
      """
    When request is sent
    Then response code is "200"
    # Getting profile of the user again
    * request method "GET"
    * request path "/profile"
    When request is sent
    Then response code is "200"
    * response body is
      """
      {"user_profile":{"id":"user-id-coming-from-oauth-identity-provider","following_count":0,"followers_count":0,"name":"Unique username","avatar_url":null,"language_settings":{"learn_language":{"code":"en"},"base_language":{"code":"pl"},"support_language":{"code":"de"}}}}
      """
