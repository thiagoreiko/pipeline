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

    static def testParallel(script, jsonDb){
        def appliersTest = [:]

        appliersTest["0"] = {
            node {
                stage("Executando 0") {                            
                    echo "teste 0"
                }
            }
        }

        appliersTest["1"] = {
            node {
                stage("Executando 1") {                            
                    echo "teste 1"
                }
            }
        }

        return appliersTest  
    }
}