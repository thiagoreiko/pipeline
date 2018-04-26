package org.foo

class Database implements Serializable {
  def script
  def jsonDb
  def classpath = "D:\\liquibase-bin\\ojdbc6.jar"
  def driverClassname = "oracle.jdbc.OracleDriver"

  Database(script, jsonDb) {
      this.script = script
      this.jsonDb = jsonDb
    }
  
  def validateScripts(credentialsId) {
    script.echo 'RUNNING VALIDATING SCRIPTS'   

    script.echo "${classpath}"
    script.echo "${driverClassname}"
  }
}