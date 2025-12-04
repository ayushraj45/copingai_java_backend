# Coping AI Backend – Spring Boot Microservice

Coping AI Backend is a production‑ready Java Spring Boot service that powers the Coping AI mobile app and website, handling personalized assessments, email delivery of results, streak‑based notifications, and user data orchestration.

***

## Core Responsibilities

- **Assessment engine** – Creates, stores, and evaluates personalized mental health and coping assessments, returning structured scores and recommendations to the client.
- **Email delivery of results** – Automatically composes and sends assessment results and summaries to users via email, including follow‑up guidance and next‑step suggestions.
- **Streak and reminder notifications** – Tracks journaling and assessment activity, sends timely notifications for streaks, missed check‑ins, and nudges to re‑engage.
- **API for mobile & web** – Exposes RESTful endpoints consumed by the React Native app and the Coping AI website.

***

## Tech Stack

- **Language**: Java
- **Framework**: Spring Boot
- **Build tool**: Maven (`pom.xml` configured for dependency management and environment‑specific builds)
- **Data & configuration**:
  - Standard Spring Boot `src/main/java` and `src/main/resources` layout
  - Properties managed via `application.properties` and environment variables (sensitive values removed from the repo for security)
- **Additional technologies**:
  - HTML templates and email formatting support

This stack demonstrates experience with modern Java backend development, Spring Boot, and production‑oriented configuration management.

***

## Architecture and Spring Features

The backend is structured using standard Spring Boot best practices to keep the codebase modular, testable, and maintainable.

- **Layered architecture**:
  - Controller layer exposing REST endpoints for assessments, notifications, and user operations
  - Service layer encapsulating business logic such as scoring, streak tracking, and email orchestration
  - Repository/data layer for persistence and retrieval of assessments, results, and user state
- **Spring features leveraged**:
  - Dependency injection via `@Service`, `@Component`, and `@Configuration`
  - REST APIs with `@RestController` and request/response DTOs
  - Scheduling support (for timed notifications and streak checks)
  - Validation and error‑handling using Spring annotations and exception handlers

This design highlights familiarity with Spring Boot conventions, separation of concerns, and clean backend architecture.

***

## Assessment Creation and Scoring

Coping AI Backend is responsible for the full lifecycle of user assessments.

- **Assessment definition**:
  - Configurable assessment structures (questions, options, weights) persisted in the backend
  - Support for multiple assessment types or categories to cover different coping or mood dimensions
- **Result computation**:
  - Business logic that aggregates user answers into meaningful scores or levels
  - Mapped outcomes (for example, “low”, “moderate”, “high”) which drive the recommendations and email content
- **API endpoints**:
  - Endpoints for creating assessments, submitting responses, and fetching results for the mobile app and web client

This showcases the ability to design and implement a custom domain model with non‑trivial business logic.

***

## Email Automation and Result Delivery

A major feature of this backend is automated, personalized email communication.

- **Transactional emails**:
  - Sends assessment summaries and detailed results directly to the user’s email after completion
  - Includes key scores, interpretation, and suggested coping strategies or follow‑up actions
- **Spring Mail integration**:
  - Uses Spring’s email capabilities to configure SMTP and send rich, structured emails
  - Supports HTML‑styled email bodies for clearer presentation of results
- **Template‑driven content**:
  - Email content generation designed to be driven by templates and assessment outcomes, making it easy to iterate on UX without changing core logic

This demonstrates end‑to‑end handling of asynchronous workflows and user communication.

***

## Notifications and Streak Tracking

To support habit formation and engagement, the backend manages streak‑based and time‑based notifications.

- **Streak tracking logic**:
  - Tracks user activity over time (journaling, assessments, check‑ins)
  - Computes streaks and detects when users have missed expected activity windows
- **Scheduled jobs**:
  - Uses Spring’s scheduling support to run periodic checks and trigger notifications at specific times of day
  - Supports configurable intervals for reminders and “you missed your check‑in” nudges
- **Notification delivery**:
  - Designed to integrate with mobile push notification providers or other messaging channels
  - Coordinates with the mobile app to ensure timely and context‑aware reminders

This part of the system highlights experience with background jobs, scheduling, and behavior‑driven engagement logic.

***

## Security and Configuration

The repository is structured with security and environment separation in mind.

- **Secure configuration**:
  - Sensitive configurations (database credentials, email provider keys, Firebase admin keys) removed from version control
  - `application.properties` and secret JSON files are managed via environment‑specific configuration and are referenced but not committed
- **Production readiness**:
  - Maven and Spring profiles prepared for separate dev, test, and production environments
  - Clean `.gitignore` ensuring build artifacts and secrets are excluded from the repository

This demonstrates an understanding of secure backend practices and deployment hygiene.

***

## Getting Started (Local Development)

1. **Clone the repository**  
   ```bash
   git clone https://github.com/ayushraj45/copingai_java_backend.git
   cd copingai_java_backend
   ```

2. **Configure environment**  
   - Create an `application.properties` (or `application-dev.properties`) in `src/main/resources` with:
     - Database connection details
     - Email (SMTP) configuration
     - Any API keys or integration credentials  
   - Ensure secret files such as Firebase admin credentials are added locally and referenced but not committed.

3. **Build and run**  
   ```bash
   ./mvnw spring-boot:run
   ```
   or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

4. **Access the API**  
   - By default, the application runs on `http://localhost:8080` with REST endpoints for assessments, results, and notifications.

***

## Developer Highlights

This backend showcases:

- Ability to design and implement a full Spring Boot service powering real mobile and web applications.
- Experience with domain‑driven assessment logic, personalized recommendations, and streak‑based engagement features.
- Hands‑on work with email automation, notification systems, background scheduling, and secure configuration management.
