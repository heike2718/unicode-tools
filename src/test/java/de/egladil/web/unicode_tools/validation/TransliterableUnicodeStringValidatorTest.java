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

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.egladil.web.unicode_tools.annotations.TransliterableUnicodeString;

/**
 * TransliterableUnicodeStringValidatorTest
 */
public class TransliterableUnicodeStringValidatorTest {

	class TestStringProvider {

		@TransliterableUnicodeString
		private final String name;

		/**
		 * TestStringProvider
		 */
		public TestStringProvider(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	@BeforeAll
	static void createValidator() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@AfterAll
	static void tearDown() {
		validatorFactory.close();
	}

	@Test
	void should_LetPassNull() {

		// Act
		Set<ConstraintViolation<TestStringProvider>> violations = validator
				.validate(new TestStringProvider(null));

		// Assert
		assertEquals(0, violations.size());

	}

	@Test
	void should_LetPassBlank() {

		// Act
		Set<ConstraintViolation<TestStringProvider>> violations = validator
				.validate(new TestStringProvider("   "));

		// Assert
		assertEquals(0, violations.size());

	}

	@Test
	void should_NotLetPassSeveralCyrillicAndGreekChars_when_DefaultImplementation() {

		// Arrange
		String testString = "Diэs ist ein предложение mίt unerlaubten kyrillήschen und gρieχischen Zeichen.";

		// Act
		Set<ConstraintViolation<TestStringProvider>> violations = validator
				.validate(new TestStringProvider(testString));

		// Assert
		assertEquals(1, violations.size());

		ConstraintViolation<TestStringProvider> violation = violations.iterator().next();
		Object invalidValue = violation.getInvalidValue();
		assertEquals(testString, invalidValue);
		assertEquals("enthält ungültige Zeichen: р,ρ,χ,э,ή,ί,д,е,ж,и,л,н,о,п", violation.getMessage());
	}
}
