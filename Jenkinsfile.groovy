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
        stage('DependencyCheck') {
            steps {
                bat './gradlew dependencyCheckUpdate dependencyCheckAnalyze'
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
    post {
        always {
            junit '**/test-results/**/*.xml'
        }
    }
}