package org.foo
import groovy.json.JsonSlurper
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
/*
    @NonCPS
    def fncValidateScripts(sqlCommands = "drop,truncate", validateRollbackScript = false, buildFailedWhenInvalid = false) {
        def arr = [:]
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    arr["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
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

        return arr
    }

    @NonCPS
    def fncExecuteScripts() {
        def arr = [:]
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    arr["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
                        //body.echo "Executing scripts DB "                          

                        //execute script
                        body.liquibaseUpdate 
                            changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                            classpath: "${classpath}", 
                            credentialsId: "${sc.Credenciais.replace("UUID-", "")}", 
                            driverClassname: "${driverClassname}", 
                            tagonsuccessfulbuild: true, 
                            testrollbacks: true, 
                            url: "${db.ConnectionString}"
                        
                        //save dblog
                        body.liquibaseDbDoc 
                            changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                            classpath: "${classpath}", 
                            credentialsId: "${sc.Credenciais.replace("UUID-", "")}", 
                            driverClassname: "${driverClassname}", 
                            outputDirectory: ".\\dbdoc\\${db.Name}\\${sc.Schema}", 
                            url: "${db.ConnectionString}"
                    }
                }
            }
        } 

        return arr
    }    

    @NonCPS
    def fncPublishReports() {
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    body.publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, 
										reportDir: "dbDoc\\${db.Name}\\${sc.Schema}", reportFiles: 'index.html', 
										reportName: 'dbDoc', reportTitles: 'dbDoc'])
                }
            }
        }
    }

    @NonCPS
    def fncSaveIdLatestValidScriptLiquibase(environment){
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    PipelineUtilities.saveGlobalVars(body, "${db.Name}_SCHEMA_${sc.Schema}_${environment}_LAST_STABLE", "${JOB_NAME}-${BUILD_NUMBER}")
                }
            }
        }
    }

    @NonCPS
    def fncRollbackToLatestStableVersion(environment){
        def fallbacks = [:]
        def json = new JsonSlurper().parseText(jsonDb)
        for (db in json.Databases) {
            for (sc in db.Schemas) {
                if(sc.Aplicar) {
                    fallbacks["DB_${db.Name}_SCHEMA_${sc.Schema}_${body.BUILD_NUMBER}"] = {
                        body.echo "Restoring scripts "
                        liquibaseRollback 
                        	changeLogFile: "${scriptsFolderPath}\\${sc.ChangeLogPath}", 
                        	classpath: "${classpath}", 
                        	credentialsId: "${sc.Credenciais.replace("UUID-", "")}", 
                        	driverClassname: "${driverClassname}", 
                        	rollbackToTag: "${db.Name}_SCHEMA_${sc.Schema}_${environment}_LAST_STABLE",//"${JOB_NAME}-${BUILD_NUMBER}",
                        	url: "${db.ConnectionString}"
                    }
                }
            }
        } 

        return fallbacks
    }

    def validateScripts() {
        body.echo 'RUNNING VALIDATING SCRIPTS'
        fncValidateScripts()
    }

    def executeScripts() {
        body.echo "Executing scripts"
        
        def appliers = [:] 
        appliers = fncExecuteScripts()

        return appliers       
    }

    def publishReports(){
        body.echo 'PUBLISHING REPORTS'
        fncPublishReports()
    }

    def saveIdLatestValidScriptLiquibase(environment) {
        body.echo 'SAVING THE LATEST VALID SCRIPTS'
        fncSaveIdLatestValidScriptLiquibase(environment)
    }

    def rollbackToLatestStableVersion(environment) {
        body.echo 'RESTORING THE LATEST STABLE VERSION OF THE DATABASE'
        fncRollbackToLatestStableVersion(environment)
    }
    */
}