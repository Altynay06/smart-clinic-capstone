# Architecture and technical workflow

Presentation: static role-selection page, Thymeleaf portal template rendered by Spring MVC, modular CSS and JavaScript.

Application: REST controllers validate request DTOs, services implement filtering and transactional booking, TokenService validates JWT signatures, expiry, and role. Passwords are BCrypt hashed and omitted from JSON. Tokens are sent in Authorization headers and kept in per-tab sessionStorage.

Data: JPA repositories manage MySQL entities. MongoRepository stores prescription documents. Neither database is exposed through a host port in compose.yaml.

Workflow: requirements and user stories -> schema -> entities/validation -> services/controllers -> portals -> tests -> Maven build -> Docker image -> GitHub Actions. Only synthetic records are used for demonstration.
