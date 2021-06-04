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

                                            [$class: 'DynamicReferenceParameter',
                                                                               choiceType: 'ET_ORDERED_LIST',
                                                                               description: 'Select the  AMI based on the following infomration',
                                                                               name: 'Image Information',
                                                                               referencedParameters: 'Env',
                                                                               script:
                                                                                   [$class: 'GroovyScript',
                                                                                   script: 'return["Could not get AMi Information"]',
                                         script: [

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
                                                                        ]
                                                                        ])
        }

        }
        }
        }
        }
    }
}