package org.foo

class Database implements Serializable {

    def script
    def scriptsFolderPath
    def classpath = "D:\\liquibase-bin\\ojdbc6.jar"
    def driverClassname = "oracle.jdbc.OracleDriver"

    Database(script, scriptsFolderPath = null, classpath = null, driverClassname = null){
        this.script = script

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = "${script.WORKSPACE}\\DB\\" }  
    }

    def validateScripts(jsonDb, credentialsId) {
        script.echo 'RUNNING VALIDATING SCRIPTS'   
    
        // for (db in jsonDb.Databases) {
        //     for (sc in db.Schemas) {
        //         if(sc.Aplicar) {
        //             script.sqlScriptValidator([
        //                 changeLogFile : "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
        //                 url : "${db.ConnectionString}", 
        //                 classpath : "${classpath}", 
        //                 driverClassname : "${driverClassname}", 
        //                 credentialsId : "${credentialsId}", 
        //                 sqlCommands : "drop,truncate", 
        //                 validateRollbackScript : false, 
        //                 buildFailedWhenInvalid : false
        //             ])                
        //         }
        //     }
        // }
  }
}