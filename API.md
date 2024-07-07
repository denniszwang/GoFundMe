# API Documentation

Welcome to our API documentation. Our API provides a set of endpoints for managing organizations, funds, contributors, and donations within our platform. Below you will find a comprehensive guide to using these endpoints.

## Base URL

All URLs referenced in the documentation have the following base:
```sh
http://<your-domain>:3001
```

## Endpoints

### General

- **GET /** - Welcome message
  - **Description**: Returns a welcome message from the API.
  - **Response**: "Welcome to my API!"

### Organizations

- **POST /createOrg** - Create a new organization
  - **Parameters**:
    - `login`: Organization's login (required)
    - `password`: Organization's password (required)
    - `name`: Organization's name (required)
    - `description`: Organization's description
  - **Response**: Status of operation and organization data on success.

- **GET /findOrgByLoginAndPassword** - Find organization by login and password
  - **Query Parameters**:
    - `login`: Organization's login (required)
    - `password`: Organization's password (required)
  - **Response**: Organization data on success.

- **POST /updateOrg** - Update organization details
  - **Query Parameters**:
    - `login`: Organization's login (required)
    - `password`: Organization's password (required)
    - `name`: New name of the organization
    - `description`: New description of the organization
  - **Response**: Status of operation and updated organization data on success.

### Funds

- **POST /createFund** - Create a new fund
  - **Parameters**:
    - `orgLogin`: Organization's login (required)
    - `name`: Fund's name (required)
    - `description`: Fund's description
    - `goal`: Fund's goal amount
  - **Response**: Status of operation and fund data on success.

- **GET /findFundsByOrgLogin** - Find funds by organization login
  - **Query Parameters**:
    - `orgLogin`: Organization's login (required)
  - **Response**: List of funds associated with the organization on success.

### Contributors

- **POST /addContributor** - Add a new contributor
  - **Parameters**:
    - `name`: Contributor's name (required)
    - `email`: Contributor's email (required)
  - **Response**: Status of operation and contributor data on success.

### Donations

- **POST /makeDonation** - Make a donation to a fund
  - **Parameters**:
    - `fundId`: ID of the fund (required)
    - `contributorEmail`: Email of the contributor (required)
    - `amount`: Donation amount (required)
  - **Response**: Status of operation and donation data on success.
