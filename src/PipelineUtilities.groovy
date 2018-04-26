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

    if ( envVarsNodePropertyList == null || envVarsNodePropertyList.size() == 0 ) {
      newEnvVarsNodeProperty = new EnvironmentVariablesNodeProperty();
      globalNodeProperties.add(newEnvVarsNodeProperty)
      envVars = newEnvVarsNodeProperty.getEnvVars()
    } else {
      envVars = envVarsNodePropertyList.get(0).getEnvVars()
    }

    //envVars.put(variableName + '_' + deployEnv, buildNumer)
    envVars.put(key,value)
    instance.save()
  }
  // static def executeSonar(body, sourceCodePath, projectKey, projectName){
  //   def sqScannerMsBuildHome = body.tool 'SonarQube Scanner for MSBuild 3.0'
  //   body.withSonarQubeEnv {
  //     body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" begin /k:${projectKey} /n:${projectName} /v:1.0"
  //     body.bat "\"${body.tool 'MSBuild 15'}\\msbuild.exe\" \"${sourceCodePath}\\.jenkins\\ci.build.task.xml\" /t:CompileSolution /nologo /v:diag /m /nr:false /p:Configuration=Release"
  //     body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" end"
  //   }
  // }
}