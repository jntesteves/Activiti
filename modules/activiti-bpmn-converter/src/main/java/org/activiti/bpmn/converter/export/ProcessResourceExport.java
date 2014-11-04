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
package org.activiti.bpmn.converter.export;

import javax.xml.stream.XMLStreamWriter;

import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ProcessResource;

public class ProcessResourceExport implements BpmnXMLConstants {

	public static boolean writeProcessResources(Process process, boolean didWriteExtensionStartElement, XMLStreamWriter xtw) throws Exception {
		if (process.getResources().size() > 0) {
		
      if (!didWriteExtensionStartElement) { 
        xtw.writeStartElement(ELEMENT_EXTENSIONS);
        didWriteExtensionStartElement = true;
      }
		  
			for (ProcessResource resource : process.getResources()) {
				xtw.writeStartElement(ACTIVITI_EXTENSIONS_PREFIX, ELEMENT_PROCESS_RESOURCE, ACTIVITI_EXTENSIONS_NAMESPACE);
				if (resource.getId() != null) xtw.writeAttribute(ATTRIBUTE_PROCESS_RESOURCE_ID, resource.getId());
				if (resource.getAmount() != null) xtw.writeAttribute(ATTRIBUTE_PROCESS_RESOURCE_AMOUNT, resource.getAmount());
				if (resource.getDailyTime() != null) xtw.writeAttribute(ATTRIBUTE_PROCESS_RESOURCE_DAILY_TIME, resource.getDailyTime());
				if (resource.getCurrency() != null) xtw.writeAttribute(ATTRIBUTE_PROCESS_RESOURCE_CURRENCY, resource.getCurrency());
				if (resource.getCost() != null) xtw.writeAttribute(ATTRIBUTE_PROCESS_RESOURCE_COST, resource.getCost());
				if (resource.getTimeUnit() != null) xtw.writeAttribute(ATTRIBUTE_PROCESS_RESOURCE_TIME_UNIT, resource.getTimeUnit());
				xtw.writeEndElement();
			}
			
		}
		
		return didWriteExtensionStartElement;
	}
}
