package org.foo
import hudson.slaves.EnvironmentVariablesNodeProperty
import jenkins.model.Jenkins
import hudson.model.*

class PipelineUtilities {

  static def saveGlobalVars(script, key, value) {
    script.instance = Jenkins.getInstance()
    script.globalNodeProperties = script.instance.getGlobalNodeProperties()
    script.envVarsNodePropertyList = script.globalNodeProperties.getAll(EnvironmentVariablesNodeProperty.class)

    script.newEnvVarsNodeProperty = null
    script.envVars = null

    if ( script.envVarsNodePropertyList == null || script.envVarsNodePropertyList.size() == 0 ) {
      script.newEnvVarsNodeProperty = new EnvironmentVariablesNodeProperty();
      script.globalNodeProperties.add(script.newEnvVarsNodeProperty)
      script.envVars = script.newEnvVarsNodeProperty.getEnvVars()
    } else {
      script.envVars = script.envVarsNodePropertyList.get(0).getEnvVars()
    }

    script.envVars.put(key,value)
    script.instance.save()
  }
 
  static def removeReadOnly(script, workspace = null) {
    if (workspace == null){
      workspace = script.WORKSPACE    
    }
    
    script.bat "\"${script.RemoveReadOnly}\" \"${workspace}\""
  }

  static def sendPromotionNotification(script, to) {
    sendEmail(script, 'dxc-static-pl-promotion', to)
  }

  static def sendApprovalNotification(script, to) {
    sendEmail(script,'dxc-static-pl-approval', to )
  }

  static def sendAnalysisNotification(script, to) {
    sendEmail(script,'dxc-static-analysis', to )
  }

  static def sendEmail(script, template, to, postSendScript = null, preSendScript = null, replyTo = null, subject = null){
    
    if (postSendScript == null) { postSendScript = '$DEFAULT_POSTSEND_SCRIPT'}
    if (preSendScript == null) { preSendScript = '$DEFAULT_PRESEND_SCRIPT'}
    if (replyTo == null) { replyTo = '$DEFAULT_REPLYTO'}
    if (subject == null) { subject = '$DEFAULT_SUBJECT'}

    script.emailext( 
      body: '${JELLY_SCRIPT,template="' + template + '"}', 
      postsendScript: postSendScript, 
      presendScript: preSendScript, 
      replyTo: replyTo, 
      subject: subject, 
      to: to
    )
  }
}