# 🚀 GitHub Workflow & Commit Guidelines

To maintain a clean history and stable production environment for the **EduSphere** project, all contributors must follow these Git and GitHub standards.

---

## 1. Branching Strategy

We follow a simplified **Feature Branch Workflow**.

* **`main`**: This branch contains the production-ready code. No one commits directly to `main`.
* **`dev`**: The integration branch for features. All PRs are merged here first.
* **Feature Branches**: Created for every new task, bug fix, or entity.
    * **Naming Convention**: `feature/short-description` or `bugfix/issue-number`
    * *Example*: `feature/add-research-project-entity`

---

## 2. Commit Message Standards

We use **Conventional Commits**. This allows us to automatically generate changelogs and makes the history easy to read.

### The Format:
`type(scope): description`

### Types:
* **`feat`**: A new feature (e.g., adding a new entity or service).
* **`fix`**: A bug fix.
* **`docs`**: Documentation changes only (README, ARCHITECTURE.md).
* **`style`**: Changes that do not affect the meaning of the code (white-space, formatting).
* **`refactor`**: A code change that neither fixes a bug nor adds a feature.
* **`test`**: Adding missing tests or correcting existing tests.
* **`chore`**: Updating build tasks, package manager configs, etc. (e.g., updating pom.xml).

### Examples:
* `feat(entity): add WorkLoad entity with LAZY fetching`
* `fix(compliance): resolve constructor conflict in ComplianceOfficer`
* `docs(architecture): update contributing guidelines for Instant usage`
* `chore(deps): update lombok version to 1.18.30`

---

## 3. The Pull Request (PR) Process



1.  **Sync**: Before starting, ensure your local `dev` is up to date:
    ```bash
    git checkout dev
    git pull origin dev
    ```
2.  **Branch**: Create your feature branch:
    ```bash
    git checkout -b feature/your-feature-name
    ```
3.  **Commit**: Make small, frequent commits following the standards in Section 2.
4.  **Push**: Push your branch to GitHub:
    ```bash
    git push origin feature/your-feature-name
    ```
5.  **Open PR**: Open a PR from your feature branch to `dev`.
6.  **Review**: At least one other developer must review the code.
7.  **Build**: Ensure the GitHub Actions (or local build) passes:
    ```bash
    ./mvnw clean compile
    ```
8.  **Merge**: Once approved and the build is green, use **"Squash and Merge"** to keep the `develop` history clean.

---

## 5. Working with the Maven Wrapper

To avoid environment issues, never commit your local `target/` folder or `.idea/` settings. These are handled by our `.gitignore`.

Always verify your changes before committing:
```powershell
# Windows
.\mvnw clean compile

# Mac/Linux
./mvnw clean compile