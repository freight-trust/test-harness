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

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.xpath.XPathConstants;

import org.databene.commons.FileUtil;
import org.databene.commons.xml.XMLUtil;
import org.databene.edifatto.Edifatto;
import org.databene.edifatto.model.Interchange;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Tests the {@link Edifatto} class.
 * Created: 10.10.2013 18:03:39
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdifattoTest {
	
	@Test
	public void testXmlConversion() throws Exception {
		Interchange interchange = Edifatto.parseEdiFile("IFTDGN_1.edi");
		Document document = Edifatto.convertToXml(interchange);
		String xmlFileName = getClass().getSimpleName() + ".xml";
		Edifatto.writeXmlFile(document, xmlFileName, "UTF-8");
		Document doc = XMLUtil.parse(xmlFileName);
		Element root = doc.getDocumentElement();
		assertEquals("interchange", root.getNodeName());
		Element[] children = XMLUtil.getChildElements(root);
		assertEquals(3, children.length);
		Element message = children[1];
		assertEquals("message", message.getNodeName());
		assertEquals("IFTDGN", message.getAttribute("type"));
		FileUtil.deleteIfExists(new File(xmlFileName));
	}
	
	@Test
	public void testQueryString() throws Exception {
		Interchange interchange = Edifatto.parseEdiFile("IFTDGN_1.edi");
		String messageType = (String) Edifatto.queryXML(interchange, "/interchange/message/@type", XPathConstants.STRING);
		assertEquals("IFTDGN", messageType);
	}
	
	@Test
	public void testQuerySingleGroup() throws Exception {
		Interchange interchange = Edifatto.parseEdiFile("IFTDGN_1.edi");
		Element group = (Element) Edifatto.queryXML(interchange, "//group[segment[@tag='EQD']/elem[position()=3 and comp='UUUU1234568']]", XPathConstants.NODE);
		assertNotNull("Searched element was not found", group);
		assertEquals("L-EQD_22", group.getAttribute("name"));
		Element eqd = (Element) group.getChildNodes().item(0);
		assertEquals("44G0", eqd.getChildNodes().item(3).getTextContent());
	}
	
	@Test
	public void testQueryGroups() throws Exception {
		Interchange interchange = Edifatto.parseEdiFile("IFTDGN_1.edi");
		NodeList groups = (NodeList) Edifatto.queryXML(interchange, "//group[@name='L-CNI_25']", XPathConstants.NODESET);
		assertEquals(3, groups.getLength());
		for (int i = 0; i < groups.getLength(); i++) {
			Element group = (Element) groups.item(i);
			assertEquals("L-CNI_25", group.getAttribute("name"));
		}
	}
	
	@Test
	public void testSaveAsDot() throws Exception {
		Interchange ic = Edifatto.parseEdiFile("IFTDGN_1.edi");
		Edifatto.exportAsDot(ic, "target/IFTDGN_1.dot");
	}
	
}
