pipeline {
 parameters{
           choice(name: 'Environment',choices: 'CIT\nSIT\nRELEASE',description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle() {
                   sh './gradlew clean assembleDebug'
                 }
               }

}
       }
}