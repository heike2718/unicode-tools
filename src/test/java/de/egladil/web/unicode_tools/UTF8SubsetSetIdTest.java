//=====================================================
// Projekt: unicode-tools
// MIT License
//
// Copyright (c) 2020 Heike Winkelvoß
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//=====================================================

package de.egladil.web.unicode_tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

/**
 * UTF8SubsetSetIdTest
 */
public class UTF8SubsetSetIdTest {

	@Test
	void should_ConstructorThrowException_when_ArgumentNull() {

		try {
			new UTF8SubsetSetId(null);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be blank", e.getMessage());
		}
	}

	@Test
	void should_ConstructorThrowException_when_ArgumentBlank() {

		try {
			new UTF8SubsetSetId("  ");
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be blank", e.getMessage());
		}
	}

	@Test
	void should_EqualsBeReflexive() {

		// Arrange
		UTF8SubsetSetId object1 = new UTF8SubsetSetId("Horst");

		// Assert
		assertEquals(object1, object1);
		assertEquals(69913579, object1.hashCode());
	}

	@Test
	void should_NameDetermineEquals() {

		// Arrange
		UTF8SubsetSetId object1 = new UTF8SubsetSetId("Horst");
		UTF8SubsetSetId object2 = new UTF8SubsetSetId("Horst");

		// Assert
		assertEquals(object1, object2);
		assertFalse(object1.equals(null));
		assertFalse(object1.equals("Horst"));

		assertEquals(object1.hashCode(), object2.hashCode());
	}

	@Test
	void should_FunctionAsExcepted() {

		// Arrange
		UTF8SubsetSetId object1 = new UTF8SubsetSetId("Horst");

		// Assert
		assertEquals("Horst", object1.name());
		assertEquals("Horst", object1.toString());
	}
}
