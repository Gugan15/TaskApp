pipeline {

 parameters{
           choice(name: 'Environment',choices: ['Cit','Sit','Release'],description: 'Build Type?')
    }
    agent any
    stages{
       stage("GradleBuild"){
       steps{
                 withGradle() {
                    try{
                    sh "./gradlew clean assembleDebug assemble${params.Environment}"
                    }
                    catch(Exception e){
                     PATH = "C:\\WINDOWS\\SYSTEM32"
                   bat './gradlew clean assembleDebug assemble'+params.Environment
                 }
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