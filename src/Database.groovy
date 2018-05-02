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
        this.jsonDb = jsonDb//new JsonSlurper().parseText(jsonDb)

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = 'a'}//"${script.WORKSPACE}\\DB\\" }  
    }    

    def validateScripts() {
        script.echo 'RUNNING VALIDATING SCRIPTS'   
        
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
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
    
    
    /*def test(index){
        return {script.echo "teste ${index}"}        
    }*/

    def test = { val -> script.echo "${val}"}

    def executeScripts(dtBase) {
        script.echo "Executing scripts"
        def appliers = [:] 

        def json = new JsonSlurper().parseText(dtBase)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                   def name = ${db.Name}
            /*         def schema = ${sc.Schema}
                    def buildNumber = ${script.Name}
                    //appliers["DB_${db.Name}_SCHEMA_${sc.Schema}_${script.BUILD_NUMBER}"] = {
                    //    script.node {
                    //        script.stage("Executando scripts DB ${db.Name} SCHEMA ${sc.Schema}") {
                    //            script.echo 'a'
                    //        }
                    //    }
                    //}
                    // appliers["DB_${name}_SCHEMA_${schema}_${buildNumber}"] = {
                    //     script.echo "Executando scripts DB ${name} SCHEMA ${schema}"                          
                    // }*/
                }
            }
        } 
        // for (int i = 0; i < 4; i++) { 
        //     def index = i
        //     appliers[index] = { test(index) }          
        // }

        return appliers       
    }

    def testVariables() {
        script.echo 'Testing'   
        
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    script.echo "${sc.Credenciais}"
                }
            }
        }
    }
    
}