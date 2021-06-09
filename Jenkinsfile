pipeline {
    agent any
       stage('Gradle Build') {
           if (isUnix()) {
               sh './gradlew clean assembleDebug'
           } else {
               bat 'gradlew.bat clean assembleDebug'
           }
       }
}