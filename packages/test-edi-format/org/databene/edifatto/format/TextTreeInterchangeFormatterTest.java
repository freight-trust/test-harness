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
package org.databene.edifatto.format;

import org.databene.commons.SystemInfo;
import org.databene.edifatto.Edifatto;
import org.databene.edifatto.model.Interchange;
import org.junit.Test;

/**
 * Tests the {@link TextTreeInterchangeFormatter}.
 * Created: 19.06.2014 14:03:09
 * @since 1.2.1
 * @author Volker Bergmann
 */

public class TextTreeInterchangeFormatterTest {
	
	@Test
	public void testNonDetailed() throws Exception {
		Interchange interchange = Edifatto.parseEdiFile("IFTDGN_1.edi");
		String text = new TextTreeInterchangeFormatter(true, SystemInfo.getLineSeparator()).format(interchange);
		System.out.println(text);
	}
	
	@Test
	public void testDetailed() throws Exception {
		Interchange interchange = Edifatto.parseEdiFile("IFTDGN_1.edi");
		String text = new TextTreeInterchangeFormatter(false, SystemInfo.getLineSeparator()).format(interchange);
		System.out.println(text);
	}
	
}
