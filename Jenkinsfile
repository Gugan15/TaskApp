pipeline {
environment {

    PATH = "C:\\WINDOWS\\SYSTEM32"

}
 parameters{
           choice(name: 'Environment',choices: 'Cit\nSit\nRelease',description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle() {
                   bat './gradlew clean assemble${parameters.Environment}'
                 }
               }

}
stage("ArchiveBuild"){
steps{
archiveArtifacts artifacts:'app/build/outputs/apk/${params.Environment.toLowerCase()}/app-${params.Environment.toLowerCase()}.apk'
}
}
       }
}