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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.egladil.web.unicode_tools.mapping.CharacterTransliterationProvider;
import de.egladil.web.unicode_tools.validation.ValidationProvider;

/**
 * TransliterableUTF8CharacterSet is a collection of
 * TransliterableUTF8Characters. Two TransliterableUTF8CharacterSet are equal
 * when their names are equal. It provides a transliteration from one printable
 * character into another printable character.
 */
public class TransliterableUTF8CharacterSet implements CharacterTransliterationProvider, ValidationProvider {

	private final TransliterableCharacterSetName name;

	private List<TransliterableUTF8Character> items;

	private Map<String, String> transliterations;

	/**
	 * TransliterableUTF8CharacterSet
	 *
	 * @param name String
	 */
	TransliterableUTF8CharacterSet(TransliterableCharacterSetName name) {

		if (name == null) {
			throw new IllegalArgumentException("name must not be null");
		}

		this.name = name;
		this.items = new ArrayList<>();
		this.transliterations = new HashMap<>();
	}

	/**
	 * Factory method that creates a TransliterableUTF8CharacterSet from some given
	 * TransliterableCharacterSetProvider by using the transliteration provided by
	 * the given TransliterableCharacterSetProvider.
	 *
	 * @param charSetProvider TransliterableCharacterSetProvider
	 * @return TransliterableUTF8CharacterSet
	 */
	public static TransliterableUTF8CharacterSet from(TransliterableCharacterSetProvider charSetProvider) {

		if (charSetProvider == null) {
			throw new IllegalArgumentException("charSetProvider must not be null");
		}

		if (charSetProvider.getItems() == null) {
			throw new IllegalArgumentException("charSetProvider.items must not be null");
		}

		TransliterableUTF8CharacterSet result = new TransliterableUTF8CharacterSet(
				new TransliterableCharacterSetName(charSetProvider.getName()));

		List<TransliterableCharacterProvider> transliterableChars = charSetProvider.getItems();
		final List<TransliterableUTF8Character> items = transliterableChars.stream()
				.map(ch -> new TransliterableUTF8Character(ch)).collect(Collectors.toList());

		return withItems(result, items);
	}

	/**
	 * Factory method that creates a new TransliterableUTF8CharacterSet with the
	 * same name as the given startingSet and the List of
	 * TransliterableUTF8Characters. The transliterationMap is calculated from the
	 * given items.
	 *
	 * @param startingSetTransliterableUTF8CharacterSet
	 * @param items                                     List
	 * @return TransliterableUTF8CharacterSet
	 */
	private static TransliterableUTF8CharacterSet withItems(final TransliterableUTF8CharacterSet startingSet,
			final List<TransliterableUTF8Character> items) {



		TransliterableUTF8CharacterSet result = new TransliterableUTF8CharacterSet(startingSet.name);
		result.items = items;
		final Map<String, String> transliterationMap = new HashMap<>();

		items.forEach(item -> {

			String key = item.asUtf8();
			String value = item.transliterated();
			transliterationMap.put(key, value);
		});
		result.transliterations = transliterationMap;
		return result;

	}

	/**
	 * Factory method that creates a new TransliterableUTF8CharacterSet with a
	 * custom transliteration for the items.
	 *
	 * @param startingSetTransliterableUTF8CharacterSet
	 * @param items                                     List
	 * @return TransliterableUTF8CharacterSet
	 * @throws IllegalArgumentException when either of the parameters is null or
	 *                                  items and transliterations are not of the
	 *                                  same size or there exists a
	 *                                  TransliterableUTF8CharacterSet without
	 *                                  transliteration.
	 */
	public static TransliterableUTF8CharacterSet withCustomTransliterations(
			final TransliterableCharacterSetProvider charSetProvider, final Map<String, String> transliterations) {

		if (transliterations == null) {
			throw new IllegalArgumentException("transliterations must not be null");
		}

		TransliterableUTF8CharacterSet result = from(charSetProvider);

		if (result.size() != transliterations.size()) {
			throw new IllegalArgumentException("items and transliterations need to be of same size");
		}

		for (TransliterableUTF8Character ch : result.items) {
			if (transliterations.get(ch.asUtf8()) == null) {
				throw new IllegalArgumentException("transliteration for " + ch.getOriginalCodepoint() + " is missing");
			}
		}

		result.transliterations = transliterations;
		return result;
	}

	@Override
	public String printableTransliteratedCharacter(String givenPrintableCharacter) {
		return this.transliterations.get(givenPrintableCharacter);
	}

	@Override
	public String name() {
		return this.name.name();
	}

	@Override
	public boolean isPrintableCharacterValid(String givenPrintableCharacter) {
		return this.transliterations.containsKey(givenPrintableCharacter);
	}

	@Override
	public boolean isUTF8CodepointValid(UTF8Codepoint codePoint) {
		if (codePoint == null) {
			return false;
		}

		return this.items.stream().map(item -> item.getOriginalCodepoint()).filter(cp -> codePoint.equals(cp))
				.findFirst().isPresent();
	}

	/**
	 * @return int the number of items.
	 */
	public int size() {
		return items.size();
	}
}
