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

/**
 * TransliterableUTF8Character is an Object owning an original code points
 * wrapper and a code point wrapper as mappint to some other code points
 * wrapper.<br>
 * <br>
 * <strong>Example: </strong> originalCodepoint '004D 0306' coding a capital M
 * with acent breve is mapped to '004D' i.e. the ISO 8859-15 capial M.
 */
public class TransliterableUTF8Character {

	private UTF8Codepoint originalCodepoint;

	private UTF8Codepoint transliteradedCodepoint;

	/**
	 * TransliterableUTF8Character from a TransliterableCharacter
	 *
	 * @param provider TransliterableCharacter
	 */
	public TransliterableUTF8Character(TransliterableCharacter provider) {

		if (provider == null) {
			throw new IllegalArgumentException("provider must not be null");
		}

		this.originalCodepoint = new UTF8Codepoint(provider.getCodePoint(), provider.getSeparationChar());
		this.transliteradedCodepoint = new UTF8Codepoint(provider.getMapping(), provider.getSeparationChar());
	}

	public UTF8Codepoint getOriginalCodepoint() {
		return originalCodepoint;
	}

	public UTF8Codepoint getTransliteradedCodepoint() {
		return transliteradedCodepoint;
	}

	/**
	 *
	 * @return String as printable UTF-8 String of length 1.
	 */
	public String asUtf8() {
		return this.originalCodepoint.utf8();
	}

	/**
	 *
	 * @return String the transliteration as String of length 1.
	 */
	public String transliterated() {
		return this.transliteradedCodepoint.utf8();
	}
}
