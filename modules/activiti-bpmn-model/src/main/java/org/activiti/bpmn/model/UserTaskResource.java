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
package org.activiti.bpmn.model;

/**
 * @author iColabora
 */
public class Resource extends BaseElement {

	protected String resource_id;
	protected String amount;
	protected String daily_time;
	protected String currency;
	protected String cost;
	protected String time_unit;


	public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDaily_time() {
		return daily_time;
	}

	public void setDaily_time(String daily_time) {
		this.daily_time = daily_time;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getTime_unit() {
		return time_unit;
	}

	public void setTime_unit(String time_unit) {
		this.time_unit = time_unit;
	}

	public Resource clone() {
		Resource clone = new Resource();
		clone.setValues(this);
		return clone;
	}

	public void setValues(Resource otherProperty) {
		setResource_id(otherProperty.getResource_id());
		setAmount(otherProperty.getAmount());
		setDaily_time(otherProperty.getDaily_time());
		setCurrency(otherProperty.getCurrency());
		setCost(otherProperty.getCost());
		setTime_unit(otherProperty.getTime_unit());
	}
}
