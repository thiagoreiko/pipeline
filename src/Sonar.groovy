package org.foo

class Sonar {
    def sqScannerMsBuildHome
    def body
    
    Sonar(body) {
        this.body = body
        //this.sqScannerMsBuildHome = body.tool 'SonarQube Scanner for MSBuild 3.0'
    }

    def begin(key, name, version, additionalParameter = null) {
        //body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" begin /k:${key} /n:${name} /v:${version} ${additionalParameter}"
        body.bat "\"${body.tool 'SonarQube Scanner for MSBuild 3.0'}\\MSBuild.SonarQube.Runner.exe\" begin /k:${key} /n:${name} /v:${version}"
    }

    def end() {
        //body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" end"
        body.bat "\"${body.tool 'SonarQube Scanner for MSBuild 3.0'}\\MSBuild.SonarQube.Runner.exe\" end"
    }

}