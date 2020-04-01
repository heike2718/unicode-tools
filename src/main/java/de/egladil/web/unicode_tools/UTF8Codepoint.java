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

import org.apache.commons.text.translate.UnicodeUnescaper;

/**
 * UTF8Codepoint is a ValueObject that codes a Charset codepoint with respect to
 * the UTF-8 charset. The default separation char between the sequence of 4digit
 * hexadecimal numbers coding a UnicodeCharacter, is a Blank.
 */
public class UTF8Codepoint {

	private static char DEFAULT_SEPARATION_CHAR = ' ';

	private final char separationChar;

	private String codePoints;

	/**
	 * Instanciates a UTF8Codepoint with Blank as separationChar.
	 */
	public UTF8Codepoint(String codePoints) {
		this(codePoints, DEFAULT_SEPARATION_CHAR);
	}

	/**
	 * Instanciates a UTF8Codepoint with a custom separationChar.
	 */
	public UTF8Codepoint(String codePoints, char separationChar) {
		this.setCodePoints(codePoints);
		this.separationChar = separationChar;
	}

	public char getSeparationChar() {
		return separationChar;
	}

	public String getCodePoints() {
		return codePoints;
	}

	/**
	 * Gets the UTF-8 String of this codepoint.
	 *
	 * @return String
	 */
	public String utf8() {
		final UnicodeUnescaper unicodeUnescaper = new UnicodeUnescaper();
		final CodePointsToUnicodeMapper codePointMapper = new CodePointsToUnicodeMapper(this.separationChar);

		String unicodeChars = codePointMapper.apply(codePoints);
		return unicodeUnescaper.translate(unicodeChars);
	}

	private void setCodePoints(String codePoints) {
		if (codePoints == null) {
			throw new IllegalArgumentException("codePoints must not be null");
		}
		this.codePoints = codePoints;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codePoints == null) ? 0 : codePoints.hashCode());
		result = prime * result + separationChar;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UTF8Codepoint other = (UTF8Codepoint) obj;
		if (codePoints == null) {
			if (other.codePoints != null) {
				return false;
			}
		} else if (!codePoints.equals(other.codePoints)) {
			return false;
		}
		if (separationChar != other.separationChar) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UTF8Codepoint [utf8=" + utf8() + ", codePoints=" + codePoints + "]";
	}

}
