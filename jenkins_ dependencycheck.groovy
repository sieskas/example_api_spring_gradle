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
        stage('Dependencycheck update') {
            steps {
                bat './gradlew dependencyCheckUpdate'
            }
        }
        stage('Dependencycheck analyse') {
            steps {
                bat './gradlew dependencyCheckAnalyze'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv(installationName: 'sonar') {
                    bat './gradlew sonarqube'
                }
            }
        }
    }
}