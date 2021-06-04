pipeline{
    agent any
    stages{
        stage("Build Choice"){
        steps{
        script {
                            properties([
                                    parameters([
                                        [$class: 'ChoiceParameter',
                                            choiceType: 'PT_SINGLE_SELECT',
                                            description: 'Select the Environemnt from the Dropdown List',
                                            filterLength: 1,
                                            filterable: false,
                                            name: 'Env',
                                            script: [
                                                $class: 'GroovyScript',
                                                fallbackScript: [
                                                    classpath: [],
                                                    sandbox: false,
                                                    script:
                                                        "return['Could not get The environments']"
                                                ],
                                                script: [
                                                    classpath: [],
                                                    sandbox: false,
                                                    script:
                                                        "return['CIT','SIT']"
                                                ]
                                            ]
                                        ],
                                         script: [
                                                                                        classpath: [],
                                                                                        sandbox: false,
                                                                                        script: '''
                                                                                        if (Env.equals("CIT")){
                                                                                            clean assembleDebug assembleCit
                                                                                        }
                                                                                        else if(Env.equals("SIT")){
                                                                                            clean assembleDebug assembleSit
                                                                                        }

                                                                                        '''
                                                                                    ]
                                                                            ]
                                                                        ],
        }

        }
        }
        }
        }
    }
}