pipeline {
environment {

    PATH = "C:\\WINDOWS\\SYSTEM32"

}
 parameters{
           choice(name: 'Environment',choices: ['Cit','Sit','Release'],description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle() {
                 echo "${params.Environment}"
                   bat './gradlew clean assemble$params.Environment'
                 }
               }

}
stage("ArchiveBuild"){
steps{
archiveArtifacts artifacts:'app/build/outputs/apk/$params.Environment.toLowerCase()/app-$params.Environment.toLowerCase().apk'
}
}
       }
}