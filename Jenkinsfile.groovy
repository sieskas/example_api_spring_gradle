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
                bat './gradlew sonarqube'
            }
        }
    }
    post {
        always {
            junit '**/*.xml'
        }
    }
}