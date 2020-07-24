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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.databene.commons.SystemInfo;
import org.databene.commons.converter.XMLNode2StringConverter;
import org.databene.commons.xml.XMLUtil;
import org.databene.edifatto.model.Component;
import org.databene.edifatto.model.Composite;
import org.databene.edifatto.model.Interchange;
import org.databene.edifatto.model.Message;
import org.databene.edifatto.util.EdiUtil;
import org.databene.edifatto.xml.NameBasedEdiToXMLConverter;
import org.databene.formats.compare.AggregateDiff;
import org.databene.formats.compare.DiffDetail;
import org.databene.formats.compare.DiffFactory;
import org.databene.formats.xml.compare.DefaultXMLComparisonModel;
import org.databene.formats.xml.compare.ComparisonContext;
import org.databene.formats.xml.compare.XMLComparisonSettings;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * Tests the {@link EdiChecker} with a {@link DefaultXMLComparisonModel}.
 * Created: 03.06.2014 14:25:49
 * @since 1.2
 * @author Volker Bergmann
 */

public class EdiChecker_NameBasedTest {
	
	private static final String LF = SystemInfo.getLineSeparator();

	DiffFactory diffFactory = new DiffFactory(new XMLNode2StringConverter());

	@Test
	public void testAssertEquals_positive() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1();
		EdiChecker checker = new EdiChecker(new XMLComparisonSettings(new DefaultXMLComparisonModel()), new NameBasedEdiToXMLConverter());
		checker.assertEquals(expectedInterchange, actualInterchange);
	}
	
	@Test
	public void testAssertEquals_negative() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1AndModifyRelease();
		EdiChecker checker = new EdiChecker(new XMLComparisonSettings(new DefaultXMLComparisonModel()), new NameBasedEdiToXMLConverter());
		boolean failed = false;
		try {
			checker.assertEquals(expectedInterchange, actualInterchange);
		} catch (AssertionError e) {
			failed = true;
			System.out.println(e.getMessage());
			String expectedMessage = "Interchanges do not match. Found 1 difference" + SystemInfo.getLineSeparator() +
				"Different element text: expected " + LF + "<E-0054_1_02.03>03A</E-0054_1_02.03>" + LF + 
				" but found " + LF + "<E-0054_1_02.03>03X</E-0054_1_02.03>" + LF + " at T-IFTDGN/S-UNH_1/C-S009_1_02/E-0054_1_02.03";
			assertEquals(expectedMessage, e.getMessage());
		}
		if (!failed)
			throw new AssertionError("Expected AssertionError did not occur");
	}

	@Test
	public void testAssertEqualsExcludingXPaths() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1AndModifyRelease();
		XMLComparisonSettings settings = EdiUtil.settingsWithExclusionPaths(new DefaultXMLComparisonModel(), "T-IFTDGN/S-UNH_1/C-S009_1_02/E-0054_1_02.03");
		EdiChecker checker = new EdiChecker(settings, new NameBasedEdiToXMLConverter());
		checker.assertEqualsExcludingXPaths(expectedInterchange, actualInterchange);
	}
	
	@Test
	public void testAssertEqualsExcludingElements() throws Exception {
		Interchange expectedInterchange = loadTestInterchange1();
		Interchange actualInterchange = loadTestInterchange1();
		((Component) actualInterchange.getMessages().get(0).getUniqueSegmentByTag("UNH").getChild(0)).setData("XXX");
		Edifatto.writeXmlFile(Edifatto.convertToXml(expectedInterchange), "target/xml1.xml", "UTF-8");
		Edifatto.writeXmlFile(Edifatto.convertToXml(actualInterchange), "target/xml2.xml", "UTF-8");
		XMLComparisonSettings settings = EdiUtil.settingsWithExclusionPaths(new DefaultXMLComparisonModel(), "//S-UNH_1/E-0062_1_01");
		EdiChecker checker = new EdiChecker(settings, new NameBasedEdiToXMLConverter());
		checker.assertEqualsExcludingXPaths(expectedInterchange, actualInterchange);
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
			diffFactory.different(XMLUtil.parseStringAsElement("<c name=\"x\">a</c>"), XMLUtil.parseStringAsElement("<c name=\"x\">b</c>"), "element text", "a/c")
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
	
	/* TODO verify the test case
	@Test
	public void testCompareChangedElementName() {
		check(
			"<a><b/><c name='x'>z</c></a>", 
			"<a><b/><c name='y'>z</c></a>", 
			DiffFactory.missing(XMLUtil.parseStringAsElement("<c name='x'>z</c>"), "list element", "a/x"),
			DiffFactory.unexpected(XMLUtil.parseStringAsElement("<c name='y'>z</c>"), "list element", "a/y")
		);
	}
	*/
	
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
		String s1 = XMLUtil.format(new NameBasedEdiToXMLConverter().convert(ic1).getDocumentElement());
		String s2 = XMLUtil.format(new NameBasedEdiToXMLConverter().convert(ic2).getDocumentElement());
		check(
			s1, 
			s2, 
			diffFactory.different(XMLUtil.parseStringAsElement("<E-8212_10_08.04>USS ENTERPRISE?</E-8212_10_08.04>"), XMLUtil.parseStringAsElement("<E-8212_10_08.04>SEA PLUSPLUS</E-8212_10_08.04>"), "element text", "T-IFTDGN/L-TDT_10/S-TDT_10/C-C222_10_08/E-8212_10_08.04"),
			diffFactory.different(XMLUtil.parseStringAsElement("<E-8260_55_01.01>UUUU1234567</E-8260_55_01.01>"), XMLUtil.parseStringAsElement("<E-8260_55_01.01>UUUU12345XX</E-8260_55_01.01>"), "element text", "T-IFTDGN/L-CNI_25[10]/L-GID_42/L-DGS_49/L-SGP_55/S-SGP_55/C-C237_55_01/E-8260_55_01.01")
		);
	}
	
	@Test
	public void testCompareIFTDGNsWithReorderedCNIs_anyMoveAllowed() throws Exception {
		Interchange ic1 = loadTestInterchange1();
		Interchange ic2 = Edifatto.parseEdiFile("IFTDGN_1_reordered_CNI.edi");
		DefaultXMLComparisonModel model = new DefaultXMLComparisonModel();
		model.addKeyExpression("L-CNI_25", ".//E-1004_25_02.01");
		XMLComparisonSettings settings = EdiUtil.settingsWithExclusionPaths(model, "//E-1490_25_01");
		settings.tolerateMovedAt(null);
		EdiChecker checker = new EdiChecker(settings, new NameBasedEdiToXMLConverter());
		checker.assertEquals(ic1, ic2);
	}
	
	@Test(expected = AssertionError.class)
	public void testCompareIFTDGNsWithReorderedCNIs_moveNotAllowed() throws Exception {
		Interchange ic1 = loadTestInterchange1();
		Interchange ic2 = Edifatto.parseEdiFile("IFTDGN_1_reordered_CNI.edi");
		DefaultXMLComparisonModel model = new DefaultXMLComparisonModel();
		model.addKeyExpression("L-CNI_25", ".//E-1004_25_02.01");
		XMLComparisonSettings settings = EdiUtil.settingsWithExclusionPaths(model, "//E-1490_25_01");
		EdiChecker checker = new EdiChecker(settings, new NameBasedEdiToXMLConverter());
		checker.assertEquals(ic1, ic2);
	}
	
	@Test
	public void testCompareIFTDGNsWithReorderedCNIs_cniMoveAllowed() throws Exception {
		Interchange ic1 = loadTestInterchange1();
		Interchange ic2 = Edifatto.parseEdiFile("IFTDGN_1_reordered_CNI.edi");
		DefaultXMLComparisonModel model = new DefaultXMLComparisonModel();
		model.addKeyExpression("L-CNI_25", ".//E-1004_25_02.01");
		XMLComparisonSettings settings = new XMLComparisonSettings(model);
		settings.tolerateDifferentAt("//E-1490_25_01");
		settings.tolerateMovedAt("//L-CNI_25");
		EdiChecker checker = new EdiChecker(settings, new NameBasedEdiToXMLConverter());
		checker.assertEquals(ic1, ic2);
	}
	
	// private helper methods ------------------------------------------------------------------------------------------
	
	private static void check(String xml1, String xml2, DiffDetail... expectedDiffs) {
		Element expected = XMLUtil.parseStringAsElement(xml1);
		Element actual = XMLUtil.parseStringAsElement(xml2);
		DefaultXMLComparisonModel model = new DefaultXMLComparisonModel();
		XMLComparisonSettings settings = new XMLComparisonSettings(model);
		ComparisonContext excludedNodes = new ComparisonContext();
		AggregateDiff actualDiffs = new AggregateDiff(expected, actual, settings);
		EdiChecker checker = new EdiChecker(settings, new NameBasedEdiToXMLConverter());
		checker.compareElementsExcludingXPaths(expected, actual, excludedNodes, expected.getNodeName(), actualDiffs);
		assertEquals(expectedDiffs.length, actualDiffs.getDetailCount());
		for (int i = 0; i < expectedDiffs.length; i++) {
			System.out.println(expectedDiffs[i]);
			System.out.println(actualDiffs.getDetail(i));
			assertEquals(expectedDiffs[i].toString(), actualDiffs.getDetail(i).toString());
		}
	}
	
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
