package org.foo

GroovyClassLoader gcl = new GroovyClassLoader();
File f = new File("Teste1.groovy");
Class tempClass = gcl.parseClass(f);
tempClass.configure();

