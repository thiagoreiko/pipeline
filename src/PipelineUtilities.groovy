package org.foo
import hudson.slaves.EnvironmentVariablesNodeProperty
import jenkins.model.Jenkins
import hudson.model.*

class PipelineUtilities {

  /*static def saveGlobalVars(script, key, value) {
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
  */

  static def saveGlobalVars(script, key, value) {
    def instance = Jenkins.getInstance()
    //script.instance = Jenkins.getInstance()
    def globalNodeProperties = instance.getGlobalNodeProperties()
    def envVarsNodePropertyList = globalNodeProperties.getAll(EnvironmentVariablesNodeProperty.class)

    script.newEnvVarsNodeProperty = null
    script.envVars = null

    if (envVarsNodePropertyList == null || envVarsNodePropertyList.size() == 0 ) {
      script.newEnvVarsNodeProperty = new EnvironmentVariablesNodeProperty();
      script.globalNodeProperties.add(script.newEnvVarsNodeProperty)
      script.envVars = script.newEnvVarsNodeProperty.getEnvVars()
    } else {
      script.envVars = envVarsNodePropertyList.get(0).getEnvVars()
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

  static def sendPromotionNotification(script, to = null) {
    sendEmail(script, 'dxc-static-pl-promotion', to)
  }

  static def sendApprovalNotification(script, to = null) {
    sendEmail(script,'dxc-static-pl-approval', to )
  }

  static def sendAnalysisNotification(script, to = null) {
    sendEmail(script,'dxc-static-analysis', to )
  }

  static def sendEmail(script, template, to = null, postSendScript = null, preSendScript = null, replyTo = null, subject = null){
    
    if (postSendScript == null) { postSendScript = '$DEFAULT_POSTSEND_SCRIPT'}
    if (preSendScript == null) { preSendScript = '$DEFAULT_PRESEND_SCRIPT'}
    if (replyTo == null) { replyTo = '$DEFAULT_REPLYTO'}
    if (subject == null) { subject = '$DEFAULT_SUBJECT'}
    if (to == null) { to = '$DEFAULT_RECIPIENTS'}

    script.emailext( 
      body: '${JELLY_SCRIPT,template="' + template + '"}', 
      postsendScript: postSendScript, 
      presendScript: preSendScript, 
      replyTo: replyTo, 
      subject: subject, 
      to: to
    )
  }

  static def executeApprovalFlow(script, time, submitter, approvalNotificationRecipient) {
    
    def dbaApproval

    script.timeout(time:time, unit:'HOURS') {
      dbaApproval = script.input message: 'Scripts de banco de dados autorizado?', ok: 'Continue', parameters: [script.choice(choices: 'SIM\nNÃO', description: 'Aprovado?', name: 'APROVADO'), script.text(defaultValue: '', description: 'Apenas em caso rejeição dos scripts', name: 'JUSTIFICATIVA')], submitter: "${submitter}", submitterParameter: 'APPROVER'
    }
    
    script.echo "Matricula do Aprovador : ${dbaApproval['APPROVER']}"
    script.echo "Aprovado : ${dbaApproval['APROVADO']}"
    script.echo "Justificativa (Obrigatório somente em caso de reprovação) : ${dbaApproval['JUSTIFICATIVA']}"
    
    if (dbaApproval['APROVADO'] == 'SIM') {
      script.env.APPROVAL_MAIL_TITLE = 'APPROVED'
    } else {
      script.env.APPROVAL_MAIL_TITLE = 'DISAPPROVED'
      script.env.REAPPROVAL_REASON = dbaApproval['JUSTIFICATIVA']
    }
        
    sendApprovalNotification(script, approvalNotificationRecipient)
    
    if (dbaApproval['APROVADO'] != 'SIM') {
      script.currentBuild.result = 'FAILURE'
    }

    return dbaApproval
    
  }
}