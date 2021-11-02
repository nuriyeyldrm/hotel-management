# Hotel Management API Back-end

The Hotel Management API implements a complex domain model flow to book a hotel room. The flow simulates a real world application in terms of having a variety of booking options and business rules.

#### Technologies
- Spring Boot (JPA, Web, Devtools, Validation, Security)
- Spring Framework
- PostgreSQL
- Maven
- Javax
- Jsonwebtoken
- Lombok

##### Application starts on localhost port 8080
- [http://localhost:8080/api/user/*]
- [http://localhost:8080/api/admin/*]
- [http://localhost:8080/api/hotel/*]
- [http://localhost:8080/api/room/*]
- [http://localhost:8080/api/reservation/*] 

#### Available Services
| Http Method | Path | Usage |
| ------ | ------ | ------ |
| GET | /api/user/auth | get user by username |
| GET | /api/admin/{id}/auth | get user by id (preauthorize admin) |
| GET | /api/admin/auth/all | get all users (preauthorize admin) |
| GET | /api/admin/auth/search? | search user info from specified parameters... (preauthorize admin) |
| POST | /api/admin/auth/add | add user (preauthorize admin) |
| POST | /api/user/signup | register |
| POST | /api/user/login | login |
| PUT | /api/user/auth | update to user |
| PUT | /api/admin/{id}/auth | update to user (preauthorize admin) |
| PATCH | /api/user/auth | update to password |
| DELETE | /api/admin/{id}/auth | delete to user (preauthorize admin) |
#### ... continues

#### Postman collections for user and admin part [https://www.getpostman.com/collections/8e1eb5f0f9126387d0a5]

#### NOTE: All method except signup/login methods, needs Authorization Bearer token in header 
![image](https://user-images.githubusercontent.com/24353402/139966947-18285470-4f69-4129-beac-d8067a222fea.png)

### User Resources

##### Request
#
```sh
POST /api/user/signup
```
##### Body
#
```json
{   
    "username": "gregory",
    "password": "test3",
    "email": "gregory@gmail.com",
    "fullName": "Gregory K Kirchner",
    "phoneNumber": "(918) 926-1503",
    "ssn": "446-66-7249",
    "drivingLicense": "467632068",
    "country": "USA",
    "state": "Oklahoma",
    "address": "1350 Bridge Street, Tulsa, OK",
    "workingSector": "IT",
    "birthDate": "06/09/1958"
}
```
##### Response
#
```json
{
    "User registered successfully!": true
}
```
# --------------------------------------------------
##### Request
#
```sh
POST /api/user/login
```
##### Body
#
```json
{   
    "username": "gregory",
    "password": "test3"
}
```
##### Response
#
```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJncmVnb3J5IiwiaWF0IjoxNjM1NjQ2NzAyLCJleHAiOjE2MzU3MzMxMDJ9.tph4RvAlggqA6rBRkgoP2ngjD2dSbow2x7cvY7cDrziWj0TKpdyb3rMNKtaK8TG1PWlhl5R7dxr3DR7Z7v6ORw"
}
```
# --------------------------------------------------
##### Request
#
```sh
GET /api/user/auth
```
##### Response
#
```json
{
    "username": "samuel",
    "email": "samuel@gmail.com",
    "fullName": "Samuel M Donahue",
    "phoneNumber": "(530) 201-1198",
    "ssn": "618-74-2612",
    "drivingLicense": "T1822054",
    "country": "USA",
    "state": "California",
    "address": "4112 Kerry Way Santa Fe Springs, CA",
    "workingSector": "logistic",
    "birthDate": "09/21/1956"
}
```
# --------------------------------------------------
##### Request
#
```sh
PUT /api/user/auth
```
##### Body
#
```json
{   
    "username": "gregory",
    "password": "test3",
    "email": "gregory@gmail.com",
    "fullName": "Gregory K Kirchner",
    "phoneNumber": "(918) 926-1504",
    "ssn": "446-66-7249",
    "drivingLicense": "467632068",
    "country": "USA",
    "state": "Oklahoma",
    "address": "1350 Bridge Street, Tulsa, OK",
    "workingSector": "IT",
    "birthDate": "06/09/1958"
}
```
##### Response
#
```json
{
    "success": true
}
```
# --------------------------------------------------
##### Request
#
```sh
PATCH /api/user/auth
```
##### Body
#
```json
{   
    "oldPassword": "test3",
    "newPassword": "test2"
}
```
##### Response
#
```json
{
    "success": true
}
```
# --------------------------------------------------
##### Request
#
```sh
GET /api/admin/{id}/auth
```
##### Response
#
```json
{
    "id": 18,
    "username": "gregory",
    "password": "$2a$10$lY7QP0ieKjJwNOdchpU1luoirXY8g4juZmr1dzQcud0uZG2DjdH8S",
    "email": "gregory@gmail.com",
    "fullName": "Gregory K Kirchner",
    "phoneNumber": "(918) 926-1504",
    "ssn": "446-66-7249",
    "drivingLicense": "467632068",
    "country": "USA",
    "state": "Oklahoma",
    "address": "1350 Bridge Street, Tulsa, OK",
    "workingSector": "IT",
    "birthDate": "06/08/1958",
    "roles": [
        {
            "id": 1,
            "name": "ROLE_CUSTOMER"
        }
    ],
    "enabled": null
}
```
# --------------------------------------------------
##### Request
#
```sh
GET /api/admin/auth/search?role=3
```
##### Params
#
![image](https://user-images.githubusercontent.com/24353402/139967179-93e34a91-4568-4e38-ae87-6367c35e08d2.png)

##### Response
#
```json
[
    {
        "id": 2,
        "username": "samuel",
        "password": "$2a$10$SE9.KsdwmdTUdTtpl6y3ieIT4R5SL7JjjV5uH.z7WEBwXc09/kxDm",
        "email": "samuel@gmail.com",
        "fullName": "Samuel M Donahue",
        "phoneNumber": "(530) 201-1198",
        "ssn": "618-74-2612",
        "drivingLicense": "T1822054",
        "country": "USA",
        "state": "California",
        "address": "4112 Kerry Way Santa Fe Springs, CA",
        "workingSector": "logistic",
        "birthDate": "09/20/1956",
        "roles": [
            {
                "id": 4,
                "name": "ROLE_MANAGER"
            },
            {
                "id": 3,
                "name": "ROLE_CUSTOMER_SERVICE"
            }
        ],
        "enabled": true
    }
]
```
# --------------------------------------------------
##### Request
#
```sh
GET /api/admin/auth/all
```
##### Response
#
```json
[
    {
        "id": 1,
        "username": "admin",
        "password": "$2a$10$6IgQ.mYGSCh4HrnGbi8OXO.Y03Vs1X6fp3oQWRr2CWRQHn1hO6IKm",
        "email": "admin@gmail.com",
        "fullName": "Samuel Donahue",
        "phoneNumber": "(530) 201-1175",
        "ssn": "618-74-2643",
        "drivingLicense": "T1822042",
        "country": "USA",
        "state": "California",
        "address": "4112 Kerry Way Santa Fe Springs, CA",
        "workingSector": "it",
        "birthDate": "09/21/1956",
        "roles": [
            {
                "id": 2,
                "name": "ROLE_ADMIN"
            }
        ],
        "enabled": true
    },
    {
        "id": 18,
        "username": "gregory",
        "password": "$2a$10$lY7QP0ieKjJwNOdchpU1luoirXY8g4juZmr1dzQcud0uZG2DjdH8S",
        "email": "gregory@gmail.com",
        "fullName": "Gregory K Kirchner",
        "phoneNumber": "(918) 926-1504",
        "ssn": "446-66-7249",
        "drivingLicense": "467632068",
        "country": "USA",
        "state": "Oklahoma",
        "address": "1350 Bridge Street, Tulsa, OK",
        "workingSector": "IT",
        "birthDate": "06/08/1958",
        "roles": [
            {
                "id": 1,
                "name": "ROLE_CUSTOMER"
            }
        ],
        "enabled": null
    }
]
```
# --------------------------------------------------
##### Request
#
```sh
POST /api/admin/auth/add
```
##### Body
#
```json
{   
    "username": "tiffany",
    "password": "test4",
    "email": "tiffany@gmail.com",
    "fullName": "Tiffany S Johnson",
    "phoneNumber": "(908) 977-4194",
    "ssn": "149-48-4262",
    "drivingLicense": "R4582 60598 82476",
    "country": "USA",
    "state": "New Jersey",
    "address": "4049 Fairfax Drive, New Brunswick, NJ",
    "workingSector": "logistic",
    "birthDate": "11/10/1974",
    "role": ["Manager"],
    "enabled": true
}
```
##### Response
#
```json
{
    "User registered successfully!": true
}
```
# --------------------------------------------------
##### Request
#
```sh
PUT /api/admin/{id}/auth
```
##### Body
#
```json
{   
    "username": "tiffany",
    "password": "test4",
    "email": "tiffany@gmail.com",
    "fullName": "Tiffany S Johnson",
    "phoneNumber": "(908) 977-4194",
    "ssn": "149-48-4262",
    "drivingLicense": "R4582 60598 82476",
    "country": "USA",
    "state": "New Jersey",
    "address": "4049 Fairfax Drive, New Brunswick, NJ",
    "workingSector": "logistic",
    "birthDate": "11/10/1974",
    "role": ["Manager", "CustomerService"],
    "enabled": true
}
```
##### Response
#
```json
{
    "success": true
}
```
# --------------------------------------------------
##### Request
#
```sh
DELETE /api/admin/{id}/auth
```
##### Response
#
```json
{
    "success": true
}
```

