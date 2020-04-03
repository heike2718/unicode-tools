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
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.unicode_tools.exceptions.UnicodeToolsException;
import de.egladil.web.unicode_tools.xml.DefaultCharacterSet;

/**
 * AbstractUnicodeSubsetValidator
 */
public abstract class AbstractUnicodeSubsetValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

	private String messageTemplate = "de.egladil.web.unicode_tools.invalidChars";

	private final ResourceBundle validationMessages = ResourceBundle.getBundle("UnicodeToolsValidationMessages",
			Locale.GERMAN);

	@Override
	public boolean isValid(T value, ConstraintValidatorContext context) {

		if (value == null) {

			return true;
		}

		if (!(value instanceof String)) {

			return false;
		}

		String strValue = (String) value;

		if (StringUtils.isBlank(strValue)) {

			return true;
		}

		ValidationProvider validationProvider = getValidationProvider();

		Set<String> unallowedSubstrings = new HashSet<>();

		for (int i = 0; i < strValue.length(); i++) {

			String letter = strValue.charAt(i) + "";
			if (!validationProvider.isPrintableCharacterValid(letter)) {
				unallowedSubstrings.add(letter);
			}
		}

		if (!unallowedSubstrings.isEmpty()) {

			String invalidChars = StringUtils.join(unallowedSubstrings, ",");
			String valMessage = validationMessages.getString(messageTemplate);
			String message = MessageFormat.format(valMessage, new Object[] { invalidChars });
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(message).addConstraintViolation();

			return false;
		}

		return true;
	}

	private ValidationProvider getValidationProvider() {

		String whitelistLocation = getWhitelistClasspathLocation();

		try (InputStream in = getClass().getResourceAsStream(whitelistLocation)) {

			Unmarshaller unmarshaller = JAXBContextProvider.getJACBContext().createUnmarshaller();

			Object obj = unmarshaller.unmarshal(in);

			if (!(obj instanceof DefaultCharacterSet)) {
				throw new IllegalArgumentException("provided xml is not valid for DefaultCharacterSet");
			}

			DefaultCharacterSet defaultCharSet = (DefaultCharacterSet) obj;

			return ValidatableUTF8CharacterSet.from(defaultCharSet);

		} catch (IOException e) {
			throw new UnicodeToolsException("resource " + whitelistLocation + " is not present");
		} catch (JAXBException e) {
			throw new UnicodeToolsException("could not unmarshall " + whitelistLocation + ": " + e.getMessage(), e);
		}
	}

	/**
	 * @return the classpath resource to the xml file containing the whitelist.
	 */
	protected abstract String getWhitelistClasspathLocation();
}
