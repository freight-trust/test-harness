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
import java.io.FileOutputStream;
import java.io.IOException;

import org.databene.commons.FileUtil;
import org.databene.commons.IOUtil;
import org.databene.edifatto.model.Interchange;
import org.junit.Test;

/**
 * Tests the EdiWriter.
 * Created: 18.10.2013 16:25:32
 * @since 1.0
 * @author Volker Bergmann
 */

public class EdiWriterTest {
	
	private static final String SOURCE_FILE = "IFTDGN_1.edi";
	private static final String TARGET_FILE = "target" + File.separator + EdiWriterTest.class.getSimpleName() + ".edi";

	@Test
	public void test() throws IOException {
		Interchange interchange = Edifatto.parseEdiFile(SOURCE_FILE);
		EdiWriter ediWriter = new EdiWriter("\n");
		ediWriter.writeInterchange(interchange, new FileOutputStream(TARGET_FILE));
		assertEquals(IOUtil.getContentOfURI(SOURCE_FILE), IOUtil.getContentOfURI(TARGET_FILE));
		FileUtil.deleteIfExists(new File(TARGET_FILE));
	}
	
}
