package org.foo
import groovy.json.JsonSlurper

class Database implements Serializable {

    def script
    def jsonDb
    def scriptsFolderPath
    def classpath = "D:\\liquibase-bin\\ojdbc6.jar"
    def driverClassname = "oracle.jdbc.OracleDriver"

    Database(script, jsonDb, scriptsFolderPath = null, classpath = null, driverClassname = null){
        this.script = script
        this.jsonDb = new JsonSlurper().parseText(jsonDb)

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = "${script.WORKSPACE}\\DB\\" }  
    }    

    def validateScripts() {
        script.echo 'RUNNING VALIDATING SCRIPTS'   
        
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    script.sqlScriptValidator([
                        changeLogFile : "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                        url : "${db.ConnectionString}", 
                        classpath : "${classpath}", 
                        driverClassname : "${driverClassname}", 
                        credentialsId : "${sc.Credenciais}", 
                        sqlCommands : "drop,truncate", 
                        validateRollbackScript : false, 
                        buildFailedWhenInvalid : false
                    ])                
                }
            }
        }
    }
    
    @NonCPS
    def test(index){
        return {
            script.node {
                script.stage("Executando ${index}") {                            
                    script.echo "teste ${index}"
                }
            }
        }
    }

    def executeScripts() {
        script.echo "Executing scripts"
        def appliers = [:] 

        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    //appliers["DB_${db.Name}_SCHEMA_${sc.Schema}_${script.BUILD_NUMBER}"] = {
                    //    script.node {
                    //        script.stage("Executando scripts DB ${db.Name} SCHEMA ${sc.Schema}") {
                    //            script.echo 'a'
                    //        }
                    //    }
                    //}

                    appliers["0"] = test(0)
                    appliers["1"] = test(1)
                }
            }
        } 

        return appliers       
    }

    def testVariables() {
        script.echo 'Testing'   
        
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    script.echo "${sc.Credenciais}"
                }
            }
        }
    }
    
}