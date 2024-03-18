# User and Product Management API Documentation


### URL: ``https://localhost:8443``
## USERS

## 1. Register User
- **Endpoint**: `POST /api/users/register`
- **Description**: Registers a new user.
- **Request Body**:
  ```json
  {
    "name": "string",
    "email": "string",
    "password": "string",
    "role": "USER/ADMIN"
  }
- **Example**:
  ```json
  {
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "john123",
    "role": "ADMIN"
  }

## 2. Login User
- **Endpoint**: `POST /api/users/login`
- **Description**: Logs in a user.
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
- **Example**:
  ```json
  {
    "username": "johndoe@example.com",
    "password": "john123"
  }
  
- **ADD THE AUTH TOKEN IN POSTMAN > Authorization > Bearer Token:** 
: Copy the auth token from the response and paste

## 3. Get User
- **Endpoint**: `GET /api/users/userDetails/{userId}`
- **Description**: Gets a user by ID. Only for Admin.
- **Request Parameters**:
  - `userId`: `string`

## 4. Get All Users
- **Endpoint**: `GET /api/users/allUsers`
- **Description**: Gets all users. Only for Admin.

## 5. Get Current User
- **Endpoint**: `GET /api/users/currentUser`
- **Description**: Gets the current user.

## 6. Update User
- **Endpoint**: `PUT /api/users/updateUser/{userId}`
- **Description**: Updates a user by ID. Only for Admin.
- **Request Parameters**:
  - `userId`: `string`
- **Request Body**:
  ```json
  {
    "name": "string",
    "email": "string",
    "password": "string",
    "role": "USER/ADMIN"
  }
- **Example**:
  ```json
  {
    "name": "John Doe",
    "email": "johndoe2@example.com",
    "password": "john1234",
    "role": "USER"
  }

## 7. Delete User
- **Endpoint**: `DELETE /api/users/deleteUser/{userId}`
- **Description**: Deletes a user by ID. Only for Admin.
- **Request Parameters**:
  - `userId`: `string`

## PRODUCTS

## 8. List All Products
- **Endpoint**: `GET /api/products/allProducts`
- **Description**: Lists all products.

## 9. Get Product
- **Endpoint**: `GET /api/products/productDetails/{id}`
- **Description**: Gets a product by ID.
- **Request Parameters**:
  - `id`: `string`

## 10. Get All Products By UserId
- **Endpoint**: `GET /api/products/allProducts/{userId}`
- **Description**: Gets all products by user ID.
- **Request Parameters**:
  - `userId`: `string`

## 11. Add Product
- **Endpoint**: `POST /api/products/storeProduct`
- **Description**: Adds a new product.
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "price": "number/double"
  }
- **Example**:
  ```json
  {
    "name": "iPhone 14",
    "description": "new iPhone 14",
    "price": 100.00
  }

## 12. Update Product
- **Endpoint**: `PUT /api/products/updateProduct/{id}`
- **Description**: Updates a product by ID.
- **Request Parameters**:
  - `id`: `string`
- **Request Body**:
  ```json
  {
    "name": "string",
    "description": "string",
    "price": "number/double"
  }
- **Example**:
  ```json
  {
    "name": "iPhone 16",
    "description": "new iPhone 16",
    "price": 120.00
  }

## 13. Delete Product
- **Endpoint**: `DELETE /api/products/deleteProduct/{id}`
- **Description**: Deletes a product by ID.
- **Request Parameters**:
  - `id`: `string`



