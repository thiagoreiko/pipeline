package org.foo

import DbUtils.*

def testando() {
    def dbUtils = new DbUtils()
    def something = 'foobar'
    dbUtils.save(something)
}