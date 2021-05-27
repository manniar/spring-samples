
This application demonstrates:

* Spring Boot Web application
* Spring Data JDBC persistence in H2 with Lombok simplified entities
* OAuth2 resource server / JWT authentication and @PreAuthorize authorization
* Actuator with micrometer
* Challenges of JUnit5 testing controllers and services with all above

In real life, I would use a common parent pom.xml for similar boot rest projects, but I wanted this demo to be standalone. Also, there would be docker/jib/kube/etc cloud stuff, but those I ruled out of scope.
