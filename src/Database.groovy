package org.foo

class Database {

    static def testParallel(script, jsonDb){

        script.echo "${jsonDb}"
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