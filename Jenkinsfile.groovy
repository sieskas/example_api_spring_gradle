pipeline {
    agent any
    tools {
        gradle 'gradle'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('build') {
            steps {
                bat './gradlew build'
            }
        }
        stage('SonarQube analysis') {
            steps {
                script {
                    scannerHome = tool 'sonarScanner'
                }
                withSonarQubeEnv('SonarQube Scanner') {
                    sh "${scannerHome}/bin/sonar-scanner"
                }
            }
        }
    }
    post {
        always {
            junit '**/*.xml'
        }
    }
}