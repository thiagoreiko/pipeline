package org.foo

class Sonar {
    
    static sqScannerMsBuildHome = 'test'

    static def begin(script) {
        script.echo "${sqScannerMsBuildHome} - Begin"    
    }

    static def end(script) {
        script.echo "${sqScannerMsBuildHome} - End"    
    }    
}