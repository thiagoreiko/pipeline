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