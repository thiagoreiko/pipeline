package org.foo

class Database {
  
    /*static def testParallel(script, jsonDb){
        def appliersTest = [:]

        (1..5).each {
            appliersTest["${it}"] = {
                script.node {
                    script.stage("Executando ${it}") {                            
                        script.echo "teste ${it}"
                    }
                }
            }
        }

        script.parallel appliersTest  
    }*/

    @NonCPS
    static def testParallel(script, jsonDb){
        def appliersTest = [:]

        appliersTest["0"] = {
            script.node {
                script.stage("Executando 0") {                            
                    script.echo "teste 0"
                }
            }
        }

        appliersTest["1"] = {
            script.node {
                script.stage("Executando 1") {                            
                    script.echo "teste 1"
                }
            }
        }

        return appliersTest  
    }
}