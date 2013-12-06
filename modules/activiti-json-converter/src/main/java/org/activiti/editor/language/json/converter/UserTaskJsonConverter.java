/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.editor.language.json.converter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.bpmn.model.UserTaskResource;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

/**
 * @author Tijs Rademakers
 */
public class UserTaskJsonConverter extends BaseBpmnJsonConverter {

  public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
      Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    
    fillJsonTypes(convertersToBpmnMap);
    fillBpmnTypes(convertersToJsonMap);
  }
  
  public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
    convertersToBpmnMap.put(STENCIL_TASK_USER, UserTaskJsonConverter.class);
  }
  
  public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    convertersToJsonMap.put(UserTask.class, UserTaskJsonConverter.class);
  }
  
  @Override
  protected String getStencilId(FlowElement flowElement) {
    return STENCIL_TASK_USER;
  }
  
  @Override
  protected void convertElementToJson(ObjectNode propertiesNode, FlowElement flowElement) {
    UserTask userTask = (UserTask) flowElement;
    String assignee = userTask.getAssignee();
    String candidateUsers = convertListToCommaSeparatedString(userTask.getCandidateUsers());
    String candidateGroups = convertListToCommaSeparatedString(userTask.getCandidateGroups());
    
    if (StringUtils.isNotEmpty(assignee) || StringUtils.isNotEmpty(candidateUsers) || StringUtils.isNotEmpty(candidateGroups)) {
      ObjectNode assignmentNode = objectMapper.createObjectNode();
      ArrayNode itemsNode = objectMapper.createArrayNode();
      
      if (StringUtils.isNotEmpty(assignee)) {
        ObjectNode assignmentItemNode = objectMapper.createObjectNode();
        assignmentItemNode.put(PROPERTY_USERTASK_ASSIGNMENT_TYPE, PROPERTY_USERTASK_ASSIGNEE);
        assignmentItemNode.put(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION, assignee);
        itemsNode.add(assignmentItemNode);
      }
      
      if (StringUtils.isNotEmpty(candidateUsers)) {
        ObjectNode assignmentItemNode = objectMapper.createObjectNode();
        assignmentItemNode.put(PROPERTY_USERTASK_ASSIGNMENT_TYPE, PROPERTY_USERTASK_CANDIDATE_USERS);
        assignmentItemNode.put(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION, candidateUsers);
        itemsNode.add(assignmentItemNode);
      }
      
      if (StringUtils.isNotEmpty(candidateGroups)) {
        ObjectNode assignmentItemNode = objectMapper.createObjectNode();
        assignmentItemNode.put(PROPERTY_USERTASK_ASSIGNMENT_TYPE, PROPERTY_USERTASK_CANDIDATE_GROUPS);
        assignmentItemNode.put(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION, candidateGroups);
        itemsNode.add(assignmentItemNode);
      }
      
      assignmentNode.put("totalCount", itemsNode.size());
      assignmentNode.put(EDITOR_PROPERTIES_GENERAL_ITEMS, itemsNode);
      propertiesNode.put(PROPERTY_USERTASK_ASSIGNMENT, assignmentNode);
    }
    
    if (userTask.getPriority() != null) {
      setPropertyValue(PROPERTY_PRIORITY, userTask.getPriority().toString(), propertiesNode);
    }
    if (userTask.getEstimatedDuration() != null) {
        setPropertyValue(PROPERTY_ESTIMATED_DURATION, userTask.getEstimatedDuration(), propertiesNode);
    }
    setPropertyValue(PROPERTY_FORMKEY, userTask.getFormKey(), propertiesNode);
    setPropertyValue(PROPERTY_DUEDATE, userTask.getDueDate(), propertiesNode);
    setPropertyValue(PROPERTY_CATEGORY, userTask.getCategory(), propertiesNode);;
    
    addFormProperties(userTask.getFormProperties(), propertiesNode);
    addUserTaskResources(userTask.getResources(), propertiesNode);
  }
  
  protected void addUserTaskResources(List<UserTaskResource> resources, ObjectNode propertiesNode) {
	  ObjectNode resourcesNode = objectMapper.createObjectNode();
	  ArrayNode itemsNode = objectMapper.createArrayNode();
	  for (UserTaskResource resource : resources) {
		  ObjectNode resourceItemNode = objectMapper.createObjectNode();
		  resourceItemNode.put(PROPERTY_USERTASK_RESOURCE_ID, resource.getId());
		  resourceItemNode.put(PROPERTY_USERTASK_RESOURCE_QUANTITY, resource.getQuantity());

		  itemsNode.add(resourceItemNode);
	  }

	  resourcesNode.put("totalCount", itemsNode.size());
	  resourcesNode.put(EDITOR_PROPERTIES_GENERAL_ITEMS, itemsNode);
	  propertiesNode.put("usertaskresource", resourcesNode);
  }
  
  protected void convertJsonToResources(JsonNode objectNode, BaseElement element) {

	  JsonNode resourcesNode = getProperty(PROPERTY_USERTASK_RESOURCE, objectNode);
	  if (resourcesNode != null) {
		  if (resourcesNode.isValueNode() && StringUtils.isNotEmpty(resourcesNode.asText())) {
			  try {
				  resourcesNode = objectMapper.readTree(resourcesNode.asText());
			  } catch (Exception e) {
				  LOGGER.info("Resources node can not be read", e);
			  }
		  }
		  JsonNode itemsArrayNode = resourcesNode.get(EDITOR_PROPERTIES_GENERAL_ITEMS);

		  if (itemsArrayNode != null) {
			  for (JsonNode resourceNode : itemsArrayNode) {
				  JsonNode resourceIdNode = resourceNode.get(PROPERTY_USERTASK_RESOURCE_ID);
				  if (resourceIdNode != null && StringUtils.isNotEmpty(resourceIdNode.asText())) {

					  UserTaskResource resource = new UserTaskResource();
					  resource.setId(resourceIdNode.asText());
					  resource.setQuantity(getValueAsString(PROPERTY_USERTASK_RESOURCE_QUANTITY, resourceNode));

					  if (element instanceof UserTask) {
						  ((UserTask) element).getResources().add(resource);
					  }
				  }
			  }
		  }
	  }
  }  
  
  @Override
  protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
    UserTask task = new UserTask();
    task.setPriority(getPropertyValueAsString(PROPERTY_PRIORITY, elementNode));
    task.setEstimatedDuration(getPropertyValueAsString(PROPERTY_ESTIMATED_DURATION, elementNode));
    task.setFormKey(getPropertyValueAsString(PROPERTY_FORMKEY, elementNode));
    task.setDueDate(getPropertyValueAsString(PROPERTY_DUEDATE, elementNode));
    task.setCategory(getPropertyValueAsString(PROPERTY_CATEGORY, elementNode));
    
    JsonNode assignmentNode = getProperty(PROPERTY_USERTASK_ASSIGNMENT, elementNode);
    if (assignmentNode != null) {
      JsonNode itemsNode = assignmentNode.get(EDITOR_PROPERTIES_GENERAL_ITEMS);
      if (itemsNode != null) {
        Iterator<JsonNode> assignmentIterator = itemsNode.getElements();
        while (assignmentIterator.hasNext()) {
          JsonNode assignmentItemNode = assignmentIterator.next();
          if (assignmentItemNode.get(PROPERTY_USERTASK_ASSIGNMENT_TYPE) != null && 
              assignmentItemNode.get(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION) != null) {
            
            String assignmentType = assignmentItemNode.get(PROPERTY_USERTASK_ASSIGNMENT_TYPE).asText();
            if (PROPERTY_USERTASK_ASSIGNEE.equals(assignmentType)) {
              task.setAssignee(assignmentItemNode.get(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION).asText());
            } else if (PROPERTY_USERTASK_CANDIDATE_USERS.equals(assignmentType)) {
              task.setCandidateUsers(getValueAsList(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION, assignmentItemNode));
            } else if (PROPERTY_USERTASK_CANDIDATE_GROUPS.equals(assignmentType)) {
              task.setCandidateGroups(getValueAsList(PROPERTY_USERTASK_ASSIGNMENT_EXPRESSION, assignmentItemNode));
            }
          }
        }
      }
    }
    convertJsonToFormProperties(elementNode, task);
    convertJsonToResources(elementNode, task);
    return task;
  }
}
