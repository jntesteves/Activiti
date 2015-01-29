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
package org.activiti.turbina.language.json.converter;

import java.util.Map;

import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ProcessResource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author iColabora
 */
public class ProcessResourceJsonConverter extends BaseBpmnJsonConverter {

  public static void fillTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap,
      Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    
    fillJsonTypes(convertersToBpmnMap);
    fillBpmnTypes(convertersToJsonMap);
  }
  
  public static void fillJsonTypes(Map<String, Class<? extends BaseBpmnJsonConverter>> convertersToBpmnMap) {
    convertersToBpmnMap.put(PROPERTY_PROCESS_RESOURCE, ProcessResourceJsonConverter.class);
  }
  
  public static void fillBpmnTypes(Map<Class<? extends BaseElement>, Class<? extends BaseBpmnJsonConverter>> convertersToJsonMap) {
    convertersToJsonMap.put(ProcessResource.class, ProcessResourceJsonConverter.class);
  }
  
  @Override
  protected String getStencilId(FlowElement flowElement) {
    return PROPERTY_PROCESS_RESOURCE;
  }
  
  @Override
  protected void convertElementToJson(ObjectNode propertiesNode, FlowElement flowElement) {
	ProcessResource resource = (ProcessResource) flowElement;
    setPropertyValue(PROPERTY_PROCESS_RESOURCE_ID, resource.getId().toString(), propertiesNode);
    setPropertyValue(PROPERTY_PROCESS_RESOURCE_AMOUNT, resource.getAmount().toString(), propertiesNode);
    setPropertyValue(PROPERTY_PROCESS_RESOURCE_DAILY_TIME, resource.getDailyTime().toString(), propertiesNode);
    setPropertyValue(PROPERTY_PROCESS_RESOURCE_CURRENCY, resource.getCurrency().toString(), propertiesNode);
    setPropertyValue(PROPERTY_PROCESS_RESOURCE_COST, resource.getCost().toString(), propertiesNode);
    setPropertyValue(PROPERTY_PROCESS_RESOURCE_TIME_UNIT, resource.getTimeUnit().toString(), propertiesNode);
  }
  
  @Override
  protected FlowElement convertJsonToElement(JsonNode elementNode, JsonNode modelNode, Map<String, JsonNode> shapeMap) {
	ProcessResource resource = new ProcessResource();
	resource.setId(getPropertyValueAsString(PROPERTY_PROCESS_RESOURCE_ID, elementNode));
	resource.setAmount(getPropertyValueAsString(PROPERTY_PROCESS_RESOURCE_AMOUNT, elementNode));
	resource.setDailyTime(getPropertyValueAsString(PROPERTY_PROCESS_RESOURCE_DAILY_TIME, elementNode));
	resource.setCurrency(getPropertyValueAsString(PROPERTY_PROCESS_RESOURCE_CURRENCY, elementNode));
	resource.setCost(getPropertyValueAsString(PROPERTY_PROCESS_RESOURCE_COST, elementNode));
	resource.setTimeUnit(getPropertyValueAsString(PROPERTY_PROCESS_RESOURCE_TIME_UNIT, elementNode));
    
    return resource;
  }
}
