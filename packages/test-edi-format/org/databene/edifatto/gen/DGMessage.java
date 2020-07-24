/*
 * Copyright (C) 2013-2015 Volker Bergmann (volker.bergmann@bergmann-it.de).
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.databene.edifatto.gen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a 'dangerous goods message'.
 * Created: 17.12.2013 15:05:19
 * @since 1.0
 * @author Volker Bergmann
 */

public class DGMessage {
	
	private String sender;
	private String receiver;
	private Date datetime;
	private String controlRef;
	private String messageRef;
	private Vessel vessel;
	private String departurePort;
	private Date departureDate;
	private String nextPort;
	private Date arrivalDate;
	private List<DGItem> items;
	
	public DGMessage() {
		this.items = new ArrayList<DGItem>();
	}

	public String getSender() {
		return sender;
	}

	public DGMessage withSender(String sender) {
		this.sender = sender;
		return this;
	}
	
	public String getReceiver() {
		return receiver;
	}

	public DGMessage withReceiver(String receiver) {
		this.receiver = receiver;
		return this;
	}
	
	public Date getDatetime() {
		return datetime;
	}

	public DGMessage withDatetime(Date datetime) {
		this.datetime = datetime;
		return this;
	}
	
	public String getControlRef() {
		return controlRef;
	}

	public DGMessage withControlRef(String controlRef) {
		this.controlRef = controlRef;
		return this;
	}
	
	public String getMessageRef() {
		return messageRef;
	}

	public DGMessage withMessageRef(String messageRef) {
		this.messageRef = messageRef;
		return this;
	}
	
	public Vessel getVessel() {
		return vessel;
	}
	
	public DGMessage withVessel(Vessel vessel) {
		this.vessel = vessel;
		return this;
	}
	
	public Date getDepartureDate() {
		return departureDate;
	}

	public DGMessage withDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
		return this;
	}

	public String getDeparturePort() {
		return departurePort;
	}
	
	public DGMessage withDeparturePort(String departurePort) {
		this.departurePort = departurePort;
		return this;
	}
	
	public String getNextPort() {
		return nextPort;
	}
	
	public DGMessage withNextPort(String nextPort) {
		this.nextPort = nextPort;
		return this;
	}
	
	public Date getArrivalDate() {
		return arrivalDate;
	}

	public DGMessage withArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
		return this;
	}

	public int getSegmentCount() {
		return 10 + items.size() * 15;
	}
	
	public List<DGItem> getItems() {
		return items;
	}
	
	public void addItem(DGItem item) {
		this.items.add(item);
	}
	
}
