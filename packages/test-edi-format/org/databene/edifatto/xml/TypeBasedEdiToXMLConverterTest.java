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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerFactoryConfigurationError;

import org.databene.commons.Encodings;
import org.databene.commons.IOUtil;
import org.databene.commons.xml.XMLUtil;
import org.databene.edifatto.EdiReader;
import org.databene.edifatto.model.Interchange;
import org.databene.edifatto.xml.TypeBasedEdiToXMLConverter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests the {@link TypeBasedEdiToXMLConverter}.
 * Created: 17.12.2013 10:05:18
 * @since 1.0
 * @author Volker Bergmann
 */

public class TypeBasedEdiToXMLConverterTest {

	@Test
	public void testXmlConversion() throws Exception {
		// load edifact file
		Interchange interchange = new EdiReader().readInterchange(IOUtil.getInputStreamForURI("IFTDGN_1.edi"));
		// convert
		TypeBasedEdiToXMLConverter converter = new TypeBasedEdiToXMLConverter();
		Document document = converter.convert(interchange);
		// check root element
		Element root = document.getDocumentElement();
		assertEquals("interchange", root.getNodeName());
		Element[] children = XMLUtil.getChildElements(root);
		assertEquals(3, children.length);
		Element message = children[1];
		assertEquals("message", message.getNodeName());
		assertEquals("IFTDGN", message.getAttribute("type"));
		// check whole file
		String actualXMLFileContent = formatXmlText(document);
		String expectedXMLFileContent = IOUtil.getContentOfURI("IFTDGN_1.xml");
		assertEquals(expectedXMLFileContent, actualXMLFileContent);
	}

	public String formatXmlText(Document document) throws TransformerFactoryConfigurationError, UnsupportedEncodingException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLUtil.saveDocument(document, Encodings.UTF_8, out);
		String xmlText = new String(out.toByteArray(), Encodings.UTF_8);
		System.out.println(xmlText);
		return xmlText;
	}
	
}
