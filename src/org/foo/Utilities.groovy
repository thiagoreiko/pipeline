package org.foo

class Utilities implements Serializable {
  def steps
  Utilities(steps) {this.steps = steps}
  def mvn(args) {
    //steps.sh "${steps.tool 'Maven'}/bin/mvn -o ${args}"
	  steps.echo 'FUNFANDO'
  }
  
  def go() {
	  steps.echo 'GO'
  }
  
  static def test(script, args) {
	script.echo 'Teste de método estático'
  }
  
}