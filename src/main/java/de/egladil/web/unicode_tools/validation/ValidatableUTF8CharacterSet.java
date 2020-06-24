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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.egladil.web.unicode_tools.UTF8Codepoint;
import de.egladil.web.unicode_tools.UTF8SubsetSetId;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacter;
import de.egladil.web.unicode_tools.xml.mapping.MappableCharacterSet;

/**
 * ValidatableUTF8CharacterSet is a subset of UTF-8 that contains items being
 * cosidered as valid.
 */
public class ValidatableUTF8CharacterSet implements ValidationProvider {

	private final UTF8SubsetSetId name;

	private List<UTF8Codepoint> items;

	/**
	 * Factory method for mapping a DefaultValidatableCharacterSet.
	 *
	 * @param characterSet
	 * @return ValidatableUTF8CharacterSet
	 */
	public static ValidatableUTF8CharacterSet from(MappableCharacterSet characterSet) {

		if (characterSet == null) {
			throw new IllegalArgumentException("characterSet must not be null");
		}

		if (characterSet.getItems() == null) {
			throw new IllegalArgumentException("characterSet.items must not be null");
		}

		ValidatableUTF8CharacterSet result = new ValidatableUTF8CharacterSet(
				new UTF8SubsetSetId(characterSet.getName()));

		List<MappableCharacter> theValidatableItems = characterSet.getItems();

		List<UTF8Codepoint> mappedItems = theValidatableItems.stream()
				.map(item -> new UTF8Codepoint(item.getCodePoint(), item.getSeparationChar()))
				.collect(Collectors.toList());

		result.items = mappedItems;

		return result;

	}

	/**
	 * ValidatableUTF8CharacterSet
	 */
	ValidatableUTF8CharacterSet(UTF8SubsetSetId name) {
		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}

		this.name = name;
		this.items = new ArrayList<>();
	}

	@Override
	public boolean isPrintableCharacterValid(String givenPrintableCharacter) {

		if (givenPrintableCharacter == null || givenPrintableCharacter.isEmpty()) {
			return true;
		}

		return items.stream().map(item -> item.utf8()).filter(ch -> givenPrintableCharacter.equals(ch)).findAny()
				.isPresent();
	}

	@Override
	public boolean isUTF8CodepointValid(UTF8Codepoint codePoint) {
		return this.items.contains(codePoint);
	}

	@Override
	public String name() {
		return this.name.name();
	}

	@Override
	public int size() {
		return this.items.size();
	}
}
