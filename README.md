# 🏋️ Iron Ledger

> **A Complete Workout Tracking Desktop Application built using Java Swing, JDBC, and MySQL**

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue?style=for-the-badge&logo=mysql)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-green?style=for-the-badge)
![Maven](https://img.shields.io/badge/Build-Maven-red?style=for-the-badge)

---

## 📌 Project Overview

**Iron Ledger** is a desktop-based fitness tracking system that allows users to:

- Track workouts
- Manage exercise categories
- Calculate BMI
- Analyze progress
- Export reports (PDF & Excel)

It is designed using a **layered architecture (UI → Service → DAO → DB)** to ensure scalability and maintainability.

---

---

## ✨ Features

### 🔐 Authentication
- User Registration
- Secure Login
- Session Management

### 🏋️ Workout Management
- Add / Update / Delete workouts
- Store sets, reps, weight, duration, date
- Category-based organization

### 🗂️ Category Management
- Add / Update / Delete exercise categories
- Dynamic dropdown integration

### ⚖️ BMI Calculator
- Real-time BMI calculation
- Category classification
- BMI history storage

### 🔎 Search & Filter
- Search by exercise name
- Filter by category
- Filter by date range

### 📊 Reports & Charts
- Category-wise workout analysis
- Duration trends
- Weight progression
- Custom Swing-based charts

### 📤 Export System
- Export to **PDF**
- Export to **Excel**

---

## 🧰 Tech Stack

| Layer | Technology |
|------|----------|
| UI | Java Swing |
| Logic | Java |
| Database | MySQL |
| Connectivity | JDBC |
| Build Tool | Maven |
| PDF | OpenPDF |
| Excel | Apache POI |

---

## 🏗️ System Architecture

mermaid<img width="1533" height="964" alt="mermaid-diagram" src="https://github.com/user-attachments/assets/1dfc8de7-9e44-470f-b5cb-4f7bc871c3b1" />

flowchart LR
    UI[Java Swing UI] --> SERVICE[Service Layer]
    SERVICE --> DAO[DAO Layer]
    DAO --> JDBC[JDBC Driver]
    JDBC --> DB[(MySQL Database)]



🗃️ Database Design
Tables
1. Users
user_id (PK)
name
username
password
email
2. Exercise Categories
category_id (PK)
category_name
description
3. Workouts
workout_id (PK)
user_id (FK)
category_id (FK)
exercise_name
sets, reps, weight, duration
workout_date
4. BMI Records
bmi_id (PK)
user_id (FK)
height, weight
bmi_value
bmi_category


<img width="1057" height="552" alt="mermaid-diagram (1)" src="https://github.com/user-attachments/assets/175375b9-711c-4c69-8ad3-c8fe1b4e4974" />
<img width="1363" height="900" alt="mermaid-diagram (2)" src="https://github.com/user-attachments/assets/653e51d6-9731-43a8-94ee-85c0936f59f2" />
🧠 Architecture Layers
Layer	Responsibility
UI	User Interface
Service	Business Logic
DAO	Database Operations
Model	Data Objects
Utils	Helpers

🚀 Future Enhancements
Cloud sync
Mobile app
AI workout suggestions
Diet planner
Dark mode
Password encryption
📌 Conclusion

Iron Ledger is a complete fitness tracking solution demonstrating:

Java Swing GUI design
JDBC + MySQL integration
Layered architecture
Data analytics & reporting
File export systems


---

## ✅ What you got
- Clean professional README
- Mermaid diagrams (architecture, workflow, ER, charts)
- Structured sections
- GitHub-ready formatting

---

If you want, I can next:
- Add **screenshots section**
- Add **demo GIF**
- Add **badges for GitHub stats**
- Or convert this into **project report PDF format**
