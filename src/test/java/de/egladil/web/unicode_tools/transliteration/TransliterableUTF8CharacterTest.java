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

package de.egladil.web.unicode_tools.transliteration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import de.egladil.web.unicode_tools.UTF8Codepoint;
import de.egladil.web.unicode_tools.transliteration.TransliterableUTF8Character;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacter;

/**
 * TransliterableUTF8CharacterTest
 */
public class TransliterableUTF8CharacterTest {

	@Test
	void should_ConstructorThrowException_when_ArgumentNull() {

		try {
			new TransliterableUTF8Character(null);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("provider must not be null", e.getMessage());
		}

	}

	@Test
	void should_ConstructorThrowException_when_ArgumentCodePoinstNull() {

		// Arrange
		MappableCharacter provider = new MappableCharacter() {

			@Override
			public char getSeparationChar() {
				return ' ';
			}

			@Override
			public String getMapping() {
				return null;
			}

			@Override
			public String getCodePoint() {
				return null;
			}
		};

		try {
			new TransliterableUTF8Character(provider);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("codePoints must not be null", e.getMessage());
		}

	}

	@Test
	void should_ConstructorThrowException_when_ArgumentMappingNull() {

		// Arrange
		MappableCharacter provider = new MappableCharacter() {

			@Override
			public char getSeparationChar() {
				return ' ';
			}

			@Override
			public String getMapping() {
				return null;
			}

			@Override
			public String getCodePoint() {
				return "Horst";
			}
		};

		try {
			new TransliterableUTF8Character(provider);
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("codePoints must not be null", e.getMessage());
		}

	}

	@Test
	void should_FunctionAsExpected_when_InitializedPropertliy() {
		// Arrange
		MappableCharacter provider = new MappableCharacter() {

			@Override
			public char getSeparationChar() {
				return ' ';
			}

			@Override
			public String getMapping() {
				return "0043";
			}

			@Override
			public String getCodePoint() {
				return "0043 0300";
			}
		};

		UTF8Codepoint expectedOriginal = new UTF8Codepoint("0043 0300");
		UTF8Codepoint expectedTransliterated = new UTF8Codepoint("0043");

		// Act
		TransliterableUTF8Character result = new TransliterableUTF8Character(provider);

		// Assert
		assertEquals(expectedOriginal, result.getOriginalCodepoint());
		assertEquals(expectedTransliterated, result.getTransliteradedCodepoint());

		assertEquals("C̀", result.asUtf8());
		assertEquals("C", result.transliterated());

	}
}
