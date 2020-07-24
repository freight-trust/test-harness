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

import java.io.FileOutputStream;

import org.databene.edifatto.definition.MessageDefinition;
import org.databene.edifatto.model.EdiProtocol;
import org.databene.edifatto.parser.EdiXsdParser;
import org.databene.edifatto.util.EdiDefDotGraphModel;
import org.databene.formats.dot.DotWriter;
import org.junit.Test;

/**
 * Tests the {@link DefinitionUtil} class.
 * Created: 26.05.2014 07:22:06
 * @since 1.2
 * @author Volker Bergmann
 */

public class DefinitionUtilTest extends AbstractEdiTest {
	
	@Test
	public void test() throws Exception {
		MessageDefinition message = EdiXsdParser.parseSchema(IFTDGN_XSD, EdiProtocol.EDIFACT, "IFTDGN", "D", "03A");
		EdiDefDotGraphModel dotModel = new EdiDefDotGraphModel(message);
		DotWriter.persist(dotModel, new FileOutputStream("target/" + IFTDGN + ".dot"));
	}
	
}
