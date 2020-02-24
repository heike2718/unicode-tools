// =====================================================
// Projekt: heike2718/unicode-tools
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
// =====================================================

package de.egladil.web.unicode_tools;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import de.egladil.web.unicode_tools.CodePointsToUnicodeMapper;

/**
 * CodePointsToUnicodeMapperTest
 *
 */
public class CodePointsToUnicodeMapperTest {

	@Test
	void should_TransformStringCorrectly_when_SeparatedByOneSpace() {

		// Arrange
		String string = "0054 0308";
		String expected = "\\u0054\\u0308";

		// Act
		String transformed = new CodePointsToUnicodeMapper().apply(string);

		// Assert
		assertEquals(expected, transformed);
	}

	@Test
	void should_TransformStringCorrectly_when_NonDefaultSeparationCharGiven() {

		// Arrange
		String string = "0054;0308";
		String expected = "\\u0054\\u0308";

		// Act
		String transformed = new CodePointsToUnicodeMapper(';').apply(string);

		// Assert
		assertEquals(expected, transformed);
	}

}
