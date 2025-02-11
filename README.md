# BookMySpace App - _Java Web Programming Course_

Duțu Maria-Alexandra, Master Baze de date si tehnologii software*

## TECHNOLOGIES
- **Backend:** Java, Spring Boot, Lombok
- **Database:** PostgreSQL, Docker
- **Frontend:** use swagger to demonstrate functionalities
- **Tests:** JUnit, Mockito

## PROJECT REQUIREMENTS

I. Define a system of your choice.
- [x] [Jump To Section](#rest-endpoints---business-requirements) Define 10 business requirements for the chosen business domain
- [x] [Jump To Section](#detailed-business-requirements) Prepare a document based on the 10 business requirements containing a description of 5 main features your project should contain for the MVP (minimum viable product) phase.
- [x] [Jump To Section](#) Create a repository for your project and commit the document for review.

II. The project should consist of a Spring Boot Application containing:
- [x] [Jump To Section](#rest-endpoints) REST endpoints for all the features defined for the MVP. You should define at least 5 endpoints.
- [x] [Jump To Section](#services) Beans for defining services (implementing business logic). One service per feature.
- [x] [Jump To Section](#repositories) Beans for defining repositories One repository per entity.
- [x] [Jump To Section](#testing) Write unit tests for all REST endpoints and services and make sure all passed.
- [x] [Jump To Section](#database-structure) The data within the application should be persisted in a database. Define at least 6 entities that will be persisted in the database, and at least 4 relations between them.
- [x] [Jump To Section](#entities) You should validate the POJO classes. You can use the existing validation constraints or create your own annotations if you need a custom constraint.
- [x] [Jump To Section](#swagger-documentation) Document the functionalities in the application such that anyone can use it after reading the document. Every API will be documented by Swagger. You can also export the visual documentation and add it to your main document presentation.
- [x] [Jump To Section](#frontend) The functionality of the application will be demonstrated using Postman or GUI (Thymeleaf,Angular, etc.).

## PROJECT DESCRIPTION

**BookMySpace App** is an app designed to simplify the booking and management of workspaces. It allows users to reserve desks, manage their reservations, view available spaces and choose a favourite desk.

## DATABASE STRUCTURE

![](/ReadmePhotos/ERD.png)

### DATABASE EXPLANATION
Users can have one or more roles (Admin, User).

Users can reserve one or more desks based on availability and personal preferences.

Users have the ability to manage and update their desk reservations.

Desks are assigned to rooms, and each room can have multiple desks.

Each desk is associated with a room and can be booked by different users. Each reservation is tied to a specific time slot and user, ensuring efficient space management within the app.

## REST ENDPOINTS - BUSINESS REQUIREMENTS

Users can create accounts and login.

Each type of user has access to different functionalities of the app.

| **Endpoint / Type of User**  |                                                               **Regular User (The Employee)**                                                               |                                                                            **Admin**                                                                            |
| :----:       |:-----------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| **Get**      | <ul><li>View all desk reservations made by them.</li><li>View the available desks and their details.</li><li>See their profile and favorite desk.</li></ul> | <ul><li>View all users and their desk reservations.</li><li>See all rooms, desks, and their availability.</li><li>See all reservations in the system.</li></ul> |
| **Create**   |                               <ul><li>Create new desk reservations for themselves.</li><li>Mark a desk as favorite.</li></ul>                               |        <ul><li>Create new users and assign them roles.</li><li>Create new rooms and desks.</li><li>Manage desk availability and reservations.</li></ul>         |
| **Update**   |                     <ul><li>Update their favourite desk and cancel reservation.</li></ul> <li>Update their password.</li></ul>                      |                                                                              |
| **Delete**   |                                                       <ul><li>Delete their favourite desk.</li></ul>                                                        |                                                        <ul><li>Delete any user, room, or desk.</li></ul>                                                        |

### DETAILED BUSINESS REQUIREMENTS

#### CREATE

1. Add new user (Create account / Register)
2. Create new rooms
3. Create new desks
4. Create new reservations
5. Mark a desk as favorite for a user

#### READ

1. See all user's desk reservations and their details
2. See all available desks and their details
3. See all rooms
4. See all user information 

#### UPDATE

1. Update user information (password/favourite desk)

#### DELETE

1. Delete rooms
2. Delete desks
3. Delete users
4. Delete roles


## Main features for minimum viable product phase (MVP)

1. A user can create an account, and the password will be encrypted when saved in the database. After registration, users can log in using the email and password they provided. You can retrieve details about a user based on their `userId` or get a list of all the users in the database. Users can also be deleted by their `userId`.

2. Roles can be added to the database by providing one of the role names: `admin`, `user`. You can retrieve information about roles by `roleId` and delete them by `roleId`.

3. Rooms can be created by providing a name, floor number, and capacity. You can get information about a specific room by providing the `roomId`. Rooms can also be deleted by their `roomId`.

4. Desks can be created and assigned to specific rooms. You can get details about a desk by providing the `deskId`, and you can delete desks by their `deskId`.

5. Users can create reservations for desks by specifying the desk and time. You can retrieve a list of all reservations for a particular desk. Reservations can also be deleted by their `reservationId`.

6. A user can have one favorite desks. You can retrieve a user’s favorite desk by their `userId`. A desk can be chosen to be favourite by multiple users.


## Backend

### Entities

There are 6 entities (*AppUser, UserRole, Role, Desk, Room, Reservation*).

Each entity is annotated with **@Entity** and other Lombok annotations (used mainly for writing less and cleaner code, but providing the constructors, getters, setters and toString, hashcode and equals functions)

Each entity has a unique identifier, generated automatically by Spring when the object is saved into the database. All ids are Long.

One entity (*UserRole*) has embedded id, as these tables are the result of Many-to-Many relationships, therefore needing a composed primary key (the two primary keys of the main entities).

Each embedded id is a separated class, which can be found in the embeddedIds package.

I used  *@NotNull* annotation on the fields that should not have null values. I also used @Email and @Valid annotation to trigger validation of POJOs in requests.

```java
    @Email
    @NotNull
    private String email;
```

### REPOSITORIES
Each java class has a corresponding Interface class which extends JpaRepository.

- RoleRepository
- UserRepository
- UserRoleRepository
- DeskRepository
- RoomRepository
- ReservationRepository


### SERVICES
Each java class has a corresponding Service class.

- RoleService
- UserService
- UserRoleService
- DeskService
- RoomService
- ReservationService


### Controllers

Each java class has a corresponding Controller class.

- RoleController ( */role* )
- UserController ( */user* )
- UserRoleController ( */userRole* )
- DeskController ( */desk* )
- RoomController ( */room* )
- ReservationController ( */reservation* )

#### REST Endpoints

In the controllers, there are defined CRUD endpoints, as presented in the next sections.

I used *@RequestBody* and *@PathVariable* to send information with the request.

The *@Parameter* annotation is only used for Swagger.

##### Create

```java
    @PostMapping("/register")
    public ResponseEntity<User> create(@RequestBody @Parameter(description = "User data provided by the register form") User user) {
        return ResponseEntity.ok(userService.create(user));
    }
```

##### Read

```java
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") @Parameter(description = "The id of the user you want to get information about") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
```

##### Update

```java
    @PostMapping("/changePassword/{id}")
    public ResponseEntity<?> changePassword(@PathVariable("id") @Parameter(description = "The id of the user") Long id, @RequestBody String password){
        userService.changePassword(id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }
```

##### Delete

```java
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @Parameter(description = "The id of the user") Long id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
```

### Exception Handling

I have created custom exceptions, such as **UserNotFound** or **UserAlreadyExists**.

```java
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found!");
    }
}
```

They are thrown in the services methods. For example, if the user with a given id is not found, the method will throw the UserNotFound exception.

```java 
    public void delete(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException();
        }
    }
```

Then, they are caught by the GlobalExceptionHandler, which is annotated with *@ControllerAdvice*.

```java
 @ExceptionHandler({UserNotFoundException.class})
public ResponseEntity<?> handle(UserNotFoundException userNotFoundException) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundException.getMessage());
}
```

### Testing

#### Unit Testing

I have tested the methods in the controllers and services. For this, I used JUnit and Mockito.

*Example Test Of a Controller Method*

```java
    @Test
    public void createReservation() throws Exception {
    
    }
```

*Example Test of a Service Method*

```java
    @Test
    void create() {
        
    }
```


#### Test Coverage

![](/ReadmePhotos/TestCoverage.png)

## Swagger Documentation

URL: http://localhost:8081/swagger-ui.html

![](/ReadmePhotos/Swagger.png)

To enable Swagger, I used the SpringDoc OpenApi dependency.

Each endpoint from each controller is annotated with *@Operation* and *@ApiResponse*. Additionally, the method's parameters are annotated with *@Parameter*.

```java
    @Operation(summary = "Get information about an user", description = "Get information about a certain user by providing their id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User was found in the database"),
            @ApiResponse(responseCode = "404", description = "User was NOT found in the database")
    })
    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") @Parameter(description = "The id of the user you want to get information about") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
```
