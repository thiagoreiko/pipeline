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
class PipelineUtilities {
	// static def go(script, args) {
	// 	script.echo 'testing static method'
	// }

  static def executeSonar(body, sourceCodePath, projectKey, projectName){
    def sqScannerMsBuildHome = body.tool 'SonarQube Scanner for MSBuild 3.0'
    body.withSonarQubeEnv {
      body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" begin /k:${projectKey} /n:${projectName} /v:1.0"
      body.bat "\"${body.tool 'MSBuild 15'}\\msbuild.exe\" \"${sourceCodePath}\\.jenkins\\ci.build.task.xml\" /t:CompileSolution /nologo /v:diag /m /nr:false /p:Configuration=Release"
      body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" end"
    }
  }
}