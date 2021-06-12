pipeline {

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
 set env.PATH = "C:\\WINDOWS\\SYSTEM32"

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