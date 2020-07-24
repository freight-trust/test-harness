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

import org.databene.edifatto.model.Component;
import org.databene.edifatto.model.Composite;
import org.databene.edifatto.model.Segment;
import org.junit.Test;

/**
 * Tests the {@link SegmentFormatter}.
 * Created: 15.08.2014 11:32:59
 * @since 2.0.0
 * @author Volker Bergmann
 */

public class SegmentFormatterTest {

	@Test
	public void testStandardSegemnt() {
		Segment segment = new Segment(null, "XYZ", null, 0, null, EdiFormatSymbols.EDIFACT);
		Composite composite = new Composite(null, segment, null);
		segment.addChild(composite);
		Component cx = new Component("x", null, segment, null);
		composite.addChild(cx);
		Component cy = new Component("y", null, segment, null);
		composite.addChild(cy);
		Component cz = new Component("z", null, segment, null);
		composite.addChild(cz);
		assertEquals("XYZ+x:y:z'", new SegmentFormatter(EdiFormatSymbols.EDIFACT).format(segment));
	}

	@Test
	public void testUNASegment() {
		Segment unaSegment = new Segment(null, "UNA", null, 0, null, EdiFormatSymbols.EDIFACT);
		unaSegment.addChild(new Component(":+.? '", null, unaSegment, null));
		assertEquals("UNA:+.? '", new SegmentFormatter(EdiFormatSymbols.EDIFACT).format(unaSegment));
	}

}
