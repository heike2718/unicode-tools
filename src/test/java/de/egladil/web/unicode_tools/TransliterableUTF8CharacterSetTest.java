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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

import de.egladil.web.unicode_tools.validation.JAXBContextProvider;
import de.egladil.web.unicode_tools.xml.DefaultTransliterableCharacter;
import de.egladil.web.unicode_tools.xml.DefaultTransliterableCharacterSet;

/**
 * TransliterableUTF8CharacterSetTest
 */
public class TransliterableUTF8CharacterSetTest {

	@Test
	void should_ConstructorWithNameNullThrowException() {

		try {
			new TransliterableUTF8CharacterSet(null);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be null", e.getMessage());
		}

	}

	@Test
	void should_ConstructorWithNameCreateEmptySet() {

		// Arrange
		TransliterableCharacterSetName name = new TransliterableCharacterSetName("Horst");

		// Act
		TransliterableUTF8CharacterSet result = new TransliterableUTF8CharacterSet(name);

		// Assert
		assertEquals(0, result.size());
		assertFalse(result.isPrintableCharacterValid("â"));
		assertFalse(result.isUTF8CodepointValid(new UTF8Codepoint("0043")));
		assertEquals("Horst", result.name());

	}

	@Test
	void should_FactoryMethodWithOneArgumentTrowException_when_ArgumentNull() {
		try {
			TransliterableUTF8CharacterSet.from(null);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("charSetProvider must not be null", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithOneArgumentTrowException_when_NameNull() {

		// Arrange
		TransliterableCharacterSet provider = new TransliterableCharacterSet() {

			@Override
			public String getName() {
				return null;
			}

			@Override
			public List<TransliterableCharacter> getItems() {
				return new ArrayList<>();
			}
		};

		try {
			TransliterableUTF8CharacterSet.from(provider);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be blank", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithOneArgumentTrowException_when_ItemsNull() {

		// Arrange
		TransliterableCharacterSet provider = new TransliterableCharacterSet() {

			@Override
			public String getName() {
				return "Horst";
			}

			@Override
			public List<TransliterableCharacter> getItems() {
				return null;
			}
		};

		try {
			TransliterableUTF8CharacterSet.from(provider);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("charSetProvider.items must not be null", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithOneArgumentCreateEmptySet_when_ProviderIsEmpty() {

		// Arrange
		TransliterableCharacterSet provider = new TransliterableCharacterSet() {

			@Override
			public String getName() {
				return "Horst";
			}

			@Override
			public List<TransliterableCharacter> getItems() {
				return new ArrayList<>();
			}
		};

		// Act
		TransliterableUTF8CharacterSet result = TransliterableUTF8CharacterSet.from(provider);

		// Assert
		assertEquals(0, result.size());
		assertFalse(result.isPrintableCharacterValid("â"));
		assertFalse(result.isUTF8CodepointValid(new UTF8Codepoint("0043")));
		assertFalse(result.isUTF8CodepointValid(null));

	}

	@Test
	void should_FactoryMethodWithOneArgumentCreateTransliterations_when_ItemsPresent() throws Exception {

		// Arrange
		TransliterableCharacterSet provider = createProviderFromXml("/veryShortCharset.xml");

		// Act
		TransliterableUTF8CharacterSet result = TransliterableUTF8CharacterSet.from(provider);

		// Assert
		assertTrue(result.isPrintableCharacterValid("C̀"));
		assertTrue(result.isUTF8CodepointValid(new UTF8Codepoint("0043 0300")));
		assertEquals("C", result.printableTransliteratedCharacter("C̀"));
	}

	@Test
	void should_FactoryMethodWithCustomTransliterationsUseIt() throws Exception {

		// Arrange
		TransliterableCharacterSet provider = createProviderFromXml("/charsetWitCustomMapping.xml");

		final Map<String, String> transliterations = new HashMap<>();
		provider.getItems().stream().forEach(item -> {
			TransliterableUTF8Character transliterableChar = new TransliterableUTF8Character(
					(DefaultTransliterableCharacter) item);
			transliterations.put(transliterableChar.asUtf8(), transliterableChar.transliterated());
		});

		// Act
		TransliterableUTF8CharacterSet result = TransliterableUTF8CharacterSet
				.withCustomTransliterations(createProviderFromXml("/veryShortCharset.xml"), transliterations);

		// Assert
		assertTrue(result.isPrintableCharacterValid("C̀"));
		assertTrue(result.isUTF8CodepointValid(new UTF8Codepoint("0043 0300")));
		assertEquals("(", result.printableTransliteratedCharacter("C̀"));
	}

	@Test
	void should_FactoryMethodWithCustomTransliterationsThrowException_when_MapNull() throws Exception {

		// Act
		try {
			TransliterableUTF8CharacterSet.withCustomTransliterations(createProviderFromXml("/veryShortCharset.xml"),
					null);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("transliterations must not be null", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithCustomTransliterationsThrowException_when_ThereIsNoTransliterationForSomeItem()
			throws Exception {

		// Arrange
		TransliterableCharacterSet provider = createProviderFromXml("/veryShortCharset.xml");
		final Map<String, String> transliterations = new HashMap<>();
		provider.getItems().stream().forEach(item -> {
			TransliterableUTF8Character transliterableChar = new TransliterableUTF8Character(
					(DefaultTransliterableCharacter) item);

			if (!transliterableChar.asUtf8().equals("C̀")) {
				transliterations.put(transliterableChar.asUtf8(), transliterableChar.transliterated());
			}
		});
		transliterations.put("Ö", "O");

		// Act
		try {
			TransliterableUTF8CharacterSet.withCustomTransliterations(provider, transliterations);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("transliteration for UTF8Codepoint [utf8=C̀, codePoints=0043 0300] is missing", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithCustomTransliterationsThrowException_when_ItemsAndTransliterationsSizesDiffer()
			throws Exception {

		// Act
		try {
			TransliterableUTF8CharacterSet.withCustomTransliterations(createProviderFromXml("/veryShortCharset.xml"),
					new HashMap<>());
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("items and transliterations need to be of same size", e.getMessage());
		}
	}

	/**
	 * @param classPathResource
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	private TransliterableCharacterSet createProviderFromXml(final String classPathResource)
			throws JAXBException, IOException {
		TransliterableCharacterSet provider = null;

		try (InputStream in = getClass().getResourceAsStream(classPathResource)) {

			Unmarshaller unmarshaller = JAXBContextProvider.getJACBContext().createUnmarshaller();

			Object obj = unmarshaller.unmarshal(in);

			if (!(obj instanceof DefaultTransliterableCharacterSet)) {
				throw new IllegalArgumentException("provided xml is not valid for DefaultTransliterableCharacterSet");
			}

			provider = (TransliterableCharacterSet) obj;
		}
		return provider;
	}
}
