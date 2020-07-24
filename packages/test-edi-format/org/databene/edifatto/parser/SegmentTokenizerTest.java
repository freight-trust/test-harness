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

import java.text.ParsePosition;
import java.util.List;

import org.databene.edifatto.AbstractEdiTest;
import org.databene.edifatto.EdiFormatSymbols;
import org.databene.edifatto.ParsingResult;
import org.databene.edifatto.model.Segment;
import org.databene.edifatto.parser.SegmentTokenizer;
import org.junit.Test;

/**
 * Tests the {@link SegmentTokenizer}.
 * Created: 10.10.2013 09:54:50
 * @since 1.0
 * @author Volker Bergmann
 */

public class SegmentTokenizerTest extends AbstractEdiTest {
	
	@Test
	public void testParseStandardSegment() throws Exception {
		String text = "X**~";
		SegmentTokenizer parser = new SegmentTokenizer(EdiFormatSymbols.X12);
		Segment segment = parser.parseStandardSegment(text, new ParsePosition(0), new ParsePosition(0), new ParsingResult());
		System.out.println(segment);
		assertEquals(2, segment.getChildCount());
	}
	
	@Test
	public void testCompleteFile() throws Exception {
		String text = readTextFileRemovingLineFeeds("IFTDGN_1.edi");
		SegmentTokenizer parser = new SegmentTokenizer(EdiFormatSymbols.EDIFACT);
		List<Segment> segments = parser.tokenize(text, new ParsingResult());
		for (Segment segment : segments)
			System.out.println(segment);
		assertEquals(57, segments.size());
	}
	
}
