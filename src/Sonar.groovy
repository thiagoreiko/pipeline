package org.foo

class Sonar {    
    def body
    
    Sonar(body) {
        this.body = body     
    }

    def begin(key, name, version, additionalParameter = "") {
        body.bat "\"${body.tool 'SonarQube Scanner for MSBuild 3.0'}\\MSBuild.SonarQube.Runner.exe\" begin /k:${key} /n:${name} /v:${version} ${additionalParameter}"
    }

    def end() {        
        body.bat "\"${body.tool 'SonarQube Scanner for MSBuild 3.0'}\\MSBuild.SonarQube.Runner.exe\" end"
    }
}