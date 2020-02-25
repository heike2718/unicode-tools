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

import de.egladil.web.unicode_tools.UnicodeSubset;
import de.egladil.web.unicode_tools.UnicodeToolsException;
import de.egladil.web.unicode_tools.annotations.DefaultUnicodeString;
import de.egladil.web.unicode_tools.impl.DefaultUnicodeSubsetBuilder;

/**
 * DefaultUnicodeStringValidator
 */
public class DefaultUnicodeStringValidator extends AbstractUnicodeSubsetValidator<DefaultUnicodeString, String> {

	private static final String UNICODE_WHITELIST_XML = "/unicodeWhitelist.xml";

	@Override
	protected UnicodeSubset getAllowedUnicodeSubset() {

		try (InputStream in = getClass().getResourceAsStream(UNICODE_WHITELIST_XML)) {

			return new DefaultUnicodeSubsetBuilder().build(in);

		} catch (IOException e) {
			throw new UnicodeToolsException("resource " + UNICODE_WHITELIST_XML + " is not present");
		} catch (JAXBException e) {
			throw new UnicodeToolsException("could not unmarshall " + UNICODE_WHITELIST_XML + ": " + e.getMessage(), e);
		}
	}

}
