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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

import de.egladil.web.unicode_tools.UTF8SubsetSetId;
import de.egladil.web.unicode_tools.internal.TransliterableUTF8CharacterSetFactory;
import de.egladil.web.unicode_tools.validation.JAXBContextProvider;
import de.egladil.web.unicode_tools.xml.DefaultCharacter;
import de.egladil.web.unicode_tools.xml.DefaultCharacterSet;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacter;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacterSet;

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
		UTF8SubsetSetId name = new UTF8SubsetSetId("Horst");

		// Act
		TransliterableUTF8CharacterSet result = new TransliterableUTF8CharacterSet(name);

		// Assert
		assertEquals(0, result.size());
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
		MappableCharacterSet provider = new MappableCharacterSet() {

			@Override
			public String getName() {
				return null;
			}

			@Override
			public List<MappableCharacter> getItems() {
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
		MappableCharacterSet provider = new MappableCharacterSet() {

			@Override
			public String getName() {
				return "Horst";
			}

			@Override
			public List<MappableCharacter> getItems() {
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
		MappableCharacterSet provider = new MappableCharacterSet() {

			@Override
			public String getName() {
				return "Horst";
			}

			@Override
			public List<MappableCharacter> getItems() {
				return new ArrayList<>();
			}
		};

		// Act
		TransliterableUTF8CharacterSet result = TransliterableUTF8CharacterSet.from(provider);

		// Assert
		assertEquals(0, result.size());

	}

	@Test
	void should_FactoryMethodWithOneArgumentCreateTransliterations_when_ItemsPresent() throws Exception {

		// Arrange
		MappableCharacterSet provider = createProviderFromXml("/veryShortCharset.xml");

		// Act
		TransliterableUTF8CharacterSet result = TransliterableUTF8CharacterSet.from(provider);

		// Assert
		assertEquals("C", result.printableTransliteratedCharacter("C̀"));
	}

	@Test
	void should_FactoryMethodWithCustomTransliterationsUseIt() throws Exception {

		// Arrange
		MappableCharacterSet provider = createProviderFromXml("/charsetWitCustomMapping.xml");

		final Map<String, String> transliterations = new HashMap<>();
		provider.getItems().stream().forEach(item -> {
			TransliterableUTF8Character transliterableChar = new TransliterableUTF8Character((DefaultCharacter) item);
			transliterations.put(transliterableChar.asUtf8(), transliterableChar.transliterated());
		});

		// Act
		TransliterableUTF8CharacterSet result = TransliterableUTF8CharacterSet
				.withCustomTransliterations(createProviderFromXml("/veryShortCharset.xml"), transliterations);

		// Assert
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
		MappableCharacterSet provider = createProviderFromXml("/veryShortCharset.xml");
		final Map<String, String> transliterations = new HashMap<>();
		provider.getItems().stream().forEach(item -> {
			TransliterableUTF8Character transliterableChar = new TransliterableUTF8Character((DefaultCharacter) item);

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
			assertEquals("transliteration for UTF8Codepoint [utf8=C̀, codePoints=0043 0300] is missing",
					e.getMessage());
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

	@Test
	void should_PrintableTransliteratedCharacterReturnM_when_TwoCharsMWuthAcent() throws Exception {

		// Act
		TransliterableUTF8CharacterSet set = new TransliterableUTF8CharacterSetFactory().createCharacterSet();

		// Assert
		assertEquals("M", set.printableTransliteratedCharacter("M̂"));

	}

	@Test
	void should_TransliterateWork_when_ComplicatedText() throws Exception {

		// Arrange
		TransliterableUTF8CharacterSet set = new TransliterableUTF8CharacterSetFactory().createCharacterSet();
		String text = "ŝM̂sC̨̆Ö";
		String expected = "SMSCOE";

		// Act
		String transliterated = set.transliterate(text);

		// Assert
		assertEquals(expected, transliterated);




	}


	/**
	 * @param classPathResource
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 */
	private MappableCharacterSet createProviderFromXml(final String classPathResource)
			throws JAXBException, IOException {
		MappableCharacterSet provider = null;

		try (InputStream in = getClass().getResourceAsStream(classPathResource)) {

			Unmarshaller unmarshaller = JAXBContextProvider.getJACBContext().createUnmarshaller();

			Object obj = unmarshaller.unmarshal(in);

			if (!(obj instanceof DefaultCharacterSet)) {
				throw new IllegalArgumentException("provided xml is not valid for DefaultCharacterSet");
			}

			provider = (MappableCharacterSet) obj;
		}
		return provider;
	}
}
