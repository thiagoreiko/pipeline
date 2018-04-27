package org.foo

class Database {
  
    static def testParallel(script, jsonDb){
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
    }
}