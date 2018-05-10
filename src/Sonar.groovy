package org.foo

class Sonar {
    def sqScannerMsBuildHome

    Sonar(body) {
        this.body = body
        this.sqScannerMsBuildHome = body.tool 'SonarQube Scanner for MSBuild 3.0'
    }

    def begin(key, name) {
        body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" begin /k:${key} /n:${name} /v:1.0"
    }

    def end() {
        body.bat "\"${sqScannerMsBuildHome}\\MSBuild.SonarQube.Runner.exe\" end"
    }    
}