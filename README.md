# GitHub API

***
* [Specification](#specification)
    + [Path](#path)
        * [GET /user/{username}/repository](#get-userusernamerepository)
            + [Description](#description)
        * [Responses](#responses)
    + [References](#references)
        - [Repository](#repository)
        - [Branch](#branch)
        - [Error](#error)
* [newman report](#newman-report)
    + [Command](#command)
    + [Result](#result)
* [Jacoco Report](src/main/resources/jacoco_report.zip)
    + [Overall Coverage Summary](#overall-coverage-summary)
    + [Coverage Breakdown](#coverage-breakdown)
* [Deploy](#deploy)

## Specification

***

### Path

##### GET /user/{username}/repository

###### Description

List all non-fork GitHub repositories for a user, with branches

##### Responses

- 200 OK `application/json`

`Example:`

```json
[
    {
        "name": "sample-repo",
        "owner": "joserbatista",
        "branchList": [
            {
                "name": "string",
                "commitHash": "39e2c2a8b61be79544905727c4c3d0e440412cd8"
            }
        ]
    }
]
```

- 400 Bad Request `application/json`

`Example:`

```json
{
    "statusCode": "string",
    "message": "string"
}
```

### References

`Example:`

```json
{
    "statusCode": "BAD_REQUEST",
    "message": "API rate limit exceeded"
}
```

#### Repository

| Name       | Type          | Description                 | Required |
|------------|---------------|-----------------------------|----------|
| name       | string        | _Example:_ `"sample-repo"`  | No       |
| owner      | string        | _Example:_ `"joserbatista"` | No       |
| branchList | List\<Branch> |                             | No       |

`Example:`

```json
{
    "name": "sample-repo",
    "owner": "joserbatista",
    "branchList": [
        {
            "name": "string",
            "commitHash": "39e2c2a8b61be79544905727c4c3d0e440412cd8"
        }
    ]
}
```

#### Branch

| Name       | Type   | Description                                             | Required |
|------------|--------|---------------------------------------------------------|----------|
| name       | string | _Example:_ `"main"`                                     | No       |
| commitHash | string | _Example:_ `"39e2c2a8b61be79544905727c4c3d0e440412cd8"` | No       |

`Example:`

```json
{
    "name": "main",
    "commitHash": "39e2c2a8b61be79544905727c4c3d0e440412cd8"
}
```

#### Error

| Name       | Type   | Description                            | Required |
|------------|--------|----------------------------------------|----------|
| statusCode | string | _Example:_ `"BAD_REQUEST"`             | No       |
| message    | string | _Example:_ `"API rate limit exceeded"` | No       |

`Example:`

```json
{
    "statusCode": "BAD_REQUEST",
    "message": "API rate limit exceeded"
}
```

## newman report

***

### Command

```shell
newman run resources/cocus-mobility-api.postman_collection.json --env-var baseUrl=cocus-server --env-var username=joserbatista
```

### Result

```
→ getRepositories_ValidUsername_ReturnsOk
GET http://cocus-server/user/joserbatista/repository [200 OK, 511B, 578ms]
✓  Status code is 200 OK
✓  Response has valid schema

→ getRepositories_InvalidAcceptHeader_Returns406
GET http://cocus-server/user/1b961dcd-025c-4614-b803-3d17ace1a11d/repository [406 Not Acceptable, 167B, 6ms]
✓  Status code is 406 Not Acceptable
✓  statusCode matches
✓  message matches

→ getRepositories_InvalidUsername_Returns404
GET http://cocus-server/user/3a522767-f8f1-476a-97ea-2fd01507ea19/repository [404 Not Found, 126B, 175ms]
✓  Status code is 404 Not Found
✓  statusCode matches
✓  message matches

→ total run duration: 913ms                                       
→ total data received: 564B (approx)                              
→ average response time: 253ms [min: 6ms, max: 578ms, s.d.: 239ms]
```

## [Jacoco Report](src/main/resources/jacoco_report.zip)

***

### Overall Coverage Summary

| Package     | Class, %      | Method, %     | Line, %         |
|-------------|---------------|---------------|-----------------|
| all classes | 93.1% (27/29) | 94.6% (53/56) | 95.3% (123/129) |

### Coverage Breakdown

| Package                                       | Class, %   | Method, %    | Line, %       |
|-----------------------------------------------|------------|--------------|---------------|
| com.cocus.mobility.challenge                  | 50% (1/2)  | 50% (1/2)    | 50% (1/2)     |
| com.cocus.mobility.challenge.client           | 100% (2/2) | 100% (5/5)   | 100% (13/13)  |
| com.cocus.mobility.challenge.client.webclient | 100% (8/8) | 100% (15/15) | 97.9% (47/48) |
| com.cocus.mobility.challenge.controller       | 100% (5/5) | 100% (11/11) | 100% (28/28)  |
| com.cocus.mobility.challenge.controller.dto   | 75% (3/4)  | 77.8% (7/9)  | 76.5% (13/17) |
| com.cocus.mobility.challenge.entity           | 100% (3/3) | 100% (4/4)   | 100% (4/4)    |
| com.cocus.mobility.challenge.entity.exception | 100% (3/3) | 100% (3/3)   | 100% (3/3)    |
| com.cocus.mobility.challenge.service          | 100% (2/2) | 100% (7/7)   | 100% (14/14)  |

## Deploy

***

This service can be deployed using the [provided](githubapp-cloudformation-template.yaml) AWS Cloudformation Template, which
uses an image deployed in a public Amazon Elastic Container Registry using the provided [Dockerfile](Dockerfile)

This app also supports using GitHub credentials, to avoid Rate Limits in GitHub API. That can be done by passing the environment variables:
GITHUB_USERNAME and GITHUB_TOKEN, for instance:

```
GITHUB_USERNAME=joserbatista
GITHUB_TOKEN=<githubtoken>
```