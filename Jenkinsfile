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
       script{
                 withGradle() {
                    if(isUnix()){
                    sh "./gradlew clean assembleDebug assemble${params.Environment}"
}else{

bat "./gradlew clean assembleDebug assemble"+params.Environment
}
    }           }
}
}
stage("ArchiveBuild"){
steps{
archiveArtifacts artifacts:'app/build/outputs/apk/*/app-*.apk'
}
}
       }
}