package org.foo
import groovy.json.JsonSlurperClassic
import org.foo.PipelineUtilities.*

class Database implements Serializable {

    def body
    def jsonDb
    def scriptsFolderPath
    def classpath = "D:\\liquibase-bin\\ojdbc6.jar"
    def driverClassname = "oracle.jdbc.OracleDriver"

    Database(body, jsonDb, scriptsFolderPath = null, classpath = null, driverClassname = null){
        this.body = body
        this.jsonDb = jsonDb

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = "${body.WORKSPACE}\\DB\\" }  
    }

    @NonCPS
    def jsonParse(def json) {
        new groovy.json.JsonSlurperClassic().parseText(json)
    }
    
    def validateScripts(sqlCommands = "drop,truncate", validateRollbackScript = false, buildFailedWhenInvalid = false) {
        
        def json = jsonParse(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    arr["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
                        body.echo "Validating scripts DB "                          

                        /*body.sqlScriptValidator([
                            changeLogFile : "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                            url : "${db.ConnectionString}", 
                            classpath : "${classpath}", 
                            driverClassname : "${driverClassname}", 
                            credentialsId : "${sc.Credenciais.replace("UUID-", "")}", 
                            sqlCommands : sqlCommands, 
                            validateRollbackScript : validateRollbackScript, 
                            buildFailedWhenInvalid : buildFailedWhenInvalid
                        ])*/
                    }
                }
            }
        } 
    }
}