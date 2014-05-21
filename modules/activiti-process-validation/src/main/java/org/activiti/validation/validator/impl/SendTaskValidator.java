package org.activiti.validation.validator.impl;

import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Interface;
import org.activiti.bpmn.model.Operation;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SendTask;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.Problems;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jbarrez
 */
public class SendTaskValidator extends ExternalInvocationTaskValidator {

	@Override
	protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
		List<SendTask> sendTasks = process.findFlowElementsOfType(SendTask.class);
		for (SendTask sendTask : sendTasks) {
			
			// Verify implementation
			if (StringUtils.isEmpty(sendTask.getType())
					&& !ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(sendTask.getImplementationType())) {
				addError(errors, Problems.SEND_TASK_INVALID_IMPLEMENTATION, process, sendTask, "One of the attributes 'type' or 'operation' is mandatory on sendTask");
			}
			
			// Verify type
			if (StringUtils.isNotEmpty(sendTask.getType())) {
				
				if (!sendTask.getType().equalsIgnoreCase("mail")
					 && !sendTask.getType().equalsIgnoreCase("mule")
					 && !sendTask.getType().equalsIgnoreCase("camel")) {
				 addError(errors, Problems.SEND_TASK_INVALID_TYPE, process, sendTask, "Invalid or unsupported type for send task");
	      }
				
			  if (sendTask.getType().equalsIgnoreCase("mail")) {
		      validateFieldDeclarationsForEmail(process, sendTask, sendTask.getFieldExtensions(), errors);
		    }
		    
				
			}

			// Web service
			verifyWebservice(bpmnModel, process, sendTask, errors);
		}
	}
	
	protected void verifyWebservice(BpmnModel bpmnModel, Process process, SendTask sendTask, List<ValidationError> errors) {
	  if (ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(sendTask.getImplementationType()) && 
	        StringUtils.isNotEmpty(sendTask.getOperationRef())) {
	  	
	  	boolean operationFound = false;
	  	if (bpmnModel.getInterfaces() != null && bpmnModel.getInterfaces().size() > 0) {
	  		for (Interface bpmnInterface : bpmnModel.getInterfaces()) {
	  			if (bpmnInterface.getOperations() != null && bpmnInterface.getOperations().size() > 0) {
	  				for (Operation operation : bpmnInterface.getOperations()) {
	  					if (operation.getId() != null && operation.getId().equals(sendTask.getOperationRef())) {
	  						operationFound = true;
	  					}
	  				}
	  			}
	  		}
	  	}
	  	
	  	if (!operationFound) {
	  		addError(errors, Problems.SEND_TASK_WEBSERVICE_INVALID_OPERATION_REF, process, sendTask, "Invalid operation reference for send task");
	  	}
	  	
	  }
  }


}
