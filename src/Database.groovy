package org.foo

class Database {
    static def testParallel(script, jsonDb){
        def appliersTest = [:]

        (1..5).each {
            appliersTest["${it}"] = {
                node {
                    stage("Executando ${it}") {                            
                        script.echo "${it}"
                    }
                }
            }
        }
        
        script.parallel appliersTest  
    }
}