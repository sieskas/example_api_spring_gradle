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