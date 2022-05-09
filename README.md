# Testing

To start working with app locally
- add proper values for db connection properties in `application.properties` file
- to inject dummy data and default roles please send GET request to `http://localhost:8080/inject`
- to register a new user please send POST request to `http://localhost:8080/register`. 

    Example:
    ```java
    {"email":"bchupika@mate.academy", "password":"12345678", "repeatPassword":"12345678"}
    ```
- to obtain a JWT token please send POST request to `http://localhost:8080/login`.

    Example:
    ```java
    {"login":"bchupika@mate.academy", "password":"12345678"}
    ```
- to send data to any url pass obtained JWT token with Authorization header.

    Example:
    
        HTTP GET: `http://localhost:8080/`
        Headers: `Authorization: Bearer <YOUR_TOKEN>`

### Requirements
Your task is to cover with unit tests some classes:
- `security` package:
    - JwtTokenProvider
    - AuthenticationServiceImpl
    - CustomUserDetailsService
- `validation` package:
    - EmailValidator
    - PasswordValidator
- `dao` package:
    - RoleDaoImpl
    - UserDaoImpl
- all classes from `service` package

#### [Try to avoid these common mistakes while solving task](https://mate-academy.github.io/jv-program-common-mistakes/java-spring-boot/testing/java-spring-tests.html)
