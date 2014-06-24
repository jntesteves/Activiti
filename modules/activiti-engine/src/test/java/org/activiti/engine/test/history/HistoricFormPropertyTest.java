package org.activiti.engine.test.history;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.junit.Ignore;
import org.junit.Test;

public class HistoricFormPropertyTest {
  
  public static final String BPMN_FILE_PATH = "org/activiti/engine/test/history/HistoricFormPropertyTest.binaryProperty.bpmn20.xml";
  
  public class BinaryFormType extends AbstractFormType {
	  
	private static final long serialVersionUID = 1L;
	
    public String getName() {
      return "binary";
    }

    public Object convertFormValueToModelValue(String propertyValue) {
      return propertyValue.getBytes();
    }

    public String convertModelValueToFormValue(Object modelValue) {
      return new String((byte[])modelValue);
    }
    
  }
  
  public ProcessEngine buildProcessEngine(String historyLevel) {
    List<AbstractFormType> customFormTypes = new ArrayList<AbstractFormType>();
    customFormTypes.add(new BinaryFormType());
    
    StandaloneInMemProcessEngineConfiguration pec = new StandaloneInMemProcessEngineConfiguration();
    pec.setCustomFormTypes(customFormTypes);
    pec.setHistory(historyLevel);
    return pec.buildProcessEngine();
  }
  
  @Test @Ignore
  public void testBigStringFormData() {
    //ProcessEngine pe = buildProcessEngine(HistoryLevel.NONE.getKey()); // works
    ProcessEngine pe = buildProcessEngine(HistoryLevel.AUDIT.getKey()); // fail
    
    Deployment d = pe.getRepositoryService().createDeployment().addClasspathResource(BPMN_FILE_PATH).deploy();
    ProcessDefinition pd = pe.getRepositoryService().createProcessDefinitionQuery().deploymentId(d.getId()).singleResult();
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10000; i++) {
      sb.append("big String... ");
    }
    
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("binaryProperty", sb.toString());
    
    pe.getFormService().submitStartFormData(pd.getId(), properties);
    
    Task t = pe.getTaskService().createTaskQuery().singleResult();
    
    pe.getFormService().submitTaskFormData(t.getId(), properties);
    
    pe.close();
    
  }
  
}
