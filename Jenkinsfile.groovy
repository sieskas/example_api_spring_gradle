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
                bat './gradlew clean build'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar') {
                    bat './gradlew sonarqube -x dependencyCheckAnalyze'
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