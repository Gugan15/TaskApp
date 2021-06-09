pipeline {
 parameters{
           choice(name: 'Environment',choices: 'CIT\nSIT\nRELEASE',description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
               bat 'gradlew.bat clean assembleDebug'
           }
}
       }
}