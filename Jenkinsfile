pipeline {
 parameters{
           choice(name: 'Environment',choices: ['Cit','Sit','Release'],description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle() {
                 echo "${params.Environment}"
                   bat './gradlew clean assembleDebug

                 }
               }

}
stage("ArchiveBuild"){
steps{
archiveArtifacts artifacts:'app/build/outputs/apk/*/app-*.apk'
}
}
       }
}