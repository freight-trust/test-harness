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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.databene.edifatto.AbstractEdiTest;
import org.databene.edifatto.Edifatto;
import org.databene.edifatto.ParsingResult;
import org.databene.edifatto.model.Interchange;
import org.databene.edifatto.model.Message;
import org.databene.edifatto.model.SegmentGroupItem;
import org.databene.edifatto.parser.X12Parser;
import org.junit.Test;

/**
 * Tests the {@link X12Parser}.
 * Created: 18.10.2013 12:25:07
 * @since 1.0
 * @author Volker Bergmann
 */

public class X12ParserTest extends AbstractEdiTest {

	@Test
	public void test() throws Exception {
		String text = readTextFileRemovingLineFeeds("300_5030.x12");
		ParsingResult result = new X12Parser().parse(text);
		List<Interchange> interchanges = result.getInterchanges();
		assertEquals(1, interchanges.size());
		Interchange interchange = interchanges.get(0);
		System.out.println(Edifatto.formatRecursively(interchange));
		List<Message> messages = interchange.getMessages();
		assertEquals(1, messages.size());
		Message message = messages.get(0);
		assertEquals("300", message.getType());
		assertEquals(null, message.getVersion());
		assertEquals(null, message.getRelease());
		List<SegmentGroupItem> messageComponents = message.getChildren();
		assertEquals(10, messageComponents.size());
	}
	
}
