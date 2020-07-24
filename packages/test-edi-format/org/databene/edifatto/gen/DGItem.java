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

/**
 * Represents an item in a 'dangerous goods message'.
 * Created: 17.12.2013 16:05:18
 * @since 1.0
 * @author Volker Bergmann
 */

public class DGItem {
	
	private String cniCode;
	private int cniItem;
	private String containerCode;
	private String containerType;
	private double equipmentWeightTne;
	private String portOfLoading;
	private String portOfDischarge;
	private String hazardCode;
	private String undgId;
	private String dgFpDescription;
	private String shipmentFpDegree;
	private String shipmentFpUnit;
	private String packagingDangerLevelCode;
	private String emergencyProcedure;
	private String[] freeText;
	private String netWeightKgm;
	private String grossWeightKgm;
	private String stowageLocation;
	
	public String getCniCode() {
		return cniCode;
	}
	
	public DGItem withCniCode(String cniCode) {
		this.cniCode = cniCode;
		return this;
	}

	public int getCniItem() {
		return cniItem;
	}
	
	public DGItem withCniItem(int cniItem) {
		this.cniItem = cniItem;
		return this;
	}

	public String getContainerCode() {
		return containerCode;
	}
	
	public DGItem withContainerCode(String containerCode) {
		this.containerCode = containerCode;
		return this;
	}

	public String getContainerType() {
		return containerType;
	}
	
	public DGItem withContainerType(String containerType) {
		this.containerType = containerType;
		return this;
	}

	public double getEquipmentWeightTne() {
		return equipmentWeightTne;
	}
	
	public DGItem withEquipmentWeightTne(double equipmentWeightTne) {
		this.equipmentWeightTne = equipmentWeightTne;
		return this;
	}

	public String getPortOfLoading() {
		return portOfLoading;
	}
	
	public DGItem withPortOfLoading(String portOfLoading) {
		this.portOfLoading = portOfLoading;
		return this;
	}

	public String getPortOfDischarge() {
		return portOfDischarge;
	}
	
	public DGItem withPortOfDischarge(String portOfDischarge) {
		this.portOfDischarge = portOfDischarge;
		return this;
	}

	public String getHazardCode() {
		return hazardCode;
	}
	
	public DGItem withHazardCode(String hazardCode) {
		this.hazardCode = hazardCode;
		return this;
	}

	public String getUndgId() {
		return undgId;
	}
	
	public DGItem withUndgId(String undgId) {
		this.undgId = undgId;
		return this;
	}

	public String getDgFpDescription() {
		return dgFpDescription;
	}
	
	public DGItem withDgFpDescription(String dgFpDescription) {
		this.dgFpDescription = dgFpDescription;
		return this;
	}

	public String getShipmentFpDegree() {
		return shipmentFpDegree;
	}
	
	public DGItem withShipmentFpDegree(String shipmentFpDegree) {
		this.shipmentFpDegree = shipmentFpDegree;
		return this;
	}

	public String getShipmentFpUnit() {
		return shipmentFpUnit;
	}
	
	public DGItem withShipmentFpUnit(String shipmentFpUnit) {
		this.shipmentFpUnit = shipmentFpUnit;
		return this;
	}

	public String getPackagingDangerLevelCode() {
		return packagingDangerLevelCode;
	}
	
	public DGItem withPackagingDangerLevelCode(String packagingDangerLevelCode) {
		this.packagingDangerLevelCode = packagingDangerLevelCode;
		return this;
	}

	public String getEmergencyProcedure() {
		return emergencyProcedure;
	}
	
	public DGItem withEmergencyProcedure(String emergencyProcedure) {
		this.emergencyProcedure = emergencyProcedure;
		return this;
	}

	public String[] getFreeText() {
		return freeText;
	}
	
	public DGItem withFreeText(String... freeText) {
		this.freeText = freeText;
		return this;
	}

	public String getNetWeightKgm() {
		return netWeightKgm;
	}
	
	public DGItem withNetWeightKgm(String netWeightKgm) {
		this.netWeightKgm = netWeightKgm;
		return this;
	}

	public String getGrossWeightKgm() {
		return grossWeightKgm;
	}
	
	public DGItem withGrossWeightKgm(String grossWeightKgm) {
		this.grossWeightKgm = grossWeightKgm;
		return this;
	}

	public String getStowageLocation() {
		return stowageLocation;
	}
	
	public DGItem withStowageLocation(String stowageLocation) {
		this.stowageLocation = stowageLocation;
		return this;
	}

}
