// =====================================================
// Projekt: heike2718/unicode-tools
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
// =====================================================

package de.egladil.web.unicode_tools.internal;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import de.egladil.web.unicode_tools.UTF8Codepoint;

/**
 * CodePointsToUnicodeMapper maps a string separated by a defined char into a
 * string separated by \\u, wich is translateable by apache text
 * UnicodeUnescaper.
 *
 */
public class CodePointsToUnicodeMapper implements Function<UTF8Codepoint, String> {

	@Override
	public String apply(UTF8Codepoint utf8CodePoint) {

		if (utf8CodePoint == null) {
			throw new IllegalArgumentException("utf8CodePoint must not be null");
		}

		String[] tokens = StringUtils.split(utf8CodePoint.getCodePoints(), utf8CodePoint.getSeparationChar());

		StringBuffer sb = new StringBuffer();
		for (String token : tokens) {
			sb.append("\\u");
			sb.append(token);
		}
		return sb.toString();
	}

}
