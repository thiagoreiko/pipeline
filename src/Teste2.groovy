package org.foo


static def testando(body) {
    def sh = new GroovyShell()  
    File f = new File("Teste1.groovy");  
    def closure = sh.evaluate(f.text);
}