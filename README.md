This Calorie Calculator "light" REST API provides information about my actual skills through some basic functions and setups.
In function the API is the backend layer of a calorie calculator webapp.

The main functions in a few words:

- Create an account, confirm registration through a verification email, login 
- Managing account (update profile, delete profile)
- Saving, updating, deleting calorie intakes, sport activities and foods

Some additional information:

- Auth0 authentication 
- Two Roles are introduced (ADMIN, USER) 
- Delivering states through HATEOAS
- Resources saved in MYSQL DB
- Tests running in testcontainers (MySQL) (not all of test class has developed - only 2 controller test, 1 service test, 2 repository test where queries have created with @Query annotation and 1 integration test)
- Project documentation available at `/swagger-ui/index.html`

Project is migrated to Spring Boot 3.1.0! Therefor some modifications were executed:

- H2 DB in dev environment has removed, instead of I use TestContainer, and I tested the docker compose support as well
so I created a compose.yaml which is recognised by the app after startup.
- Flyway migration removed I rely on Hibernate to create schema.
- I use problem+json format to handle errors therefore RestExceptionController class was recoded.

I permanently develop this API with further functions...I'm opened to get advices and feedbacks from anyone who would like to help me to become a better developer :)

Thanks