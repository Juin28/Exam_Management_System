
# Javadoc Documentation

## Introduction

This repository contains the **Javadoc** documentation for the **Exam Management System** project. The system is designed to provide an interface for students, teachers and managers to perform their respective tasks. The Javadoc provides a detailed explanation of the classes, methods, and packages that make up the application.

The documentation is intended for developers who want to understand the architecture of the system.

---

## Table of Contents

1. [How to Access the Documentation](#how-to-access-the-documentation)
2. [Javadoc Features](#javadoc-features)
---

## How to Access the Documentation

1. **Locate the Files:**
    - The Javadoc files are located in the `javadoc` directory of this repository.

2. **Open the Documentation:**
    - Navigate to the `javadoc` folder.
    - Open the `index.html` file in your web browser.

3. **Browse the Structure:**
    - Explore packages, classes, and methods using the navigation links.

---

## Javadoc Features

The Javadoc includes:

- **Directory for Model:**
  - This can be accessed through http://localhost:63342/COMP3111_Project/javadoc/comp3111.examsystem/comp3111/examsystem/model/package-summary.html
  - Example: The `Course` class represents a course in the exam management system, including attributes such as 'courseID', 'courseName', 'department' and 'id'.

- **Directory for Controller:**
    - This can be accessed through http://localhost:63342/COMP3111_Project/javadoc/comp3111.examsystem/comp3111/examsystem/controller/package-summary.html
    - In the packages, the corresponding methods are documented with their parameters, return types, and exceptions.
    - Example: The `addCourse(javafx.event.ActionEvent event)` method in the `CourseManagementController` class allows adding a course into the course database.
    - Parameters, return types, and exceptions are fully documented.

- **Custom Tags:**
    - `@param` for method parameters 
    - `@return` for return values.
    - `@throws` for exception handling.

