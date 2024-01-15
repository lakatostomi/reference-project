Calorie Calculator "light" REST API is a monolithic backend application. 

The API has the following functions:

- Create an account, confirm registration through a verification email, login 
- Managing account (update profile, delete profile)
- Saving, updating and deleting calorie intakes, sport activities and foods

Technical implementations:

- Auth0 JWT authentication 
- Two roles were introduced (ADMIN, USER) 
- Delivering states through HATEOAS
- Resources saved in MYSQL DB
- Tests are running in TestContainers (MySQL) (not all test classes are developed - only 2 controller tests, 1 service test, 2 repository tests where custom queries were created with @Query annotation, and 1 integration test)
- Project documentation available at `/swagger-ui/index.html`

The project is migrated to Spring Boot 3.1.0! Therefore some modifications were executed:

- H2 DB in the dev environment is removed, instead I use TestContainer support of Spring boot, and I tested the docker compose support as well. I have created a `compose.yaml` file which is recognized by the app after startup.
- Flyway migration was removed I rely on Docker and Hibernate to create schema.
- I use problem+json format to handle errors therefore RestExceptionController class was recoded.

There are 3 ways to run the API:

In order to test the registration function of the API you have to set a valid email and password in the `application.properties` file.

- `CalorieCalculatorApplication.class` will bootstrap the App using the dev environment and use the `compose.yaml` file in classpath to create DB.
- After disabling compose support in `application-dev.properties` `CalorieCalculatorApplicationTests.class` will bootstrap the App using Spring boot TestContainer support in dev environment and set up a TestContainer to create DB. 
- Build an image with `spring-boot-maven-plugin` and use the `./docker-compose/service.yaml` to compose up the App and DB in the prod environment.

CI/CD
- I have created a '.gitlab-ci.yml' file to configure a CI/CD pipeline on GitLab. 2 pipelines were created both are in the same file:
  1. I build and push the image with a `Dockerfile` to the DockerHub Registry.
  2. I build and push the image to Google Container Registry with the maven JIB plugin, this pipeline uses my custom docker image that I configured [`here`](https://github.com/lakatostomi/rest-api-on-gcp/)

I have deployed the API to Google Cloud therefore I configured a staging environment where the API uses a Cloud SQL instance as a persistence layer.  

I have developed another web app that I deployed to Google Cloud. The Cloud infrastructure of the app was provisioned with Terraform. https://github.com/lakatostomi/rest-api-on-gcp  

Thanks for reading!