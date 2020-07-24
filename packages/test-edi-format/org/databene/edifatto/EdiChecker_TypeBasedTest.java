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

import java.io.IOException;

import org.databene.commons.SystemInfo;
import org.databene.commons.converter.XMLNode2StringConverter;
import org.databene.commons.xml.XMLUtil;
import org.databene.edifatto.EdiChecker;
import org.databene.edifatto.Edifatto;
import org.databene.edifatto.model.Component;
import org.databene.edifatto.model.Composite;
import org.databene.edifatto.model.Interchange;
import org.databene.edifatto.model.Message;
import org.databene.edifatto.util.TypeBasedXMLComparisonModel;
import org.databene.formats.compare.AggregateDiff;
import org.databene.formats.compare.DiffDetail;
import org.databene.formats.compare.DiffFactory;
import org.databene.formats.xml.compare.ComparisonContext;
import org.databene.formats.xml.compare.XMLComparisonSettings;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * Tests the {@link EdiChecker} with a {@link TypeBasedXMLComparisonModel}.
 * Created: 10.10.2013 17:01:40
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdiChecker_TypeBasedTest {
	
	private static final String LF = SystemInfo.getLineSeparator();
	
	DiffFactory diffFactory = new DiffFactory(new XMLNode2StringConverter());

	@Test
	public void testAssertEquals_positive() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1();
		new EdiChecker().assertEquals(expectedInterchange, actualInterchange);
	}

	@Test
	public void testAssertEquals_negative() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1AndModifyRelease();
		boolean failed = false;
		try {
			new EdiChecker().assertEquals(expectedInterchange, actualInterchange);
		} catch (AssertionError e) {
			failed = true;
			System.out.println(e.getMessage());
			String expectedMessage = "Interchanges do not match. Found 2 differences" + SystemInfo.getLineSeparator() +
				"Different attribute: expected '03A' but found '03X' at interchange/message@release" + SystemInfo.getLineSeparator() +
				"Different element text: expected " + LF + "<comp>03A</comp>" + LF + " but found " + LF + "<comp>03X</comp>" + LF + " at interchange/message/S-UNH_1(seg#2)[3][3]";
			assertEquals(expectedMessage, e.getMessage());
		}
		if (!failed)
			throw new AssertionError("Expected AssertionError did not occur");
	}

	@Test
	public void testAssertEqualsExcludingXPaths() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1AndModifyRelease();
		XMLComparisonSettings settings = new XMLComparisonSettings(new TypeBasedXMLComparisonModel());
		settings.tolerateAnyDiffAt("//message/@release");
		settings.tolerateAnyDiffAt("//segment[@name='S-UNH_1']");
		new EdiChecker(settings).assertEqualsExcludingXPaths(expectedInterchange, actualInterchange);
	}
	
	@Test
	public void testAssertEqualsExcludingElements() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1();
		Composite child = (Composite) actualInterchange.getUniqueSegmentByTag("UNB").getChild(3);
		child.getChild(0).setData("123");
		child.getChild(1).setData("456");
		((Component) actualInterchange.getMessages().get(0).getUniqueSegmentByTag("UNH").getChild(0)).setData("XXX");
		Edifatto.writeXmlFile(Edifatto.convertToXml(expectedInterchange), "target/xml1.xml", "UTF-8");
		Edifatto.writeXmlFile(Edifatto.convertToXml(actualInterchange), "target/xml2.xml", "UTF-8");
		XMLComparisonSettings settings = new XMLComparisonSettings(new TypeBasedXMLComparisonModel());
		settings.tolerateAnyDiffAt("//segment[@tag='UNB']/elem[position()=5]");
		settings.tolerateAnyDiffAt("//segment[@tag='UNH']/elem[position()=2]");
		settings.tolerateAnyDiffAt("//group[@pos='0090']/group[@pos='0130']/segment[@tag='DTM']/elem[position()=2]/comp[position()=2]");
		new EdiChecker(settings).assertEqualsExcludingXPaths(expectedInterchange, actualInterchange);
	}
	
	@Test
	public void testCompareEqualElements() {
		check("<a/>", "<a/>");
		check("<a><b/></a>", "<a><b/></a>");
	}

	@Test
	public void testCompareMissingChildElements() {
		check(
			"<a><b/><c/></a>", 
			"<a/>", 
			diffFactory.missing(XMLUtil.parseStringAsElement("<b/>"), "list element", "a/b"),
			diffFactory.missing(XMLUtil.parseStringAsElement("<c/>"), "list element", "a/c")
		);
	}

	@Test
	public void testCompareUnexpectedChildElements() {
		check(
			"<a/>", 
			"<a><b/><c/></a>", 
			diffFactory.unexpected(XMLUtil.parseStringAsElement("<b/>"), "list element", "a/b"),
			diffFactory.unexpected(XMLUtil.parseStringAsElement("<c/>"), "list element", "a/c")
		);
	}

	@Test
	public void testCompareMovedChildElement() {
		check(
			"<a><b/><c/><d/></a>", 
			"<a><b/><d/><c/></a>", 
			diffFactory.moved(XMLUtil.parseStringAsElement("<c/>"), "list element", "a/c", "a/d")
		);
	}

	@Test
	public void testCompareChangedChildText() {
		check(
			"<a><b>x</b></a>", 
			"<a><b>y</b></a>", 
			diffFactory.different(XMLUtil.parseStringAsElement("<b>x</b>"), XMLUtil.parseStringAsElement("<b>y</b>"), "element text", "a/b")
		);
	}

	@Test
	public void testCompareChangedChildElement() {
		check(
			"<a><b/><c>a</c></a>", 
			"<a><b/><c>b</c></a>", 
			diffFactory.different(XMLUtil.parseStringAsElement("<c>a</c>"), XMLUtil.parseStringAsElement("<c>b</c>"), "element text", "a/c")
		);
	}

	@Test
	public void testCompareChangedNamedChildElement() {
		check(
			"<a><b/><c name='x'>a</c></a>", 
			"<a><b/><c name='x'>b</c></a>", 
			diffFactory.different(XMLUtil.parseStringAsElement("<c name=\"x\">a</c>"), XMLUtil.parseStringAsElement("<c name=\"x\">b</c>"), "element text", "a/x")
		);
	}

	@Test
	public void testCompareChangedAttribute() {
		check(
			"<a><b/><c att='x'>z</c></a>", 
			"<a><b/><c att='y'>z</c></a>", 
			diffFactory.different("x", "y", "attribute", "a/c@att")
		);
	}

	@Test
	public void testCompareChangedElementName() {
		check(
			"<a><b/><c name='x'>z</c></a>", 
			"<a><b/><c name='y'>z</c></a>", 
			diffFactory.missing(XMLUtil.parseStringAsElement("<c name='x'>z</c>"), "list element", "a/x"),
			diffFactory.unexpected(XMLUtil.parseStringAsElement("<c name='y'>z</c>"), "list element", "a/y")
		);
	}

	@Test
	public void testCompareMissingNestedChildElements() {
		check(
			"<a><b/><c><d/></c></a>", 
			"<a><b/><c></c></a>", 
			diffFactory.missing(XMLUtil.parseStringAsElement("<d/>"), "list element", "a/c/d")
		);
	}

	@Test
	public void testCompareIFTDGNs() throws Exception {
		Interchange ic1 = loadTestInterchange1();
		Interchange ic2 = loadTestInterchange2();
		String s1 = XMLUtil.format(Edifatto.convertToXml(ic1).getDocumentElement());
		String s2 = XMLUtil.format(Edifatto.convertToXml(ic2).getDocumentElement());
		check(
			s1, 
			s2, 
			diffFactory.different(XMLUtil.parseStringAsElement("<comp>USS ENTERPRISE?</comp>"), XMLUtil.parseStringAsElement("<comp>SEA PLUSPLUS</comp>"), "element text", "interchange/message/L-TDT_10/S-TDT_10(seg#5)[9][4]"),
			diffFactory.different(XMLUtil.parseStringAsElement("<comp>UUUU1234567</comp>"), XMLUtil.parseStringAsElement("<comp>UUUU12345XX</comp>"), "element text", "interchange/message/L-CNI_25[10]/L-GID_42/L-DGS_49/L-SGP_55/S-SGP_55(seg#52)[2][1]")
		);
	}
	
	private static void check(String xml1, String xml2, DiffDetail... expectedDiffs) {
		Element expected = XMLUtil.parseStringAsElement(xml1);
		Element actual = XMLUtil.parseStringAsElement(xml2);
		ComparisonContext nodeSettings = new ComparisonContext();
		EdiChecker checker = new EdiChecker();
		AggregateDiff actualDiffs = new AggregateDiff(expected, actual, new XMLComparisonSettings(new TypeBasedXMLComparisonModel()));
		checker.compareElementsExcludingXPaths(expected, actual, nodeSettings, expected.getNodeName(), actualDiffs);
		assertEquals(expectedDiffs.length, actualDiffs.getDetailCount());
		for (int i = 0; i < expectedDiffs.length; i++) {
			System.out.println(expectedDiffs[i]);
			System.out.println(actualDiffs.getDetail(i));
			assertEquals(expectedDiffs[i].toString(), actualDiffs.getDetail(i).toString());
		}
	}
	
	// private helper methods ------------------------------------------------------------------------------------------
	
	private static Interchange loadTestInterchange1AndModifyRelease() throws IOException {
		Interchange actualInterchange = loadTestInterchange1();
		Message message = actualInterchange.getMessages().get(0);
		((Composite) message.getHeaderSegment().getChild(1)).getChild(2).setData("03X");
		return actualInterchange;
	}
	
	private static final Interchange loadTestInterchange1() throws IOException {
		return Edifatto.parseEdiFile("IFTDGN_1.edi");
	}
	
	private static final Interchange loadTestInterchange2() throws IOException {
		return Edifatto.parseEdiFile("IFTDGN_2.edi");
	}
	
}
