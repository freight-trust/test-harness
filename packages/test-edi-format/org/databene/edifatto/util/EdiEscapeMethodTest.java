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
package org.databene.edifatto.util;

import static org.junit.Assert.*;

import java.util.List;

import org.databene.commons.CollectionUtil;
import org.databene.edifatto.EdiFormatSymbols;
import org.junit.Test;

/**
 * Tests the {@link EdiEscapeMethod}.
 * Created: 18.12.2013 08:21:48
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdiEscapeMethodTest {
	
	@Test
	public void testEmptyString() throws Exception {
		check("", "");
	}

	@Test
	public void testStringWithoutEscChar() throws Exception {
		check("Alice,Bob", "Alice,Bob");
	}

	@Test
	public void testStringWithEscapableChar() throws Exception {
		check("Alice?+Bob", "Alice+Bob");
		check("Alice?:Bob", "Alice:Bob");
		check("Alice?'Bob", "Alice'Bob");
		check("?+Alice?:Bob?'", "+Alice:Bob'");
	}

	@Test
	public void testStringWithEscapeChar() throws Exception {
		check("Alice??Bob", "Alice?Bob");
	}

	@Test
	public void testArrayWithoutEscChar() throws Exception {
		check("Alice:Bob", "Alice", "Bob");
	}

	@Test
	public void testArrayWithEscapableChar() throws Exception {
		check("?'Al?+i?+ce?':?'Bob?'", "'Al+i+ce'", "'Bob'");
	}

	private static void check(String expected, String... texts) throws Exception {
		EdiEscapeMethod method = new EdiEscapeMethod(EdiFormatSymbols.EDIFACT);
		List<String> list = CollectionUtil.toList(texts);
		Object actual = method.exec(list);
		assertEquals(expected, actual);
	}
	
}
