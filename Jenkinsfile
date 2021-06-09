pipeline {
environment {

    PATH = "C:\\WINDOWS\\SYSTEM32"

}
 parameters{
           choice(name: 'Environment',choices: 'CIT\nSIT\nRELEASE',description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle() {
                   bat './gradlew clean assembleDebug'
                 }
               }

}
stage("ArchiveBuild"){
steps{
archiveArtifacts artifacts:'app-debug.apk'
}
}
       }
}