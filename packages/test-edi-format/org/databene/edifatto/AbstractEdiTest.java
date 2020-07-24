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

import java.io.IOException;

import org.databene.commons.IOUtil;
import org.databene.edifatto.util.EdiUtil;

/**
 * Parent class for Edifatto unit tests.
 * Created: 22.10.2013 13:07:47
 * @since 1.0
 * @author Volker Bergmann
 */

public abstract class AbstractEdiTest {
	
	public static final String IFTDGN = "D03A_IFTDGN";
	public static final String IFTDGN_XSD = "org/databene/edifatto/" + IFTDGN + ".xsd";
	
	public static String readTextFileRemovingLineFeeds(String uri) throws IOException {
		return EdiUtil.readTextFileRemovingLineFeeds(IOUtil.getInputStreamForURI(uri));
	}
	
}
