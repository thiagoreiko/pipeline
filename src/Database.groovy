package org.foo

import groovy.json.JsonSlurper
import com.cloudbees.groovy.cps.NonCPS

class Database implements Serializable {
  def script
  def jsonDb
  def scriptsFolderPath
  def classpath = "D:\\liquibase-bin\\ojdbc6.jar"
  def driverClassname = "oracle.jdbc.OracleDriver"

  Database(script, jsonDb, scriptsFolderPath = null, classpath = null, driverClassname = null) {
      this.script = script
      this.jsonDb = jsonDb

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = "${script.WORKSPACE}\\DB\\" }
    }
  
  def validateScripts(credentialsId) {
    script.echo 'RUNNING VALIDATING SCRIPTS'   
    
    for (db in jsonDb.Databases) {
        for (sc in db.Schemas) {
            if(sc.Aplicar) {
                script.sqlScriptValidator([
                    changeLogFile : "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                    url : "${db.ConnectionString}", 
                    classpath : "${classpath}", 
                    driverClassname : "${driverClassname}", 
                    credentialsId : "${credentialsId}", 
                    sqlCommands : "drop,truncate", 
                    validateRollbackScript : false, 
                    buildFailedWhenInvalid : false
                ])                
            }
        }
    }
  }

  def executeScripts(credentialId_update, credentialId_dbDoc){
      def appliers = [:]

      for (db in jsonDb.Databases) {
        for (sc in db.Schemas) {
            if(sc.Aplicar) {
                appliers["DB_${db.Name}_SCHEMA_${sc.Schema}_${BUILD_NUMBER}"] = {
                    node {
                        stage("Executando scripts DB ${db.Name} SCHEMA ${sc.Schema}") {
                            //executa script
                             script.liquibaseupdate 
                                 changelogfile: "${scriptsfolderpath}\\${sc.changelogpath}", 
                                 classpath: "${classpath}", 
                                 credentialsid: "${credentialid_update}", 
                                 driverclassname: "${driverclassname}", 
                                 tagonsuccessfulbuild: true, 
                                 testrollbacks: true, 
                                 url: "${db.connectionstring}"
                            
                            //grava dblog
                             script.liquibasedbdoc 
                                 changelogfile: "${scriptsfolderpath}\\${sc.changelogpath}", 
                                 classpath: "${classpath}", 
                                 credentialsid: "${credentialid_dbdoc}", 
                                 driverclassname: "${driverclassname}", 
                                 outputdirectory: ".\\dbdoc\\${db.name}\\${sc.schema}", 
                                 url: "${db.connectionstring}"
                        }
                    }
                }
            }
        }
    }
    
    //executa scripts paralelamente
    parallel appliers
  }
}