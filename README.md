# unicode-tools

provides some tools for handling validating and mapping (transliterate) Strings based on UTF-8.

__Transliteration__

is a method that allowes to map some character to another usually simpler character.

This way one can search for text containig characters that are errorprone when input
by keyboard. Usually, there will be a larger set of matches when searching with transliterated
text.

__TransliterableCharacter__

is an interface that defines a transliterable character. A transliterable character
is a character that can be mapped to another character

__TransliterableCharacterSet__

is a collection of TransliterableCharacter instances i.e. a Set of characters
that are considered to be valid mappable input. It provides both a set of valid characters
and a way to transform these characters into a simpler to search character.

__ValidationProvider__

is an in interface that can be used to decide whether some character is valid with respect
to a TransliterableCharacterSet.

___Example:___ C̀ can be mapped to C


__UTF8Codepoint__

is a ValueType that encapsulates UTF-8 code points.


__Validation__

Given the Annotation DefaultUnicodeString one can annotate class attributes in order
to allow to be validated by the java beans validation process. The Implementation of
the Validator is done by DefaultUnicodeStringValidator that is based on the TransliterableUTF8CharacterSet
given by [defaultTransliterableCharacterSet.xml](/src/main/resources/defaultTransliterableCharacterSet.xml)

By defining an own TransliterableUTF8CharacterSet one can exploid this validator.


## Releases

[Release-Notes](RELEASE-NOTES.md)