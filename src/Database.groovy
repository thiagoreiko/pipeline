package org.foo
import groovy.json.JsonSlurper

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

    def validateScripts() {
        body.echo 'RUNNING VALIDATING SCRIPTS'   
        
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    body.sqlScriptValidator([
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
    def fnc() {
        def arr = [:]
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    arr["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
                        body.echo "Executando scripts DB "                          
                    }
                }
            }
        } 

        return arr
    }

    //def test = { val -> body.echo "${val}"}

    def executeScripts() {
        body.echo "Executing scripts"
        
        def appliers = [:] 
        appliers = fnc()
        // for (int i = 0; i < 4; i++) { 
        //     def index = i
        //     appliers[index] = { test(index) }          
        // }

        return appliers       
    }
}