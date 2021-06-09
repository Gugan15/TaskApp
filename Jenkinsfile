pipeline {
    agent any
    stages{
       stage('GradleBuild') {
           if (isUnix()) {
               sh './gradlew clean assembleDebug'
           } else {
               bat 'gradlew.bat clean assembleDebug'
           }
       }
       }
}