# PatchForge 🛠️

**PatchForge** is a lightweight, web-based utility designed to streamline the creation of UAT (User Acceptance Testing) patches. It allows developers to quickly package specific `.class` files from a `.war` archive, along with SQL scripts and deployment guides, into a single, ready-to-deploy `.zip` file.

Built with **Spring Boot** and styled with a sleek **GitHub Dark Theme** UI.

## ✨ Features

- **Targeted Extraction:** Upload a `.war` file and extract only the specific classes you need by pasting their source paths.
- **Automated Packaging:** Automatically generates the correct folder structure and zips it up for easy deployment.
- **Documentation Included:** Optionally attach SQL queries (`queries.sql`) and deployment notes (`Guide.txt`) directly into the patch.
- **Dynamic Configuration:** Filenames for SQL/Guides and the application logo can be configured via `application.properties`.
- **Modern UI:** Fully responsive, dark-themed interface with fixed, scrollable forms for a seamless user experience.
- **IP-Based Security (Optional):** Can be locked down to specific internal IPs to prevent unauthorized access.
- **Audit Trail (Optional):** Tracks who built which patch and when.

## 🛠️ Technology Stack

- **Backend:** Java 17+, Spring Boot 3.x
- **Frontend:** HTML5, CSS3 (custom GitHub Dark theme), Thymeleaf
- **Build Tool:** Maven
- **Utilities:** Spring Boot Actuator (for auditing), Spring Security (for IP whitelisting)

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- A modern web browser

### Installation & Running Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/asharalishafqat99-coder/patch-forge.git
   cd patch-forge
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Open in your browser:**
   http://localhost:8080/patchforge/

   
## 👨‍💻 Developers

- **Lead Developer:** Ashar Ali Shafqat — [GitHub](https://github.com/asharalishafqat99-coder) • [Email](mailto:asharalishafqat99@gmail.com)