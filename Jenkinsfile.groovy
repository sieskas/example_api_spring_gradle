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
                    // requires SonarQube Scanner 2.8+
                    scannerHome = tool 'sonar'
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