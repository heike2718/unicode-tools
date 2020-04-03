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

package de.egladil.web.unicode_tools.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

import de.egladil.web.unicode_tools.UTF8Codepoint;
import de.egladil.web.unicode_tools.UTF8SubsetSetName;
import de.egladil.web.unicode_tools.xml.DefaultCharacterSet;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacter;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacterSet;

/**
 * ValidatableUTF8CharacterSetTest
 */
public class ValidatableUTF8CharacterSetTest {

	@Test
	void should_ConstructorWithNameNullThrowException() {

		try {
			new ValidatableUTF8CharacterSet(null);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be null", e.getMessage());
		}

	}

	@Test
	void should_FactoryMethodWithMappableCharacterSetThrowException_when_ParameterNull() {

		try {
			ValidatableUTF8CharacterSet.from(null);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("characterSet must not be null", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithMappableCharacterSetThrowException_when_NameNull() {

		// Arrange
		MappableCharacterSet charset = new MappableCharacterSet() {

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
			ValidatableUTF8CharacterSet.from(charset);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be blank", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithMappableCharacterSetThrowException_when_NameBlank() {

		// Arrange
		MappableCharacterSet charset = new MappableCharacterSet() {

			@Override
			public String getName() {
				return "  ";
			}

			@Override
			public List<MappableCharacter> getItems() {
				return new ArrayList<>();
			}
		};

		try {
			ValidatableUTF8CharacterSet.from(charset);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("name must not be blank", e.getMessage());
		}
	}

	@Test
	void should_FactoryMethodWithMappableCharacterSetThrowException_when_MappableItemsNull() {

		// Arrange
		MappableCharacterSet charset = new MappableCharacterSet() {

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
			ValidatableUTF8CharacterSet.from(charset);
			fail("no IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("characterSet.items must not be null", e.getMessage());
		}
	}

	@Test
	void should_ConstructorWithNameCreateEmptySet() {

		// Arrange
		UTF8SubsetSetName name = new UTF8SubsetSetName("Horst");

		// Act
		ValidatableUTF8CharacterSet result = new ValidatableUTF8CharacterSet(name);

		// Assert
		assertEquals(0, result.size());
		assertFalse(result.isPrintableCharacterValid("â"));
		assertFalse(result.isUTF8CodepointValid(new UTF8Codepoint("0043")));
		assertEquals("Horst", result.name());
	}

	@Test
	void should_FactoryMethodWithOneArgumentCreateTransliterations_when_ItemsPresent() throws Exception {

		// Arrange
		MappableCharacterSet provider = createProviderFromXml("/veryShortCharset.xml");

		// Act
		ValidatableUTF8CharacterSet result = ValidatableUTF8CharacterSet.from(provider);

		// Assert
		assertTrue(result.isPrintableCharacterValid("C̀"));
		assertTrue(result.isUTF8CodepointValid(new UTF8Codepoint("0043 0300")));
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

			provider = (DefaultCharacterSet) obj;
		}
		return provider;
	}

	@Test
	void shouldIsPrintableCharacterValidReturnTrue_when_ParameterNull() {

		assertTrue(new ValidatableUTF8CharacterSet(new UTF8SubsetSetName("Horst")).isPrintableCharacterValid(null));
	}

	@Test
	void shouldIsPrintableCharacterValidReturnTrue_when_ParameterEmpty() {

		assertTrue(new ValidatableUTF8CharacterSet(new UTF8SubsetSetName("Horst")).isPrintableCharacterValid(""));
	}

	@Test
	void shouldIsUTF8CodepointValidReturnFalse_when_ParameterEmpty() {

		assertFalse(new ValidatableUTF8CharacterSet(new UTF8SubsetSetName("Horst")).isUTF8CodepointValid(null));
	}
}
