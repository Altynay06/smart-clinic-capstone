# Smart Clinic Management System

Java Development Capstone Project based on the [official IBM template](https://github.com/ibm-developer-skills-network/okssu-java-database-capstone-template).

## Features
Admin, doctor, and patient portals; doctor search; authenticated booking and cancellation; private appointment lists; MongoDB prescriptions; MySQL reporting procedures; Docker and CI.

## Run
Copy .env.example to .env and replace every placeholder with a random value. JWT_SECRET must be at least 32 characters and DEMO_PASSWORD at least 12. Run docker compose up --build. Open http://localhost:8080.

Seeded educational accounts: admin (admin role), doctor1@clinic.example through doctor3@clinic.example (doctors), patient1@clinic.example through patient5@clinic.example (patients). Their password is the configured DEMO_PASSWORD. All names and records are synthetic. Disable APP_SEED for deployment without demo users.

## Tests
With Java 17 and Maven, run mvn -B verify inside app. Tests use H2 and a mocked prescription repository; they validate authentication, authorization, password hiding, validation, duplicate booking and MVC rendering. MySQL/MongoDB integration must additionally be checked against running containers.

## API
POST /admin/login, /doctor/login, /patient/login; GET /doctor?name=&specialty=&time=; POST /doctor (admin); POST /patient (registration); GET /patient/appointments (patient); GET /appointments (doctor); POST /appointments (patient); DELETE /appointments/{id} (owner); POST /prescriptions (assigned doctor); GET /prescriptions/{appointmentId} (assigned doctor or patient). Protected endpoints require Authorization: Bearer TOKEN.

Artifacts: schema-design.md, schema-architecture.md, user_stories.md, SQL/procedures.sql, Dockerfile, .github/workflows/ci.yml.

Submission status: implementation prepared; final grading submission is pending. Screenshots and SQL/curl outputs must come from an actual run.
