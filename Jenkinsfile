pipeline {
 parameters{
           choice(name: 'Environment',choices: 'CIT\nSIT\nRELEASE',description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle(gradle:'gradle_4_2_1') {
                   sh './gradlew clean assembleDebug'
                 }
               }

}
       }
}