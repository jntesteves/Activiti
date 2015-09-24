package org.activiti.engine.delegate.event.impl;

import org.activiti.engine.EngineServices;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.history.*;

/**
 * @author Mike Dias
 */
public class ActivitiHistoricEntityEvent implements ActivitiEntityEvent {

  private Object historicEntity;
  private ActivitiEventType eventType;
  private String executionId;
  private String processInstanceId;
  private String processDefinitionId;

  public ActivitiHistoricEntityEvent(ActivitiEventType eventType, Object historicEntity) {
    this.eventType = eventType;
    this.historicEntity = historicEntity;
  }

  public ActivitiHistoricEntityEvent(ActivitiEventType eventType, HistoricProcessInstance historicProcInst) {
    this(eventType, (Object) historicProcInst);
    this.executionId = historicProcInst.getId();
    this.processInstanceId = historicProcInst.getId();
    this.processDefinitionId = historicProcInst.getProcessDefinitionId();
  }

  public ActivitiHistoricEntityEvent(ActivitiEventType eventType, HistoricTaskInstance historicTask) {
    this(eventType, (Object) historicTask);
    this.executionId = historicTask.getExecutionId();
    this.processInstanceId = historicTask.getProcessInstanceId();
    this.processDefinitionId = historicTask.getProcessDefinitionId();
  }

  public ActivitiHistoricEntityEvent(ActivitiEventType eventType, HistoricActivityInstance historicActivity) {
    this(eventType, (Object) historicActivity);
    this.executionId = historicActivity.getExecutionId();
    this.processInstanceId = historicActivity.getProcessInstanceId();
    this.processDefinitionId = historicActivity.getProcessDefinitionId();
  }

  public ActivitiHistoricEntityEvent(ActivitiEventType eventType, HistoricVariableInstance historicVar) {
    this(eventType, (Object) historicVar);
    this.processInstanceId = historicVar.getProcessInstanceId();
  }

  public ActivitiHistoricEntityEvent(ActivitiEventType eventType, HistoricIdentityLink historicIdentityLink) {
    this(eventType, (Object) historicIdentityLink);
    this.processInstanceId = historicIdentityLink.getProcessInstanceId();
  }

  @Override
  public Object getEntity() {
    return historicEntity;
  }

  @Override
  public ActivitiEventType getType() {
    return eventType;
  }

  @Override
  public String getExecutionId() {
    return executionId;
  }

  @Override
  public String getProcessInstanceId() {
    return processInstanceId;
  }

  @Override
  public String getProcessDefinitionId() {
    return processDefinitionId;
  }

  @Override
  public EngineServices getEngineServices() {
    return null;
  }
}
