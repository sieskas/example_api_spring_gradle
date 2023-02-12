pipeline {
  agent any
  tools {
    gradle 'gradle'
  }
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  stages {

    stage('Git Checkout') {
      steps {
        //enable remote triggers
        script {
          properties([pipelineTriggers([pollSCM('''* * * * *''')])])
        }
        git credentialsId: 'Jenkins', url: 'https://github.com/sieskas/example_api_spring_gradle'
      }
    }
    stage('build') {
      steps {
        bat './gradlew build'
      }
    }
    stage('SonarQube analysis') {
      def scannerHome = tool 'sonar';
      withSonarQubeEnv('My SonarQube Server') { // If you have configured more than one global server connection, you can specify its name
        sh "${scannerHome}/bin/sonar-scanner"
      }
    }
  }
  post {
    always {
      junit '**/*.xml'
    }
  }
}