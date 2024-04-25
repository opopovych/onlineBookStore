
#  Online Book Store
> The Online Book Store is a Java Spring Boot application.

## Used Technologies
**Core Technologies:**
* Java 17
* Maven

**Spring Framework:**
* Spring Boot
* Spring Boot Web
* Spring Data JPA
* Spring Boot Security
* Spring Boot Validation

**Database:**
* MySQL
* Hibernate
* Liquibase

**Testing:**
* Spring Boot Starter Test
* JUnit
* Mockito
* Docker Test Containers

**Auxiliary Libraries and tools:**
* Docker
* Lombok
* MapStruct
* Swagger
* JWT

## Endpoints
**AuthController:** Handles registration and login requests, supporting both Basic and JWT authentication.

    server.servlet.context-path = /api

* `POST: /auth/registration` - The endpoint for registration.
* `POST: /auth/login` - The endpoint for login.

**BookController:** Handles requests for book CRUD operations.
* `GET: /books` - The endpoint for retrieving all books.
* `GET: /books/{id}` - The endpoint for searching a specific book by ID.
* `POST: /books` - The endpoint for creating new book. (Admin Only)
* `PUT: /books/{id}` - The endpoint for updating book information. (Admin Only)
* `DELETE: /books/{id}` - The endpoint for deleting book. (Admin Only)

**CategoryController:** Handles requests for category CRUD operations and retrieving all books by category.
* `GET: /categories` - The endpoint for retrieving all categories.
* `GET: /categories/{id}` - The endpoint for retrieving a specific category by its ID.
* `GET: /categories/{id}/books` - The endpoint for retrieving books by a category ID.
* `POST: /categories` - The endpoint for creating a new category. (Admin Only)
* `PUT: /categories/{id}` - The endpoint for updating category information. (Admin Only)
* `DELETE: /categories/{id}` - The endpoint for deleting categories. (Admin Only)

**OrderController:** Handles requests for order CRUD operations.
* `GET: /orders` - The endpoint for retrieving orders history.
* `GET: /orders/{order-id}/items` - The endpoint for retrieving order items from a specific order.
* `GET: /orders/{order-id}/items/{item-id}` - The endpoint for retrieving a specific item from a specific order.
* `POST: /orders` - The endpoint for placing an order.
* `PATCH: /orders/{id}` - The endpoint for updating an order status. (Admin Only)

**ShoppingCartController:** Handles requests for shopping cart CRUD operations.
* `GET: /cart` - The endpoint for retrieving all items from a shopping cart.
* `POST: /cart` - The endpoint for adding item to a shopping cart.
* `PUT: /cart/cart-items/{cartItemId}` - The endpoint for updating quantity of a specific item in shopping cart.
* `DELETE: /cart/cart-items/{cartItemId}` - The endpoint for deleting items from a shopping cart.

## How to run Book Store API
* Install Docker
* Clone current project repository
* Add your ".env" file (see .env.example)
* Configure a ".env" file with necessary environment variables
* Run the command mvn clean package
* Use docker-compose up to run Docker container
