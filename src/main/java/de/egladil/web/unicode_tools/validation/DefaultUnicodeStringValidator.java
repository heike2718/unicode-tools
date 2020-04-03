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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.egladil.web.unicode_tools.TransliterableUTF8CharacterSet;
import de.egladil.web.unicode_tools.annotations.DefaultUnicodeString;
import de.egladil.web.unicode_tools.exceptions.UnicodeToolsException;
import de.egladil.web.unicode_tools.xml.DefaultTransliterableCharacterSet;

/**
 * DefaultUnicodeStringValidator
 */
public class DefaultUnicodeStringValidator extends AbstractUnicodeSubsetValidator<DefaultUnicodeString, String> {

	private static final String UNICODE_WHITELIST_XML = "/defaultTransliterableCharacterSet.xml";

	@Override
	protected ValidationProvider getValidationProvider() {
		try (InputStream in = getClass().getResourceAsStream(UNICODE_WHITELIST_XML)) {

			Unmarshaller unmarshaller = JAXBContextProvider.getJACBContext().createUnmarshaller();

			Object obj = unmarshaller.unmarshal(in);

			if (!(obj instanceof DefaultTransliterableCharacterSet)) {
				throw new IllegalArgumentException("provided xml is not valid for DefaultTransliterableCharacterSet");
			}

			DefaultTransliterableCharacterSet defaultCharSet = (DefaultTransliterableCharacterSet) obj;

			return TransliterableUTF8CharacterSet.from(defaultCharSet);

		} catch (IOException e) {
			throw new UnicodeToolsException("resource " + UNICODE_WHITELIST_XML + " is not present");
		} catch (JAXBException e) {
			throw new UnicodeToolsException("could not unmarshall " + UNICODE_WHITELIST_XML + ": " + e.getMessage(), e);
		}
	}
}
