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

/**
 * CharacterTransliterationProvider provides some sort of transliteration for some
 * Character into another Character.
 */
public interface CharacterTransliterationProvider {

	/**
	 * Returns the transliteration as printable String of length 1 to which the
	 * givenPrintableCharacter will be mapped according to the mapping that is
	 * defined by some CharcterSet.
	 *
	 * @param givenPrintableCharacter String
	 * @return String or null, if givenPrintableCharacter is not contained.
	 */
	String printableTransliteratedCharacter(String givenPrintableCharacter);

	/**
	 * Gibt den Namen zurück fürs Logging.
	 *
	 * @return String
	 */
	String name();
}
