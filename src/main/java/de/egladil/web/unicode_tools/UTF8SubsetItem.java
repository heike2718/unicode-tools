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
 * UTF8SubsetItem is an Item of a UTF8Subset.
 */
public class UTF8SubsetItem {

	private final UTF8Codepoint utf8Codepoint;

	private UTF8Codepoint mapping;

	/**
	 * UTF8SubsetItem
	 */
	public UTF8SubsetItem(final UTF8Codepoint utf8Codepoint) {
		if (utf8Codepoint == null) {
			throw new IllegalArgumentException("utf8Codepoint must not be null");
		}
		this.utf8Codepoint = utf8Codepoint;
	}

	/**
	 * UTF8SubsetItem
	 */
	public UTF8SubsetItem(UTF8Codepoint utf8Codepoint, UTF8Codepoint mapping) {
		this(utf8Codepoint);

		if (mapping == null) {
			throw new IllegalArgumentException("mapping must not be null");
		}

		this.mapping = mapping;
	}

	/**
	 * @return UTF8Codepoint never null.
	 */
	public UTF8Codepoint utf8Codepoint() {
		return utf8Codepoint;
	}

	/**
	 * @return UTF8Codepoint or null.
	 */
	public UTF8Codepoint mapping() {
		return this.mapping;
	}

	/**
	 * @return Stringt the utf8CodePoint as printable character.
	 */
	public String asPrintableCharacter() {
		return this.utf8Codepoint.utf8();
	}

	/**
	 *
	 * @return String null or the mapping as printable character.
	 */
	public String mappingAsPrintableCharacter() {
		if (mapping == null) {
			return null;
		}
		return mapping.utf8();
	}

	/**
	 * @return boolean true if it has a mapping false otherwise.
	 */
	public boolean isTransliterable() {
		return this.mapping != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((utf8Codepoint == null) ? 0 : utf8Codepoint.hashCode());
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
		UTF8SubsetItem other = (UTF8SubsetItem) obj;
		if (utf8Codepoint == null) {
			if (other.utf8Codepoint != null) {
				return false;
			}
		} else if (!utf8Codepoint.equals(other.utf8Codepoint)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UTF8SubsetItem [" + utf8Codepoint + ", printed as " + asPrintableCharacter() + "]";
	}
}
