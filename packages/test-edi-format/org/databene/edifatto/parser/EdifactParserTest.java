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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.databene.commons.IOUtil;
import org.databene.commons.SyntaxError;
import org.databene.edifatto.AbstractEdiTest;
import org.databene.edifatto.EdiParserSettings;
import org.databene.edifatto.Edifatto;
import org.databene.edifatto.ParsingResult;
import org.databene.edifatto.model.Composite;
import org.databene.edifatto.model.Interchange;
import org.databene.edifatto.model.InterchangeItem;
import org.databene.edifatto.model.Message;
import org.databene.edifatto.model.Segment;
import org.databene.edifatto.model.SegmentGroupItem;
import org.databene.edifatto.parser.EdifactParser;
import org.junit.Test;

/**
 * Tests the {@link EdifactParser}.
 * Created: 10.10.2013 16:23:22
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdifactParserTest extends AbstractEdiTest {
	
	@Test
	public void testParse_withoutLineBreaks() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1_without_linebreak.edi");
		ParsingResult result = new EdifactParser().parse(text);
		Interchange interchange = getSingleInterchange(result);
		System.out.println(Edifatto.formatRecursively(interchange));
		verifyIFTDGN_1(text, interchange, 0, 1);
	}

	@Test
	public void testParse_withAcceptedLineBreaks() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1.edi");
		ParsingResult result = new EdifactParser().parse(text);
		Interchange interchange = getSingleInterchange(result);
		System.out.println(Edifatto.formatRecursively(interchange));
		verifyIFTDGN_1(text, interchange, 0, 1);
	}

	@Test(expected = SyntaxError.class)
	public void testParse_withUnacceptedLineBreaks() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1.edi");
		EdifactParser parser = createEdifactParser(false, false, false);
		parser.parse(text);
	}
	
	@Test
	public void testParse_withAcceptedSpaceAfterSegment() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1-with_space_after_segment.edi");
		EdifactParser parser = createEdifactParser(true, true, false);
		ParsingResult result = parser.parse(text);
		Interchange interchange = getSingleInterchange(result);
		System.out.println(Edifatto.formatRecursively(interchange));
		verifyIFTDGN_1(text, interchange, 0, 1);
	}

	@Test(expected = SyntaxError.class)
	public void testParse_withUnacceptedSpaceAfterSegment() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1-with_space_after_segment.edi");
		EdifactParser parser = createEdifactParser(true, false, false);
		parser.parse(text);
	}
	
	@Test
	public void testParse_AcceptedMultipleInterchanges() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1-with_multiple_interchanges.edi");
		EdifactParser parser = createEdifactParser(true, false, true);
		ParsingResult result = parser.parse(text);
		assertEquals(2, result.getInterchanges().size());
		verifyIFTDGN_1(text, result.getInterchanges().get(0),    0, 2);
		verifyIFTDGN_1(text, result.getInterchanges().get(1), 1166, 2);
	}
	
	@Test(expected = SyntaxError.class)
	public void testParse_UnacceptedMultipleInterchanges() throws Exception {
		String text = IOUtil.getContentOfURI("IFTDGN_1-with_multiple_interchanges.edi");
		EdifactParser parser = createEdifactParser(true, false, false);
		parser.parse(text);
	}

	// TODO test UNA support
	
	
	// private helpers ---------------------------------------------------------
	
	private static EdifactParser createEdifactParser(boolean acceptingLineBreaks, 
			boolean acceptingSpaceAfterSegment, boolean acceptingMultipleInterchanges) {
		EdiParserSettings settings = new EdiParserSettings();
		settings.setAcceptingLineBreaks(acceptingLineBreaks);
		settings.setAcceptingSpaceAfterSegment(acceptingSpaceAfterSegment);
		settings.setAcceptingMultipleInterchanges(acceptingMultipleInterchanges);
		return new EdifactParser(settings);
	}

	private static Interchange getSingleInterchange(ParsingResult result) {
		assertEquals(1, result.getInterchanges().size());
		return result.getInterchanges().get(0);
	}
	
	private static void verifyIFTDGN_1(String text, Interchange interchange, int offset, int lengthFactor) throws IOException {
		// verify message
		List<Message> messages = interchange.getMessages();
		assertEquals(1, messages.size());
		Message message = messages.get(0);
		assertEquals("IFTDGN", message.getType());
		assertEquals("D", message.getVersion());
		assertEquals("03A", message.getRelease());
		List<SegmentGroupItem> messageComponents = message.getChildren();
		assertEquals(11, messageComponents.size());
		
		// verify offsets
		assertEquals(offset + 0, interchange.getStartOffset());
		Segment icHeader = (Segment) interchange.getChild(0);
		assertEquals(offset + 0, icHeader.getStartOffset());
		Composite icH1 = (Composite) icHeader.getChild(0);
		assertTrue(icH1.getSegment() == icHeader);
		assertEquals(offset + 4, icH1.getStartOffset());
		assertEquals(offset + 4, icH1.getChild(0).getStartOffset());
		assertEquals(offset + 8, icH1.getChild(0).getEndOffset());
		assertEquals(offset + 10, icH1.getEndOffset());
		assertEquals(offset + 38, icHeader.getEndOffset());
		assertEquals(offset + 38, message.getStartOffset());
		SegmentGroupItem msgHeader = message.getChild(0);
		assertEquals(offset + 38, msgHeader.getStartOffset());
		assertEquals(offset + 38 + 42, msgHeader.getEndOffset());
		InterchangeItem icTrailer = interchange.getChild(interchange.getChildCount() - 1);
		String normalizedText = normalizeText(text);
		assertEquals(offset + normalizedText.length() / lengthFactor, icTrailer.getEndOffset());
		assertEquals(offset + normalizedText.length() / lengthFactor, interchange.getEndOffset());
	}

	private static String normalizeText(String text) throws IOException {
		StringBuilder result = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(text));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line.trim());
			}
			return result.toString();
		} finally {
			reader.close();
		}
	}
	
}
