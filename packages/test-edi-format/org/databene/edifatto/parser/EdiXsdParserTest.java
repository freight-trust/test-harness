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
package org.databene.edifatto.parser;

import static org.junit.Assert.*;

import org.databene.edifatto.definition.MessageDefinition;
import org.databene.edifatto.model.EdiProtocol;
import org.databene.edifatto.parser.EdiXsdParser;
import org.junit.Test;

/**
 * Tests the {@link EdiXsdParser}.
 * Created: 17.10.2013 17:19:07
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdiXsdParserTest {
	
	@Test
	public void testEdifact_IFTDNGN() throws Exception {
		MessageDefinition message = EdiXsdParser.parseSchema("org/databene/edifatto/D03A_IFTDGN.xsd", EdiProtocol.EDIFACT, "IFTDGN", "D", "03A");
		message.printRecursively();
		assertEquals("IFTDGN", message.getType());
		assertEquals("D", message.getVersion());
		assertEquals("03A", message.getRelease());
		assertEquals(11, message.getChildCount());
		assertEquals("BGM", message.getChild(1).getTag());
	}
	
	@Test
	public void testX12_300_5030() throws Exception {
		MessageDefinition message = EdiXsdParser.parseSchema("org/databene/edifatto/X12-5030_300.xsd", EdiProtocol.X12, "300", "5030", null);
		message.printRecursively();
		assertEquals("300", message.getType());
		assertEquals("5030", message.getVersion());
		assertNull(message.getRelease());
		assertEquals(19, message.getChildCount());
		assertEquals("B1", message.getChild(1).getTag());
	}
	
}
