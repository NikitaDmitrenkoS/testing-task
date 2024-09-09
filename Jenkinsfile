pipeline {
    agent any

    environment {
        AWS_REGION = 'us-east-1'
        ECR_REPO_NAME = 'github-repo-api'
        IMAGE_TAG = "${env.BUILD_ID}"
        ECR_LOGIN = '$(aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com)'
        ECS_STACK_NAME = 'GitHubRepoApiStack'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-repo/github-repo-api.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${ECR_REPO_NAME}:${IMAGE_TAG} .'
                }
            }
        }

        stage('Push to ECR') {
            steps {
                script {
                    sh '''
                        ${ECR_LOGIN}
                        docker tag ${ECR_REPO_NAME}:${IMAGE_TAG} <account-id>.dkr.ecr.us-east-1.amazonaws.com/${ECR_REPO_NAME}:${IMAGE_TAG}
                        docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/${ECR_REPO_NAME}:${IMAGE_TAG}
                    '''
                }
            }
        }

        stage('Deploy to ECS') {
            steps {
                script {
                    sh '''
                        aws cloudformation deploy --template-file ecs-fargate.yml --stack-name ${ECS_STACK_NAME} \
                        --capabilities CAPABILITY_NAMED_IAM --parameter-overrides ImageUri=<account-id>.dkr.ecr.us-east-1.amazonaws.com/${ECR_REPO_NAME}:${IMAGE_TAG}
                    '''
                }
            }
        }

        stage('Deploy API Gateway') {
            steps {
                script {
                    sh 'aws cloudformation deploy --template-file api-gateway.yml --stack-name ApiGatewayStack'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
