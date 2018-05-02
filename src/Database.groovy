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
        this.jsonDb = jsonDb //new JsonSlurper().parseText(jsonDb)

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = "${script.WORKSPACE}\\DB\\" }  
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

    def executeScripts(){
      def appliers = [:]      
      
      def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    appliers["DB_${db.Name}_SCHEMA_${sc.Schema}_${BUILD_NUMBER}"] = {
                        script.node {
                            script.stage("Executando scripts DB ${db.Name} SCHEMA ${sc.Schema}") {
                                //executa script
                                script.liquibaseUpdate 
                                    changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                                    classpath: "${classpath}", 
                                    credentialsId: "${sc.Credenciais}", 
                                    driverClassname: "${driverClassname}", 
                                    tagonsuccessfulbuild: true, 
                                    testrollbacks: true, 
                                    url: "${db.ConnectionString}"
                                
                                //grava dblog
                                script.liquibaseDbDoc 
                                    changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                                    classpath: "${classpath}", 
                                    credentialsId: "${sc.Credenciais}", 
                                    driverClassname: "${driverClassname}", 
                                    outputDirectory: ".\\dbdoc\\${db.Name}\\${sc.Schema}", 
                                    url: "${db.ConnectionString}"
                            }
                        }
                    }
                }
            }   
        }
    
      return appliers
    }

    def testVariables() {
        script.echo 'RUNNING VALIDATING SCRIPTS'   
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