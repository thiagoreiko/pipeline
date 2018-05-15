package org.foo
import groovy.json.JsonSlurperClassic


class Database implements Serializable {

    def body
    def jsonDb
    def scriptsFolderPath
    def classpath = "D:\\liquibase-bin\\ojdbc6.jar"
    def driverClassname = "oracle.jdbc.OracleDriver"
    def pipe
    Database(body, jsonDb, pipe, scriptsFolderPath = null, classpath = null, driverClassname = null){
        this.body = body
        this.jsonDb = jsonParse(jsonDb)
        this.pipe = pipe

        body.echo "${pipe}"

      if(classpath != null) { this.classpath = classpath }
      
      if(driverClassname != null) { this.driverClassname = driverClassname }

      if(scriptsFolderPath != null) { this.scriptsFolderPath = scriptsFolderPath }
      else { this.scriptsFolderPath = "${body.WORKSPACE}" }  
    }

    @NonCPS
    def jsonParse(def json) {
        new groovy.json.JsonSlurperClassic().parseText(json)
    }
    
    def validateScripts(sqlCommands = "drop,truncate", validateRollbackScript = false, buildFailedWhenInvalid = false) {
        
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {                    
                    //body.echo "Validating scripts DB "                          

                    body.sqlScriptValidator([
                        changeLogFile : "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                        url : "${db.ConnectionString}", 
                        classpath : "${classpath}", 
                        driverClassname : "${driverClassname}", 
                        credentialsId : "${sc.Credenciais.replace("UUID-", "")}", 
                        sqlCommands : sqlCommands, 
                        validateRollbackScript : validateRollbackScript, 
                        buildFailedWhenInvalid : buildFailedWhenInvalid
                    ])
                }
            }
        }        
    }

    def executeScripts(params) {
        def arr = [:]
        
        if(params.tagOnSuccessFulbuild == null){params.tagOnSuccessFulbuild = true}
        if(params.testRollbacks == null){params.testRollbacks = true}
        
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    arr["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
                        //body.echo "Executing scripts DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"

                        //execute script
                        body.liquibaseUpdate( 
                            changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                            classpath: "${classpath}", 
                            credentialsId: "${sc.Credenciais.replace("UUID-", "")}", 
                            driverClassname: "${driverClassname}", 
                            tagOnSuccessfulBuild: params.tagOnSuccessFulbuild, 
                            testRollbacks: params.testRollbacks, 
                            url: "${db.ConnectionString}")
                        
                        //save dblog
                        body.liquibaseDbDoc(
                            changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                            classpath: "${classpath}", 
                            credentialsId: "${sc.Credenciais.replace("UUID-", "")}", 
                            driverClassname: "${driverClassname}", 
                            outputDirectory: ".\\dbdoc\\${db.Name}\\${sc.Schema}", 
                            url: "${db.ConnectionString}")
                    }
                }
            }
        } 

        return arr
    }

    def publishReports() {
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    body.publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, 
										reportDir: "dbDoc\\${db.Name}\\${sc.Schema}", reportFiles: 'index.html', 
										reportName: 'dbDoc', reportTitles: 'dbDoc'])
                }
            }
        }
    }

    def saveIdLatestValidScriptLiquibase(environment){
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    //PipelineUtilities.saveGlobalVars(body, "${db.Name}_SCHEMA_${sc.Schema}_${environment}_LAST_STABLE", "${JOB_NAME}-${BUILD_NUMBER}")
                    //PipelineUtilities.saveGlobalVars(body, "${db.Name}_SCHEMA_${sc.Schema}_${environment}_LAST_STABLE", "${body.JOB_NAME}-${body.BUILD_NUMBER}")
                }
            }
        }
    }

    def rollbackToLatestStableVersion(environment){
        def fallbacks = [:]
        for (db in jsonDb.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    fallbacks["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
                        body.echo "Restoring scripts "
                        liquibaseRollback(
                        	changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                        	classpath: "${classpath}", 
                        	credentialsId: "${sc.Credenciais.replace("UUID-", "")}", 
                        	driverClassname: "${driverClassname}", 
                        	rollbackToTag: "${db.Name}_SCHEMA_${sc.Schema}_${environment}_LAST_STABLE",//"${JOB_NAME}-${BUILD_NUMBER}",
                        	url: "${db.ConnectionString}")
                    }
                }
            }
        } 

        return fallbacks
    }
}