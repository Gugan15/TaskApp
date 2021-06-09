pipeline {
 parameters{
           choice(name: 'Environment',choices: 'CIT\nSIT\nRELEASE',description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
               node {
                 withGradle {
                   sh './gradlew build'
                 }
               }
           }
}
       }
}