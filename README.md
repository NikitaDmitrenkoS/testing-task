# GitHub Repositories API

This project provides a REST API to fetch non-fork GitHub repositories for a given user, along with branch details and last commit information. It is implemented using **Spring WebFlux** for a fully reactive, non-blocking API.

## Features

- Fetch non-fork GitHub repositories for a user.
- For each repository, retrieve branch names and their last commit SHA.
- Proper error handling for non-existent users (404) and unsupported content types (406).

## Prerequisites

To run this application, you'll need:
- Java 17+
- Gradle 7.4+ (or use the Gradle wrapper `./gradlew`)
- Docker (if running the app in a container)
- AWS CLI (if deploying to AWS)

## Running the Application

You can run the application locally in two ways: using Gradle or Docker.

### Run Locally with Gradle

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-repo/github-repo-api.git
   cd github-repo-api
