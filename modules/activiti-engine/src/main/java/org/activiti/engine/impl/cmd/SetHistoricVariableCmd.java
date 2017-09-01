package org.activiti.engine.impl.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity;
import org.activiti.engine.impl.variable.VariableType;
import org.activiti.engine.impl.variable.VariableTypes;

import java.io.Serializable;

/**
 * @author Thiago Alves (iColabora)
 */
public class SetHistoricVariableCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String procInstId;
  protected String variableName;
  protected Object variableValue;
  protected String procDefId;

  public SetHistoricVariableCmd(String procInstId, String variableName, Object variableValue, String procDefId) {
    this.procInstId = procInstId;
    this.variableName = variableName;
    this.variableValue = variableValue;
    this.procDefId = procDefId;
  }

  @Override
  public Object execute(CommandContext commandContext) {
    HistoricVariableInstanceEntityManager HistoricVariableInstanceEntityManager = commandContext
      .getHistoricVariableInstanceEntityManager();
    HistoricVariableInstanceEntity historicVariableInstance = HistoricVariableInstanceEntityManager
      .findHistoricVariableInstanceByProcessInstanceAndName(procInstId, variableName);

    VariableTypes variableTypes = Context
        .getProcessEngineConfiguration()
        .getVariableTypes();

    VariableType type = variableTypes.findVariableType(variableValue);
    VariableInstanceEntity variableInstanceEntity = new VariableInstanceEntity().create(variableName,type,variableValue);
    variableInstanceEntity.setProcessInstanceId(procInstId);
    variableInstanceEntity.setProcessDefinitionId(procDefId);

    if (historicVariableInstance == null) {
      historicVariableInstance = HistoricVariableInstanceEntity.copyAndInsert(variableInstanceEntity);
    } else {
      historicVariableInstance.setValue(variableValue);
    }

    HistoricDetailVariableInstanceUpdateEntity.copyAndInsert(variableInstanceEntity);
    commandContext.getHistoryManager().recordHistoricVariableUpdate(historicVariableInstance);

    return null;
  }

}
