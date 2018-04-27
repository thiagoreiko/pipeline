package org.foo

class Database {
    @NonCPS
    static def testParallel(script, jsonDb){
        def appliersTest = [:]

        (1..5).each {
            appliersTest["${it}"] = {
                script.node {
                    script.stage("Executando ${it}") {                            
                        script.echo "${it}"
                    }
                }
            }
        }

        script.parallel appliersTest  
    }
}