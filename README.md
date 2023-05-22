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
- Tests running in testcontainers (MySQL) (not all of test class has developed - only 2 controller test, 1 service test, 2 repository test where queries have created with @Query annotation and 1 integration test) 
- H2 DB in dev environment and MySQL DB in prod environment
- 

I permanently develop this API with further functions...I'm opened to get advices and feedbacks from anyone who would like to help me to become a better developer :)

Thanks