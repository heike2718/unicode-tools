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
 * UTF8CodepointTest
 */
public class UTF8CodepointTest {

	@Test
	void should_OneArgumenConstructorThrowException_when_ArgumentNull() {

		try {
			new UTF8Codepoint(null);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("codePoints must not be null", e.getMessage());
		}
	}

	@Test
	void should_OneArgumentConstructorCreateWithDefaultSeparationChar_when_ArgumentIsNotNull() {

		// Act
		UTF8Codepoint result = new UTF8Codepoint("0043 0300");

		// Assert
		assertEquals("0043 0300", result.getCodePoints());
		assertEquals(' ', result.getSeparationChar());

		assertEquals("C̀", result.utf8());
		assertEquals("UTF8Codepoint [utf8=C̀, codePoints=0043 0300]", result.toString());
		assertEquals(2005686813, result.hashCode());
	}

	@Test
	void should_OneArgumentConstructorCreateWithGivenSeparationChar_when_ArgumentIsNotNull() {

		// Act
		UTF8Codepoint result = new UTF8Codepoint("0043,0300", ',');

		// Assert
		assertEquals("0043,0300", result.getCodePoints());
		assertEquals(',', result.getSeparationChar());
	}

	@Test
	void should_UTF8CodePointsBeEqual_when_BothAttributesAreEqual() {

		// Arrange
		UTF8Codepoint codePoint1 = new UTF8Codepoint("0043 0300");
		UTF8Codepoint codePoint2 = new UTF8Codepoint("0043 0300");

		// Assert
		assertEquals(codePoint1, codePoint2);
	}

	@Test
	void should_EqualsBeReflexive_when() {

		// Arrange
		UTF8Codepoint codePoint = new UTF8Codepoint("0043 0300");

		// Assert
		assertEquals(codePoint, codePoint);
	}

	@Test
	void should_UTF8CodePointsNotBeEqual_when_DifferentSeparationChars() {

		// Arrange
		UTF8Codepoint codePoint1 = new UTF8Codepoint("0043 0300");
		UTF8Codepoint codePoint2 = new UTF8Codepoint("0043 0300", ',');

		// Assert
		assertFalse(codePoint1.equals(codePoint2));
	}

	@Test
	void should_UTF8CodePointsNotBeEqual_when_DifferentCodePoints() {

		// Arrange
		UTF8Codepoint codePoint1 = new UTF8Codepoint("0043 0300");
		UTF8Codepoint codePoint2 = new UTF8Codepoint("0043");

		// Assert
		assertFalse(codePoint1.equals(codePoint2));
	}

	@Test
	void should_UTF8CodePointNotEqualNull() {

		// Act
		UTF8Codepoint codePoint = new UTF8Codepoint("0043 0300");

		// Assert
		assertFalse(codePoint.equals(null));
	}

	@Test
	void should_UTF8CodePointNotEqualString() {

		// Act
		UTF8Codepoint codePoint = new UTF8Codepoint("0043 0300");

		// Assert
		assertFalse(codePoint.equals("0043 0300"));
	}

}
