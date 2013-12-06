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
package org.activiti.bpmn.converter.child;

import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.util.BpmnXMLUtil;
import org.activiti.bpmn.model.BaseElement;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Resource;
import org.activiti.bpmn.model.UserTask;

/**
 * @author iColabora
 */
public class ResourceParser extends BaseChildElementParser {

  public String getElementName() {
    return ELEMENT_USERTASK_RESOURCE;
  }
  
  public void parseChildElement(XMLStreamReader xtr, BaseElement parentElement, BpmnModel model) throws Exception {
    
    if (parentElement instanceof UserTask == false) return;
    
    Resource resource = new Resource();
    BpmnXMLUtil.addXMLLocation(resource, xtr);
    resource.setResource_id(xtr.getAttributeValue(null, ATTRIBUTE_USERTASK_RESOURCE_ID));
    resource.setAmount(xtr.getAttributeValue(null, ATTRIBUTE_USERTASK_RESOURCE_AMOUNT));
    resource.setDaily_time(xtr.getAttributeValue(null, ATTRIBUTE_USERTASK_RESOURCE_DAILY_TIME));
    resource.setCurrency(xtr.getAttributeValue(null, ATTRIBUTE_USERTASK_RESOURCE_CURRENCY));
    resource.setCost(xtr.getAttributeValue(null, ATTRIBUTE_USERTASK_RESOURCE_COST));
    resource.setTime_unit(xtr.getAttributeValue(null, ATTRIBUTE_USERTASK_RESOURCE_TIME_UNIT));
    
    ((UserTask) parentElement).getResources().add(resource);
    
  }
}
