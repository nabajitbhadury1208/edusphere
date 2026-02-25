# 🚀 GitHub Workflow & Commit Guidelines

To maintain a clean history and stable production environment for the **EduSphere** project, all contributors must follow these Git and GitHub standards.

---

## 1. Branching Strategy

We follow a simplified **Feature Branch Workflow**.

* **`main`**: This branch contains the production-ready code. No one commits directly to `main` unless you are a pro guy.
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

## 3. Pre-Development Workflow (Git Pull Before Coding)

Before starting any new work, always sync with the remote repository to prevent conflicts:

1. **Navigate to the repository**:
   ```powershell
   cd c:\Users\2480005\code_space\edusphere
   ```

2. **Switch to the target branch** (typically `main` or `dev`):
   ```bash
   git checkout main
   ```
   or
   ```bash
   git checkout dev
   ```

3. **Pull the latest changes**:
   ```bash
   git pull
   ```
   
   This fetches and merges the latest changes from the remote branch. If there are any conflicts at this stage, proceed to **Section 4: Merge Conflict Resolution**.

4. **Verify your working directory is clean**:
   ```bash
   git status
   ```
   
   Ensure the output shows "working tree clean" before proceeding.

5. **Create your feature branch**:
   ```bash
   git checkout -b feature/your-feature-name
   ```

---

## 4. Merge Conflict Resolution

Merge conflicts occur when Git cannot automatically merge changes. Here's how to resolve them:

### Detecting Conflicts
After running `git pull`, if there are conflicts, you'll see:
```
CONFLICT (content conflict): Merge conflict in <filename>
Automatic merge failed; fix conflicts and then commit the result.
```

Run this to see which files have conflicts:
```bash
git status
```

Files with conflicts will be marked as "both modified" or "both added".

### Resolving Conflicts Manually

1. **Open the conflicted file(s)** in VS Code:
   - Look for conflict markers:
     ```
     <<<<<<< HEAD
     Your local changes
     =======
     Remote changes
     >>>>>>> branch-name
     ```

2. **Decide which changes to keep**:
   - Keep only your changes (remove the remote changes)
   - Keep only the remote changes (remove your changes)
   - Combine both changes intelligently
   - Remove the conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`)

3. **Example - Resolving a Java File Conflict**:
   
   **Before** (conflicted):
   ```java
   <<<<<<< HEAD
   public class Student extends User {
       private String studentId;
   =======
   public class Student extends User {
       private String studentId;
       private String major;
   >>>>>>> feature/add-major-field
   ```
   
   **After** (resolved):
   ```java
   public class Student extends User {
       private String studentId;
       private String major;
   }
   ```

4. **Mark conflicts as resolved**:
   ```bash
   git add <filename>
   ```
   Or add all resolved files:
   ```bash
   git add .
   ```

5. **Complete the merge**:
   ```bash
   git commit -m "merge: resolve conflicts from main/dev"
   ```

6. **Verify the merge**:
   ```bash
   git log --oneline -n 5
   ```

### Tips for Avoiding Conflicts
- Pull frequently (at least daily)
- Keep feature branches focused and short-lived
- Communicate with team members about overlapping work
- Use feature branches instead of committing directly to `main` or `dev`

---

## 5. The Pull Request (PR) Process

1.  **Pre-coding Sync** (See Section 3):
    - Navigate to the repository
    - Switch to the target branch (`main` or `dev`)
    - Run `git pull`
    - Create your feature branch

2.  **Branch**: Create your feature branch:
    ```bash
    git checkout -b feature/your-feature-name
    ```

3.  **Commit**: Make small, frequent commits following the standards in Section 2.

4.  **Pre-push Sync** (See Section 6):
    - Before pushing, always pull again to check for new remote changes
    - Resolve any merge conflicts if they occur

5.  **Push**: Push your branch to GitHub:
    ```bash
    git push origin feature/your-feature-name
    ```

6.  **Open PR**: Open a PR from your feature branch to `dev`.

7.  **Review**: At least one other developer must review the code.

8.  **Build**: Ensure the GitHub Actions (or local build) passes:
    ```bash
    ./mvnw clean compile
    ```

9.  **Merge**: Once approved and the build is green, use **"Squash and Merge"** to keep the `develop` history clean.

---

## 6. Pre-Push Workflow (Git Pull Before Pushing)

Always pull from the remote before pushing to ensure you're working with the latest code:

1. **Commit your local changes**:
   ```bash
   git add .
   git commit -m "type(scope): description"
   ```

2. **Pull the latest changes from the remote**:
   ```bash
   git pull
   ```

3. **If there are merge conflicts** (See Section 4):
   - Resolve conflicts in affected files
   - Test your changes with: `./mvnw clean compile`
   - Stage resolved files: `git add .`
   - Complete the merge: `git commit -m "merge: resolve conflicts before push"`

4. **Verify the build passes**:
   ```bash
   ./mvnw clean compile
   ```

5. **Verify the merge is complete**:
   ```bash
   git status
   ```
   Output should show "nothing to commit, working tree clean"

6. **Push your branch**:
   ```bash
   git push origin feature/your-feature-name
   ```

7. **Handle push rejections** (if the remote has new changes):
   - Git will reject the push if there are conflicts
   - Repeat steps 2-6 if needed

---

## 7. Quick Reference Workflow

```powershell
# ===== BEFORE STARTING CODING =====
git checkout main
git pull
# Resolve conflicts if any (Section 4)
git checkout -b feature/my-feature

# ===== WRITE CODE =====
# Make your changes...

# ===== BEFORE PUSHING CODE =====
git add .
git commit -m "feat(entity): description"
git pull  # CRITICAL: Always pull before push
# Resolve conflicts if any (Section 4)
./mvnw clean compile  # Verify build
git push origin feature/my-feature

# ===== HANDLE MERGE CONFLICTS (If They Occur) =====
# 1. See git status to identify conflicted files
# 2. Open files in VS Code
# 3. Manually resolve conflicts (keep/remove sections)
# 4. git add <filename>
# 5. git commit -m "merge: resolve conflicts"
```

---

## 5. Working with the Maven Wrapper

To avoid environment issues, never commit your local `target/` folder or `.idea/` settings. These are handled by our `.gitignore`.

Always verify your changes before committing:
```powershell
# Windows
.\mvnw clean compile

# Mac/Linux
./mvnw clean compile
```

---

## 8. Working with the Maven Wrapper

To avoid environment issues, never commit your local `target/` folder or `.idea/` settings. These are handled by our `.gitignore`.

Always verify your changes before committing:
```powershell
# Windows
.\mvnw clean compile

# Mac/Linux
./mvnw clean compile
```

---

## 9. Troubleshooting Common Git Issues

### Issue: "Your branch is behind by X commits"
**Solution**: Run `git pull` to fetch and merge the latest changes.

### Issue: "fatal: Not a valid object name"
**Solution**: Ensure you're in the correct repository directory and the branch exists locally or remotely.

### Issue: "error: Your local changes to ... would be overwritten by merge"
**Solution**: Commit or stash your changes before pulling:
```bash
git stash                    # Temporarily save changes
git pull                     # Pull latest changes
git stash pop               # Restore your changes
```

### Issue: "Merge conflict in multiple files"
**Solution**: Refer to **Section 4: Merge Conflict Resolution** for detailed steps.

### Issue: "Push rejected - fetch first"
**Solution**: Always `git pull` before `git push` (see Section 6).

---

## 10. Best Practices Summary

✅ **Do**:
- Always pull before starting new work
- Always pull before pushing
- Resolve conflicts immediately when they occur
- Keep feature branches short-lived (1-3 days max)
- Write clear, conventional commit messages
- Test your code with `./mvnw clean compile` before pushing
- Communicate with teammates about overlapping work

❌ **Don't**:
- Commit directly to `main` or `dev`
- Push without pulling first
- Leave merge conflicts unresolved
- Make massive commits with unrelated changes
- Force push to `main` or `dev` (even with `-f` flag)
- Ignore build failures