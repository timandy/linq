package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Action1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArgsList;
import com.bestvike.linq.util.StringSplitOptions;
import com.bestvike.linq.util.Strings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Created by 许崇雷 on 2019-08-13.
 */
class SplitTest extends TestCase {
    private static String[] ToStringArray(char[] source) {
        if (source == null)
            return null;

        String[] result = new String[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = String.valueOf(source[i]);
        }
        return result;
    }

    private static IEnumerable<Object[]> SplitCharSeparatorData() {
        ArgsList argsList = new ArgsList();
        argsList.add("", ',', StringSplitOptions.None, new String[]{""});
        argsList.add("", ',', StringSplitOptions.RemoveEmptyEntries, new String[0]);
        argsList.add(",", ',', StringSplitOptions.None, new String[]{"", ""});
        argsList.add(",", ',', StringSplitOptions.RemoveEmptyEntries, new String[0]);
        argsList.add(",,", ',', StringSplitOptions.None, new String[]{"", "", ""});
        argsList.add(",,", ',', StringSplitOptions.RemoveEmptyEntries, new String[0]);
        argsList.add("ab", ',', StringSplitOptions.None, new String[]{"ab"});
        argsList.add("ab", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"ab"});
        argsList.add("a,b", ',', StringSplitOptions.None, new String[]{"a", "b"});
        argsList.add("a,b", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b"});
        argsList.add("a,", ',', StringSplitOptions.None, new String[]{"a", ""});
        argsList.add("a,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a"});
        argsList.add(",b", ',', StringSplitOptions.None, new String[]{"", "b"});
        argsList.add(",b", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"b"});
        argsList.add(",a,b", ',', StringSplitOptions.None, new String[]{"", "a", "b"});
        argsList.add(",a,b", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b"});
        argsList.add("a,b,", ',', StringSplitOptions.None, new String[]{"a", "b", ""});
        argsList.add("a,b,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b"});
        argsList.add("a,b,c", ',', StringSplitOptions.None, new String[]{"a", "b", "c"});
        argsList.add("a,b,c", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("a,,c", ',', StringSplitOptions.None, new String[]{"a", "", "c"});
        argsList.add("a,,c", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "c"});
        argsList.add(",a,b,c", ',', StringSplitOptions.None, new String[]{"", "a", "b", "c"});
        argsList.add(",a,b,c", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("a,b,c,", ',', StringSplitOptions.None, new String[]{"a", "b", "c", ""});
        argsList.add("a,b,c,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add(",a,b,c,", ',', StringSplitOptions.None, new String[]{"", "a", "b", "c", ""});
        argsList.add(",a,b,c,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("first,second", ',', StringSplitOptions.None, new String[]{"first", "second"});
        argsList.add("first,second", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second"});
        argsList.add("first,", ',', StringSplitOptions.None, new String[]{"first", ""});
        argsList.add("first,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first"});
        argsList.add(",second", ',', StringSplitOptions.None, new String[]{"", "second"});
        argsList.add(",second", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"second"});
        argsList.add(",first,second", ',', StringSplitOptions.None, new String[]{"", "first", "second"});
        argsList.add(",first,second", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second"});
        argsList.add("first,second,", ',', StringSplitOptions.None, new String[]{"first", "second", ""});
        argsList.add("first,second,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second"});
        argsList.add("first,second,third", ',', StringSplitOptions.None, new String[]{"first", "second", "third"});
        argsList.add("first,second,third", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"});
        argsList.add("first,,third", ',', StringSplitOptions.None, new String[]{"first", "", "third"});
        argsList.add("first,,third", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "third"});
        argsList.add(",first,second,third", ',', StringSplitOptions.None, new String[]{"", "first", "second", "third"});
        argsList.add(",first,second,third", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"});
        argsList.add("first,second,third,", ',', StringSplitOptions.None, new String[]{"first", "second", "third", ""});
        argsList.add("first,second,third,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"});
        argsList.add(",first,second,third,", ',', StringSplitOptions.None, new String[]{"", "first", "second", "third", ""});
        argsList.add(",first,second,third,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"});
        argsList.add("first,second,third", ' ', StringSplitOptions.None, new String[]{"first,second,third"});
        argsList.add("first,second,third", ' ', StringSplitOptions.RemoveEmptyEntries, new String[]{"first,second,third"});
        argsList.add("Foo Bar Baz", ' ', StringSplitOptions.None, new String[]{"Foo", "Bar", "Baz"});
        argsList.add("Foo Bar Baz", ' ', StringSplitOptions.RemoveEmptyEntries, new String[]{"Foo", "Bar", "Baz"});
        return argsList;
    }

    private static IEnumerable<Object[]> SplitStringSeparatorData() {
        ArgsList argsList = new ArgsList();
        argsList.add("a,b,c", null, StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("a,b,c", "", StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("aaabaaabaaa", "aa", StringSplitOptions.None, new String[]{"", "ab", "ab", "a"});
        argsList.add("this, is, a, string, with some spaces", ", ", StringSplitOptions.None, new String[]{"this", "is", "a", "string", "with some spaces"});
        argsList.add("a,b,c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("a,b,c", "", StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("aaabaaabaaa", "aa", StringSplitOptions.RemoveEmptyEntries, new String[]{"ab", "ab", "a"});
        argsList.add("this, is, a, string, with some spaces", ", ", StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with some spaces"});
        return argsList;
    }

    private static IEnumerable<Object[]> SplitCharArraySeparatorData() {
        ArgsList argsList = new ArgsList();
        argsList.add("a b c", null, StringSplitOptions.None, new String[]{"a", "b", "c"});
        argsList.add("a b c", new char[0], StringSplitOptions.None, new String[]{"a", "b", "c"});
        argsList.add("a,b,c", null, StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("a,b,c", new char[0], StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("this, is, a, string, with some spaces", new char[]{' '}, StringSplitOptions.None, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new char[]{' ', ','}, StringSplitOptions.None, new String[]{"this", "", "is", "", "a", "", "string", "", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new char[]{',', ' '}, StringSplitOptions.None, new String[]{"this", "", "is", "", "a", "", "string", "", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new char[]{',', ' ', 's'}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "a", "", "", "tring", "", "with", "", "ome", "", "pace", ""});
        argsList.add("this, is, a, string, with some spaces", new char[]{',', ' ', 's', 'a'}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "", "", "", "", "tring", "", "with", "", "ome", "", "p", "ce", ""});
        argsList.add("a b c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("a b c", new char[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("a,b,c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("a,b,c", new char[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("this, is, a, string, with some spaces", new char[]{' '}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new char[]{' ', ','}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new char[]{',', ' '}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new char[]{',', ' ', 's'}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "a", "tring", "with", "ome", "pace"});
        argsList.add("this, is, a, string, with some spaces", new char[]{',', ' ', 's', 'a'}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "tring", "with", "ome", "p", "ce"});
        return argsList;
    }

    private static IEnumerable<Object[]> SplitStringArraySeparatorData() {
        ArgsList argsList = new ArgsList();
        argsList.add("a b c", null, StringSplitOptions.None, new String[]{"a", "b", "c"});
        argsList.add("a b c", new String[0], StringSplitOptions.None, new String[]{"a", "b", "c"});
        argsList.add("a,b,c", null, StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("a,b,c", new String[0], StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("a,b,c", new String[]{null}, StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("a,b,c", new String[]{""}, StringSplitOptions.None, new String[]{"a,b,c"});
        argsList.add("this, is, a, string, with some spaces", new String[]{" "}, StringSplitOptions.None, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new String[]{" ", ", "}, StringSplitOptions.None, new String[]{"this", "is", "a", "string", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new String[]{", ", " "}, StringSplitOptions.None, new String[]{"this", "is", "a", "string", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new String[]{",", " ", "s"}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "a", "", "", "tring", "", "with", "", "ome", "", "pace", ""});
        argsList.add("this, is, a, string, with some spaces", new String[]{",", " ", "s", "a"}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "", "", "", "", "tring", "", "with", "", "ome", "", "p", "ce", ""});
        argsList.add("a b c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("a b c", new String[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"});
        argsList.add("a,b,c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("a,b,c", new String[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("a,b,c", new String[]{null}, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("a,b,c", new String[]{""}, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"});
        argsList.add("this, is, a, string, with some spaces", new String[]{" "}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new String[]{" ", ", "}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new String[]{", ", " "}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"});
        argsList.add("this, is, a, string, with some spaces", new String[]{",", " ", "s"}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "a", "tring", "with", "ome", "pace"});
        argsList.add("this, is, a, string, with some spaces", new String[]{",", " ", "s", "a"}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "tring", "with", "ome", "p", "ce"});
        return argsList;
    }

    @Test
    void SplitInvalidSource() {
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, ','));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, ',', StringSplitOptions.None));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, new char[]{','}));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, new char[]{','}, StringSplitOptions.None));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, ","));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, ",", StringSplitOptions.None));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, new String[]{","}));
        assertThrows(ArgumentNullException.class, () -> Linq.split(null, new String[]{","}, StringSplitOptions.None));
    }

    @Test
    void SplitInvalidOptions() {
        final String value = "a,b";

        assertThrows(ArgumentNullException.class, () -> Linq.split(value, ',', null));
        assertThrows(ArgumentNullException.class, () -> Linq.split(value, new char[]{','}, null));
        assertThrows(ArgumentNullException.class, () -> Linq.split(value, ",", null));
        assertThrows(ArgumentNullException.class, () -> Linq.split(value, new String[]{","}, null));
    }

    @Test
    void SplitEmptyValueWithRemoveEmptyEntriesOptionEmptyResult() {
        String value = Strings.Empty;
        final StringSplitOptions options = StringSplitOptions.RemoveEmptyEntries;

        assertEmpty(Linq.split(value, ',', options));
        assertEmpty(Linq.split(value, new char[]{','}, options));
        assertEmpty(Linq.split(value, ",", options));
        assertEmpty(Linq.split(value, new String[]{","}, options));
    }

    @Test
    void SplitNoMatchSingleResult() {
        final String value = "a b";
        final StringSplitOptions options = StringSplitOptions.None;

        IEnumerable<String> expected = Linq.singleton(value);

        assertEquals(expected, Linq.split(value, ','));
        assertEquals(expected, Linq.split(value, ',', options));
        assertEquals(expected, Linq.split(value, new char[]{','}));
        assertEquals(expected, Linq.split(value, new char[]{','}, options));
        assertEquals(expected, Linq.split(value, ","));
        assertEquals(expected, Linq.split(value, ",", options));
        assertEquals(expected, Linq.split(value, new String[]{","}));
        assertEquals(expected, Linq.split(value, new String[]{","}, options));
    }

    @ParameterizedTest
    @MethodSource("SplitCharSeparatorData")
    void SplitCharSeparator(String value, char separator, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separator, options));
        assertEquals(Linq.of(expected), Linq.split(value, new char[]{separator}, options));
        assertEquals(Linq.of(expected), Linq.split(value, String.valueOf(separator), options));
        assertEquals(Linq.of(expected), Linq.split(value, new String[]{String.valueOf(separator)}, options));
        if (options == StringSplitOptions.None) {
            assertEquals(Linq.of(expected), Linq.split(value, separator));
            assertEquals(Linq.of(expected), Linq.split(value, new char[]{separator}));
            assertEquals(Linq.of(expected), Linq.split(value, String.valueOf(separator)));
            assertEquals(Linq.of(expected), Linq.split(value, new String[]{String.valueOf(separator)}));
        }
    }

    @ParameterizedTest
    @MethodSource("SplitStringSeparatorData")
    void SplitStringSeparator(String value, String separator, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separator, options));
        assertEquals(Linq.of(expected), Linq.split(value, new String[]{separator}, options));
        if (options == StringSplitOptions.None) {
            assertEquals(Linq.of(expected), Linq.split(value, separator));
            assertEquals(Linq.of(expected), Linq.split(value, new String[]{separator}));
        }
    }

    @Test
    void SplitNullCharArraySeparator_BindsToCharArrayOverload() {
        String value = "a b c";
        String[] expected = new String[]{"a", "b", "c"};
        // Ensure Split(null) compiles successfully as a call to Split(char[])
        assertEquals(Linq.of(expected), Linq.split(value, (char[]) null));
    }

    @ParameterizedTest
    @MethodSource("SplitCharArraySeparatorData")
    void SplitCharArraySeparator(String value, char[] separators, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separators, options));
        assertEquals(Linq.of(expected), Linq.split(value, ToStringArray(separators), options));
    }

    @ParameterizedTest
    @MethodSource("SplitStringArraySeparatorData")
    void SplitStringArraySeparator(String value, String[] separators, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separators, options));
    }

    @Test
    void testSplitEmptySource() {
        assertEquals(Linq.of(""), Linq.split("", '\0', StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", ' ', StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", (String) null, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", "", StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", " ", StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", (char[]) null, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[0], StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{'\0'}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{'\0', '\0'}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{'\0', '\0', '\0'}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{'\0', '\0', '\0', '\0', '\0'}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{' '}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{' ', ' '}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{' ', ' ', ' '}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new char[]{' ', ' ', ' ', ' ', ' '}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", (String[]) null, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new String[0], StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new String[]{null}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new String[]{""}, StringSplitOptions.None));
        assertEquals(Linq.of(""), Linq.split("", new String[]{" "}, StringSplitOptions.None));

        assertEquals(Linq.empty(), Linq.split("", '\0', StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", ' ', StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", (String) null, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", "", StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", " ", StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", (char[]) null, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[0], StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{'\0'}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{'\0', '\0'}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{'\0', '\0', '\0'}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{'\0', '\0', '\0', '\0', '\0'}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{' '}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{' ', ' '}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{' ', ' ', ' '}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new char[]{' ', ' ', ' ', ' ', ' '}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", (String[]) null, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new String[0], StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new String[]{null}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new String[]{""}, StringSplitOptions.RemoveEmptyEntries));
        assertEquals(Linq.empty(), Linq.split("", new String[]{" "}, StringSplitOptions.RemoveEmptyEntries));
    }

    @Test
    void testSplitOverflow() {
        CharSequence source = new RepeatCharWithTailString('a', 'z', Integer.MAX_VALUE);

        assertEquals(2L, Linq.split(source, 'z', StringSplitOptions.None).longCount());
        assertEquals(2L, Linq.split(source, "z", StringSplitOptions.None).longCount());
        assertEquals(1L, Linq.split(source, new char[0], StringSplitOptions.None).longCount());
        assertEquals(2L, Linq.split(source, new char[]{'z'}, StringSplitOptions.None).longCount());
        assertEquals(2L, Linq.split(source, new char[]{'z', ' '}, StringSplitOptions.None).longCount());
        assertEquals(2L, Linq.split(source, new char[]{'z', ' ', ' '}, StringSplitOptions.None).longCount());
        assertEquals(2L, Linq.split(source, new char[]{'z', ' ', ' ', ' ', ' '}, StringSplitOptions.None).longCount());
        assertEquals(2L, Linq.split(source, new String[]{"z"}, StringSplitOptions.None).longCount());

        assertEquals(1L, Linq.split(source, 'z', StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, "z", StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, new char[0], StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, new char[]{'z'}, StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, new char[]{'z', ' '}, StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, new char[]{'z', ' ', ' '}, StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, new char[]{'z', ' ', ' ', ' ', ' '}, StringSplitOptions.RemoveEmptyEntries).longCount());
        assertEquals(1L, Linq.split(source, new String[]{"z"}, StringSplitOptions.RemoveEmptyEntries).longCount());
    }

    @Test
    void testSplit() {
        Action1<IEnumerable<String>> assertTwoElements = source -> {
            assertEquals(source.runOnce(), source.runOnce());
            try (IEnumerator<String> e = source.enumerator()) {
                assertTrue(e.moveNext());
                assertTrue(e.moveNext());
                assertFalse(e.moveNext());
                assertFalse(e.moveNext());
            }
        };
        assertTwoElements.apply(Linq.split("abc ", ' ', StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", " ", StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", new char[0], StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", new char[]{' '}, StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", new char[]{' ', ' '}, StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", new char[]{' ', ' ', ' '}, StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", new char[]{' ', ' ', ' ', ' ', ' '}, StringSplitOptions.None));
        assertTwoElements.apply(Linq.split("abc ", new String[]{" "}, StringSplitOptions.None));


        Action1<IEnumerable<String>> assertSingleElement = source -> {
            assertEquals(source.runOnce(), source.runOnce());
            try (IEnumerator<String> e = source.enumerator()) {
                assertTrue(e.moveNext());
                assertFalse(e.moveNext());
                assertFalse(e.moveNext());
            }
        };
        assertSingleElement.apply(Linq.split("abc ", ' ', StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", " ", StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", new char[0], StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", new char[]{' '}, StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", new char[]{' ', ' '}, StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", new char[]{' ', ' ', ' '}, StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", new char[]{' ', ' ', ' ', ' ', ' '}, StringSplitOptions.RemoveEmptyEntries));
        assertSingleElement.apply(Linq.split("abc ", new String[]{" "}, StringSplitOptions.RemoveEmptyEntries));

        assertEquals(Linq.of("thi", "", "", "i", "", "", "", "", "", "", "tring", "", "with", "", "ome", "", "p", "ce", "", "ų"), Linq.split("this, is, a, string, with some spaces ų", new char[]{',', ' ', 's', 'Ā', 'a'}, StringSplitOptions.None));
        assertEquals(Linq.of("thi", "i", "tring", "with", "ome", "p", "ce", "ų"), Linq.split("this, is, a, string, with some spaces ų", new char[]{',', ' ', 's', 'Ā', 'a'}, StringSplitOptions.RemoveEmptyEntries));
    }


    private static final class RepeatCharWithTailString implements CharSequence {
        private final char value;
        private final char tail;
        private final int count;

        private RepeatCharWithTailString(char value, char tail, int count) {
            if (count < 0)
                ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.count);
            this.value = value;
            this.tail = tail;
            this.count = count;
        }

        private static void checkBoundsBeginEnd(int begin, int end, int length) {
            if (begin < 0 || begin > end || end > length)
                throw new StringIndexOutOfBoundsException("begin " + begin + ", end " + end + ", length " + length);
        }

        @Override
        public int length() {
            return this.count;
        }

        @Override
        public char charAt(int index) {
            if (index < 0 || index >= this.count)
                throw new StringIndexOutOfBoundsException(index);
            return index == this.count - 1 ? this.tail : this.value;
        }

        @Override
        public CharSequence subSequence(int beginIndex, int endIndex) {
            int length = this.length();
            checkBoundsBeginEnd(beginIndex, endIndex, length);
            int subLen = endIndex - beginIndex;
            if (beginIndex == 0 && beginIndex == length)
                return this;
            return "sub sequence with length: " + subLen;
        }
    }
}
