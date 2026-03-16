pipeline {
    agent any // Запускать на любом доступном узле Jenkins

    environment {
        // Укажи здесь свой логин на GitHub
        GHCR_USER = 'nikita65288'
        // Мы заранее должны будем добавить токен в Jenkins Credentials с этим ID
        GHCR_CREDENTIALS_ID = 'github-ghcr-token'

        // Список микросервисов, которые нужно упаковать в Docker
        SERVICES = 'chat-service auth-service user-service media-service gateway-service'
    }

    stages {
        stage('Checkout') {
            steps {
                // Jenkins сам скачает код из GitHub ветки
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                // Собираем весь проект целиком, пропуская тесты для скорости
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Login') {
            steps {
                // Авторизуемся в GitHub Registry перед пушем
                withCredentials([passwordVariable: 'GH_TOKEN', usernameVariable: 'GH_USER', credentialsId: "${GHCR_CREDENTIALS_ID}"]) {
                    sh 'echo $GH_TOKEN | docker login ghcr.io -u $GH_USER --password-stdin'
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    // Разбиваем строку с сервисами на массив и идем по каждому
                    def servicesList = SERVICES.split(' ')

                    for (int i = 0; i < servicesList.size(); i++) {
                        def serviceName = servicesList[i]
                        def imageName = "ghcr.io/${GHCR_USER}/${serviceName}:latest"

                        echo "Building and pushing ${serviceName}..."

                        // Заходим в папку сервиса, собираем и пушим образ
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
            // Очищаем локальные образы после сборки, чтобы не забивать диск сервера
            sh 'docker system prune -f'
            // Выходим из аккаунта
            sh 'docker logout ghcr.io'
        }
    }
}