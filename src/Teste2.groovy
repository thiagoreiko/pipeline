package org.foo

import groovy.transform.BaseScript
@BaseScript Teste1 teste1

static def testando(body) {
    body.println "$meaningOfLife" //works as expected
}
