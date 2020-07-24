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
package org.databene.edifatto;

import java.util.HashMap;
import java.util.Map;

import org.databene.commons.TimeUtil;
import org.databene.edifatto.gen.DGMessage;
import org.databene.edifatto.gen.DGItem;
import org.databene.edifatto.gen.Vessel;
import org.databene.edifatto.model.Interchange;
import org.junit.Test;

/**
 * Tests the {@link EdiGenerator}.
 * Created: 11.12.2013 18:29:43
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdiGeneratorTest {
	
	@Test
	public void test() throws Exception {
		// set variables
		DGMessage m = new DGMessage().withSender("ABCDEF").withReceiver("XYZ").
				withDatetime(TimeUtil.date(2013, 7, 21, 12, 59, 0, 0)).withControlRef("XXX").
				withMessageRef("M130821145902B").withVessel(new Vessel("1234567", "USS ENTERPRISE?", "A8RH6")).
				withDeparturePort("EGALY").withDepartureDate(TimeUtil.date(2013, 7, 23)).
				withNextPort("LBBEY").withArrivalDate(TimeUtil.date(2013, 7, 22));
		
		DGItem item1 = new DGItem().withContainerCode("UUUU1234568").withContainerType("44G0").withEquipmentWeightTne(26).
				withCniCode("3ALYSA0390").withCniItem(6).
				withPortOfLoading("EGALY").withPortOfDischarge("SYLTK").
				withHazardCode("3").withUndgId("1263").withDgFpDescription("24C").
				withShipmentFpDegree("24").withShipmentFpUnit("CEL").
				withPackagingDangerLevelCode("3").withEmergencyProcedure("F-E").
				withFreeText("PAINT").
				withNetWeightKgm("21113").withGrossWeightKgm("0").withStowageLocation("0420482");
		m.addItem(item1);
		
		DGItem item2 = new DGItem().withContainerCode("UUUU1234569").withContainerType("22G1").withEquipmentWeightTne(29.4).
				withCniCode("3MERNE3502").withCniItem(9).
				withPortOfLoading("EGALY").withPortOfDischarge("GBFXT").
				withHazardCode("9").withUndgId("3077").withDgFpDescription("").
				withShipmentFpDegree("").withShipmentFpUnit("").
				withPackagingDangerLevelCode("3").withEmergencyProcedure("F-A").
				withFreeText("ENVIRONMENTALLY HAZARDOUS ", "SUBSTANCE, SOLID, N.O.S. Z", " INC OXIDE %72").
				withNetWeightKgm("25000").withGrossWeightKgm("0").withStowageLocation("0030212");
		m.addItem(item2);
		
		DGItem item3 = new DGItem().withContainerCode("UUUU1234567").withContainerType("22G1").withEquipmentWeightTne(22.8).
				withCniCode("3MERNE3607").withCniItem(10).
				withPortOfLoading("EGALY").withPortOfDischarge("SYLTK").
				withHazardCode("6.1").withUndgId("3288").withDgFpDescription("").
				withShipmentFpDegree("").withShipmentFpUnit("").
				withPackagingDangerLevelCode("3").withEmergencyProcedure("F-A").
				withFreeText("TOXIC SOLID, INORGANIC, N.", " O.S. SODIUM BICHROMATE AN", "H YDROUS").
				withNetWeightKgm("20000").withGrossWeightKgm("0").withStowageLocation("0131082");
		m.addItem(item3);
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("m", m);
		
		// create interchange
		Interchange actual = Edifatto.createInterchange("IFTDGN.ftl", EdiFormatSymbols.EDIFACT, variables);
		
		// verify result
		System.out.println(Edifatto.formatRecursively(actual));
		Interchange expected = Edifatto.parseEdiFile("IFTDGN_1.edi");
		Edifatto.assertEquals(expected, actual);
	}
	
}
