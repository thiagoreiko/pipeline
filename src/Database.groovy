package org.foo

class Database implements Serializable {
  def script
  def jsonDb

  Database(script, jsonDb) {
      this.script = script
      this.jsonDb = jsonDb
    }
  
  def validateScripts() {
    script.echo 'RUNNING VALIDATING SCRIPTS'
    // script.echo "${jsonDb}"

    script.echo script.WORKSPACE
  }
}