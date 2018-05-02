package org.foo
/*class PipelineUtilities implements Serializable {
  def steps
  PipelineUtilities(steps) {this.steps = steps}
  def mvn(args) {
    //steps.sh "${steps.tool 'Maven'}/bin/mvn -o ${args}"
    steps.echo 'dkskdskjjsdsdjjsdkjdskkjdjdsjksdjdkjsda'
  }
}
*/

import hudson.slaves.EnvironmentVariablesNodeProperty
import jenkins.model.Jenkins
import hudson.model.*

class PipelineUtilities {

  static def saveGlobalVars(script, key, value) {
    script.instance = Jenkins.getInstance()
    script.globalNodeProperties = script.instance.getGlobalNodeProperties()
    script.envVarsNodePropertyList = script.globalNodeProperties.getAll(EnvironmentVariablesNodeProperty.class)

    script.newEnvVarsNodeProperty = null
    script.envVars = null

    if ( script.envVarsNodePropertyList == null || script.envVarsNodePropertyList.size() == 0 ) {
      script.newEnvVarsNodeProperty = new EnvironmentVariablesNodeProperty();
      script.globalNodeProperties.add(script.newEnvVarsNodeProperty)
      script.envVars = script.newEnvVarsNodeProperty.getEnvVars()
    } else {
      script.envVars = script.envVarsNodePropertyList.get(0).getEnvVars()
    }

    script.envVars.put(key,value)
    script.instance.save()
  }
 
  static def removeReadOnly(script, workspace = null) {
    if (workspace == null){
      workspace = script.WORKSPACE
    }
    script.bat '"${script.RemoveReadOnly}" "${workspace}"'
  }
}