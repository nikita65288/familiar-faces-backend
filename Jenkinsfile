pipeline {
    agent any

    environment {
        GHCR_USER = 'nikita65288'
        GHCR_CREDENTIALS_ID = 'github-ghcr-token'

        SERVICES = 'chat-service auth-service user-service media-service gateway-service'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${GHCR_CREDENTIALS_ID}",
                    passwordVariable: 'GH_TOKEN',
                    usernameVariable: 'GH_USER'
                )]) {
                    sh 'echo $GH_TOKEN | docker login ghcr.io -u $GH_USER --password-stdin'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    def servicesList = SERVICES.split(' ')

                    for (int i = 0; i < servicesList.size(); i++) {
                        def serviceName = servicesList[i]
                        def imageName = "ghcr.io/${GHCR_USER}/${serviceName}:latest"

                        echo "Building and pushing ${serviceName}..."

                        dir("${serviceName}") {
                            sh "docker build -t ${imageName} ."
                            sh "docker push ${imageName}"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            sh 'docker system prune -f'
            sh 'docker logout ghcr.io'
        }
    }
}