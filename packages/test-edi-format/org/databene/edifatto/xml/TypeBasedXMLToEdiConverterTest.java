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
package org.databene.edifatto.xml;

import org.databene.commons.xml.XMLUtil;
import org.databene.edifatto.EdiFormatSymbols;
import org.databene.edifatto.Edifatto;
import org.databene.edifatto.model.EdiProtocol;
import org.databene.edifatto.model.Interchange;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Tests the {@link TypeBasedXMLToEdiConverter}.
 * Created: 17.04.2014 12:36:02
 * @since 1.1
 * @author Volker Bergmann
 */

public class TypeBasedXMLToEdiConverterTest {
	
	@Test
	public void test() throws Exception {
		Document document = XMLUtil.parse("IFTDGN_1.xml");
		TypeBasedXMLToEdiConverter converter = new TypeBasedXMLToEdiConverter(EdiProtocol.EDIFACT, EdiFormatSymbols.EDIFACT);
		Interchange actual = converter.convert(document);
		Interchange expected = Edifatto.parseEdiFile("IFTDGN_1.edi");
		Edifatto.assertEquals(expected, actual);
	}
	
}
