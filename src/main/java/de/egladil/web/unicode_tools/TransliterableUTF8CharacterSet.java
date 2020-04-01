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

/**
 * TransliterableUTF8CharacterSet
 */
public class TransliterableUTF8CharacterSet implements TransliterationProvider, ValidationProvider {

	private final TransliterableCharacterSetName name;

	private List<TransliterableUTF8Character> items;

	private Map<String, String> transliterations;

	/**
	 * TransliterableUTF8CharacterSet
	 *
	 * @param name String
	 */
	TransliterableUTF8CharacterSet(TransliterableCharacterSetName name) {
		this.name = name;
		this.items = new ArrayList<>();
		this.transliterations = new HashMap<>();
	}

	public static TransliterableUTF8CharacterSet withItems(TransliterableUTF8CharacterSet startingSet,
			List<TransliterableUTF8Character> items) {

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

	public static TransliterableUTF8CharacterSet withItemsAndTransliterations(
			TransliterableUTF8CharacterSet startingSet, List<TransliterableUTF8Character> items,
			Map<String, String> transliterations) {

		TransliterableUTF8CharacterSet result = new TransliterableUTF8CharacterSet(startingSet.name);
		result.items = items;

		// TODO items must not contain elements that have no mapping in
		// transliterations. Every key in transliteration must have a match in items.

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
	public boolean isValid(String givenPrintableCharacter) {
		return this.transliterations.containsKey(givenPrintableCharacter);
	}

	@Override
	public boolean isValid(UTF8Codepoint codePoint) {
		if (codePoint == null) {
			return false;
		}

		return this.items.stream().map(item -> item.getOriginalCodepoint()).filter(cp -> codePoint.equals(cp))
				.findFirst().isPresent();
	}
}
