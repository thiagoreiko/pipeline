package org.foo

class MSBuild {
    static def clean(script, fullProjectPath) {
        script.bat "\"${script.tool 'MSBuild 15'}\\msbuild.exe\" \"${fullProjectPath}\" /t:Clean /nologo /v:diag /m /nr:false /p:Configuration=Release"
    }

    static def rebuild(script, fullProjectPath) {
        script.bat "\"${script.tool 'MSBuild 15'}\\msbuild.exe\" \"${fullProjectPath}\" /t:Rebuild /nologo /v:diag /m /nr:false /p:Configuration=Release"
    }    
}