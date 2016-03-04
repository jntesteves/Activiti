package org.activiti.engine.impl.cmd;

import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntityManager;

import java.io.Serializable;

/**
 * @author Thiago Alves
 */
public class SetHistoricVariableCmd implements Command<Object>, Serializable {

  private static final long serialVersionUID = 1L;
  protected String procInstId;
  protected String variableName;
  protected Object variableValue;

  public SetHistoricVariableCmd(String procInstId, String variableName, Object variableValue) {
    this.procInstId = procInstId;
    this.variableName = variableName;
    this.variableValue = variableValue;
  }

  @Override
  public Object execute(CommandContext commandContext) {
    HistoricVariableInstanceEntityManager HistoricVariableInstanceEntityManager = commandContext
      .getHistoricVariableInstanceEntityManager();
    HistoricVariableInstanceEntity historicVariableInstance = HistoricVariableInstanceEntityManager
      .findHistoricVariableInstanceByProcessInstanceAndName(procInstId, variableName);

    historicVariableInstance.setValue(variableValue);

    HistoryManager historyManager = commandContext.getHistoryManager();
    historyManager.recordHistoricVariableUpdate(historicVariableInstance);

    return null;
  }
}
