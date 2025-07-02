# TuanDoan Travel Tour Website

Welcome to **TuanDoan Travel Tour Website** – a modern web application for booking and managing travel tours. This project is designed to provide users with a seamless experience in searching, booking, and managing tours, while also offering powerful admin features for tour operators.

---

## 🌟 Features

- **User Registration & Login:** Secure authentication for users and admins.
- **Tour Browsing:** Explore a variety of tours with detailed information.
- **Tour Booking:** Book tours online and manage your booking history.
- **User Profile:** Update personal information and view booking records.
- **Admin Dashboard:** Manage tours, users, and bookings with advanced tools.
- **Responsive UI:** Optimized for both desktop and mobile devices.
- **Email Notifications:** Receive booking confirmations and updates via email.

---

## 📁 Project Structure

```
TuanDoan_travel_tour_website/
├── HELP.md
├── local.env
├── mvnw, mvnw.cmd
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/javaadvancedg9/
│   │   │       └── .... 
│   │   └── resources/
│   │       ├── *.html
│   │       ├── application.properties
│   │       ├── application.yaml
│   │       ├── data.json
│   │       ├── assets/
│   │       ├── public/
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/javaadvancedg9/
└── target/
    └── (build output)
```

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone -b dev https://github.com/GVOne-blood/TuanDoan_travel_tour_website.git
cd TuanDoan_travel_tour_website
```

### 2. Prerequisites

- **Java:** JDK 17 or newer (recommended)
- **Maven:** 3.6+
- **MySQL:** 8.x (or compatible)

### 3. Database Configuration

- Create a new MySQL database (e.g., `tuan_doan_tour`).
- Import the provided SQL file:
    - Check for `src/main/resources/data.sql` or `src/main/resources/data.json` for initial data.
- Update your database credentials in `src/main/resources/application.properties` or `application.yaml`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tuan_doan_tour
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

### 4. Build and Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

- The website will be available at: [http://localhost:8080](http://localhost:8080)

---

## 🛠️ Additional Notes

- **Frontend:** HTML, CSS, JS files are in `src/main/resources/`.
- **Backend:** Java Spring Boot code is in `src/main/java/com/javaadvancedg9/`.
- **Assets:** Images, CSS, JS, and other static files are in `assets/`, `public/`, and `static/` folders.
- **User & Admin Pages:** Located in `src/main/resources/` and `templates/`.

---

## 📬 Contact

For questions or contributions, please open an issue or contact the maintainer via GitHub.
**Email** [dta150904@gmail.com]
---

Enjoy exploring and contributing to **TuanDoan Travel Tour Website**!