# electronic-store-app
Full Project for electronic store including UI and service apps

IN PROGRESS...

Here's a complete `README.md` file template for your electronic store application:

---

# ElectroHomes - Electronic Store Application

ElectroHomes is a comprehensive electronic store application designed to offer a seamless shopping experience for users looking to purchase electronic products. The application features a Spring Boot backend and an Angular frontend.

## Features

- **Product Catalog:** Browse a variety of electronic products categorized for easy navigation.
- **User Authentication:** Secure user login and registration.
- **Shopping Cart:** Add, manage, and purchase products.
- **Order Management:** Track order history and status.
- **Responsive Design:** Optimized for both desktop and mobile platforms.

## Technologies Used

- **Backend:** Spring Boot, Java
- **Frontend:** Angular, HTML, CSS, TypeScript
- **Database:** MySQL
- **Authentication:** JSON Web Tokens (JWT)
- **Deployment:** [Your deployment platform, e.g., AWS, Heroku]

## Setup Instructions

### Prerequisites

- Java 11 or later
- Node.js and npm
- MySQL

### Backend Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/vijayvrx8055/electronic-store-app.git
   ```
2. **Navigate to the backend directory:**
   ```bash
   cd electronic-store-app/electrohomes-service-app
   ```
3. **Set up the MySQL database:**
   - Create a new MySQL database:
     ```sql
     CREATE DATABASE electronic_store;
     ```
   - Update `application.properties` with your database credentials.
4. **Run the Spring Boot application:**
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup

1. **Navigate to the frontend directory:**
   ```bash
   cd ../electrohomes-ui-app
   ```
2. **Install dependencies:**
   ```bash
   npm install
   ```
3. **Run the Angular application:**
   ```bash
   ng serve
   ```
4. **Access the application:**
   Open your browser and navigate to `http://localhost:4200`.

## API Endpoints

- **User Registration/Login:**
  - `POST /api/auth/register` - Register a new user.
  - `POST /api/auth/login` - Login a user.
- **Product Management:**
  - `GET /api/products` - Get all products.
  - `POST /api/products` - Add a new product.
- **Order Management:**
  - `POST /api/orders` - Create a new order.
  - `GET /api/orders/{userId}` - Get orders for a user.

## Database Details

- **Database:** MySQL
- **Tables:**
  - `users` - Stores user details.
  - `products` - Stores product information.
  - `orders` - Stores order details.
  - `order_items` - Stores details of items in an order.

## Contributing

Contributions are welcome! Follow these steps:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/YourFeature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/YourFeature`
5. Create a new Pull Request.

## License

This project is licensed under the GPL-3.0 License.

## Contact

For support or inquiries, contact [vijay.kumar.vrx@gmail.com](mailto:vijay.kumar.vrx@gmail.com).

---

Thank you for using ElectroHomes!
