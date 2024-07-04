<a id="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/denniszwang/GoFundMe">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Go Fund Me Clone</h3>

  <p align="center">
    A Full-Stack Crowdfunding Project
    <br />
    <a href="https://github.com/denniszwang/GoFundMe/blob/main/wiki.md"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/denniszwang/GoFundMe">View Demo</a>
    ·
    <a href="https://github.com/denniszwang/GoFundMe/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    ·
    <a href="https://github.com/denniszwang/GoFundMe/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<!-- [![Product Name Screen Shot][product-screenshot]](https://example.com) -->

This project is a GoFundMe-inspired platform that enables users to support and initiate fundraising campaigns through a seamless front and backend service. Whether creating organizations, setting up contributor profiles, or making donations, users can easily navigate our web interface or use a Java-based terminal application for a more hands-on approach. Designed to be accessible and versatile, our platform empowers individuals and organizations to make a meaningful impact, one donation at a time. Detailed features and implementation guides are available in our project's wiki documentation.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

![MongoDB]
![Express.js]
![React.js]
![NodeJS]
![Java]

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

This is an example of how to list things you need to use the software and how to install them.
* npm
  ```sh
  npm install npm@latest -g
  ```
* MongoDB

### Installation

1. **Download Project Code**
   Begin by cloning the repository to get the project code on your local machine.
   ```sh
   git clone https://github.com/denniszwang/GoFundMe.git
   ```

2. **Install and Configure Node Express App**
  - **Install Node.js: Make sure Node.js is installed on your system.
  - **Install Node Packages: Navigate to the project directory and install the required npm packages. Run this command with administrator privileges.
    ```sh
      npm install
    ```
   
3. **Install MongoDB**
  Follow the official MongoDB documentation to install MongoDB on your system.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage

To effectively use the application from both the frontend and backend, follow these steps:

1. **Start the Admin App**
  - Navigate to the `admin` directory within the project.
  - Start the admin application by running:
    ```sh
    node admin.js
    ```

2. **Start the RESTful API Server**
  - Within the same `admin` directory, start the API server by executing:
    ```sh
    node api.js
    ```

3. **Set Up Organization App** 
  - Ensure you have Java installed on your system.
  - Locate the `UserInterface` class in the project.
  - Run the `UserInterface` class, passing the login ID and password you created for an organization via the Admin app as runtime arguments to the main method.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Features -->
## Features

The application's capabilities are organized into the following main categories, each with specific features:

### Organization Management
- **Create Organization**: Set up new organization profiles.
- **Edit Organization**: Modify existing organization information.
- **Delete Organization**: Remove an organization from the platform.
- **View Organization Details**: Look up detailed information about organizations.

### Contributor and Donations
- **Create Contributor**: Add new contributors to the platform.
- **Edit Contributor**: Change details for existing contributors.
- **Delete Contributor**: Erase a contributor's information from the system.
- **View Contributor**: See detailed information about contributors.
- **Donate Money**: Enable contributors to make donations.
- **Display Donations**: Show all donations for a specific fund or organization.
- **Aggregate Donations by Contributor**: Compile donation amounts by each contributor.

### Security and User Management
- **Login/Logout**: Manage user sessions securely.
- **Password Encryption**: Protect user passwords with encryption.
- **New User Registration**: Register a new account on the platform.
- **Change Password**: Allow users to update their passwords.
- **Edit Fund**: Permit the edition of funds from the system.
- **Delete Fund**: Permit the deletion of funds from the system.

_For more detailed feature information, please refer to the [Documentation](https://github.com/denniszwang/GoFundMe/blob/main/wiki.md)._

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Dennis Wang - [LinkedIn](https://www.linkedin.com/in/denniswang1011/) - denniswz123@gmail.com

Tiffany Gao - 

Qianyue Ding -

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- ACKNOWLEDGMENTS -->
## Acknowledgments
* Part of the project is based on the [CIS 573](https://chrismurphyonline.github.io/cis5730su23/) excellent course taught by Chris Murphy at University of Pennsylvania.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[MongoDB]: https://img.shields.io/badge/mongoDB-47A248?style=for-the-badge&logo=MongoDB&logoColor=white
[Express.js]: https://img.shields.io/badge/express.js-%23404d59.svg?style=for-the-badge&logo=express&logoColor=%2361DAFB
[NodeJS]: https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white
[Java]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white