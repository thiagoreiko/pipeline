package org.foo

import groovy.transform.BaseScript
@BaseScript Teste1 Teste1

static def testando(body) {
    body.println "$meaningOfLife" //works as expected
}
