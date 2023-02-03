# User service Airport  
## Indytskyi Artem 
---
### Stack of technologies :smiley:

- `Spring Boot 3.0.2`
- `Flyway` 
- `JWT`
- `Postgres`
- `TestContainers`
- `Redis`

You will like those projects!

---
### General info :musical_note:
This is part of a test project based on the simulation of the airport system. This microservice implements the security of the project.

It includes:
1. registration part
2. identity verification
3. login part 
4. logout part
---
### How to start service locally :construction_worker:

No additional configurations are needed to start the service. All configurations are in application.yaml.

#### Docker images

In order to start the project you will need to run the docker containers. 

[image download site](https://hub.docker.com) - Docker hub

1. Download the last Redis image

 ````
docker pull redis
````
2. Start container
```
docker run -p 6379:6379 -d redis:latest redis-server --requirepass "nickfury" 

````
For a guide for usage with Docker, [checkout the docs](https://github.com/maildev/maildev/blob/master/docs/docker.md).

3. Download the image for your email tests
```
docker pull maildev/maildev
````
3. Start container
````
docker run -p 1087:1080 -p 1025:1025 maildev/maildev
````
Run your application :sunglasses:

---
### Usage :star:
The user of this service can perform next methods:

- Get all passegers or by Email `Admin + Authorized`
- Create account `User`
- Login to your  `User`
- Resend Confirmation Email `User`
- Refresh token `User`
- Validate Token `User + Authorized`
- Logout of the account `User + Authorized`
- Update passenger `User + Authorized`
- Delete user `User + Authorized`
- Update user `User + Authorized`

#### To test the microservice (2 ways):
- [Open the Swagger](http://localhost:8080/swagger-ui/index.html#/) - port on which you run the application
- Add a file for the postman from the root folder 

### IMPOTRANT :fire: :fire:
- The hash of the password is passed to the entity, but not the password. As planned, the password should be hashed on the client side.
- Go to the test page to open and [confirm the email](http://0.0.0.0.0:1087) <- `Replace the port on which you are running` 
- Replace the secret key with your personal key to prevent them from hacking and generating a token (the key can be found in the yam file )
- bootstrap.yml - whose file calls a separate service 
 so that all your configurations and settings are pulled from a separate service and are secured  

---
### THANKS :heart:
---
