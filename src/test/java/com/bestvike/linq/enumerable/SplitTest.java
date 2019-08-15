package com.bestvike.linq.enumerable;

import com.bestvike.TestCase;
import com.bestvike.function.Action1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.Linq;
import com.bestvike.linq.exception.ArgumentNullException;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.StringSplitOptions;
import com.bestvike.linq.util.Strings;
import com.bestvike.tuple.Tuple;
import com.bestvike.tuple.Tuple4;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-08-13.
 */
public class SplitTest extends TestCase {
    private static String[] ToStringArray(char[] source) {
        if (source == null)
            return null;

        String[] result = new String[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = String.valueOf(source[i]);
        }
        return result;
    }

    @Test
    public void SplitInvalidSource() {
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
    public void SplitInvalidOptions() {
        final String value = "a,b";

        assertThrows(ArgumentNullException.class, () -> Linq.split(value, ',', null));
        assertThrows(ArgumentNullException.class, () -> Linq.split(value, new char[]{','}, null));
        assertThrows(ArgumentNullException.class, () -> Linq.split(value, ",", null));
        assertThrows(ArgumentNullException.class, () -> Linq.split(value, new String[]{","}, null));
    }

    @Test
    public void SplitEmptyValueWithRemoveEmptyEntriesOptionEmptyResult() {
        String value = Strings.Empty;
        final StringSplitOptions options = StringSplitOptions.RemoveEmptyEntries;

        assertEmpty(Linq.split(value, ',', options));
        assertEmpty(Linq.split(value, new char[]{','}, options));
        assertEmpty(Linq.split(value, ",", options));
        assertEmpty(Linq.split(value, new String[]{","}, options));
    }

    @Test
    public void SplitNoMatchSingleResult() {
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

    private List<Tuple4<String, Character, StringSplitOptions, String[]>> SplitCharSeparatorData() {
        List<Tuple4<String, Character, StringSplitOptions, String[]>> list = new ArrayList<>();
        list.add(Tuple.create("", ',', StringSplitOptions.None, new String[]{""}));
        list.add(Tuple.create("", ',', StringSplitOptions.RemoveEmptyEntries, new String[0]));
        list.add(Tuple.create(",", ',', StringSplitOptions.None, new String[]{"", ""}));
        list.add(Tuple.create(",", ',', StringSplitOptions.RemoveEmptyEntries, new String[0]));
        list.add(Tuple.create(",,", ',', StringSplitOptions.None, new String[]{"", "", ""}));
        list.add(Tuple.create(",,", ',', StringSplitOptions.RemoveEmptyEntries, new String[0]));
        list.add(Tuple.create("ab", ',', StringSplitOptions.None, new String[]{"ab"}));
        list.add(Tuple.create("ab", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"ab"}));
        list.add(Tuple.create("a,b", ',', StringSplitOptions.None, new String[]{"a", "b"}));
        list.add(Tuple.create("a,b", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b"}));
        list.add(Tuple.create("a,", ',', StringSplitOptions.None, new String[]{"a", ""}));
        list.add(Tuple.create("a,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a"}));
        list.add(Tuple.create(",b", ',', StringSplitOptions.None, new String[]{"", "b"}));
        list.add(Tuple.create(",b", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"b"}));
        list.add(Tuple.create(",a,b", ',', StringSplitOptions.None, new String[]{"", "a", "b"}));
        list.add(Tuple.create(",a,b", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b"}));
        list.add(Tuple.create("a,b,", ',', StringSplitOptions.None, new String[]{"a", "b", ""}));
        list.add(Tuple.create("a,b,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b"}));
        list.add(Tuple.create("a,b,c", ',', StringSplitOptions.None, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,b,c", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,,c", ',', StringSplitOptions.None, new String[]{"a", "", "c"}));
        list.add(Tuple.create("a,,c", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "c"}));
        list.add(Tuple.create(",a,b,c", ',', StringSplitOptions.None, new String[]{"", "a", "b", "c"}));
        list.add(Tuple.create(",a,b,c", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,b,c,", ',', StringSplitOptions.None, new String[]{"a", "b", "c", ""}));
        list.add(Tuple.create("a,b,c,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create(",a,b,c,", ',', StringSplitOptions.None, new String[]{"", "a", "b", "c", ""}));
        list.add(Tuple.create(",a,b,c,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("first,second", ',', StringSplitOptions.None, new String[]{"first", "second"}));
        list.add(Tuple.create("first,second", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second"}));
        list.add(Tuple.create("first,", ',', StringSplitOptions.None, new String[]{"first", ""}));
        list.add(Tuple.create("first,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first"}));
        list.add(Tuple.create(",second", ',', StringSplitOptions.None, new String[]{"", "second"}));
        list.add(Tuple.create(",second", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"second"}));
        list.add(Tuple.create(",first,second", ',', StringSplitOptions.None, new String[]{"", "first", "second"}));
        list.add(Tuple.create(",first,second", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second"}));
        list.add(Tuple.create("first,second,", ',', StringSplitOptions.None, new String[]{"first", "second", ""}));
        list.add(Tuple.create("first,second,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second"}));
        list.add(Tuple.create("first,second,third", ',', StringSplitOptions.None, new String[]{"first", "second", "third"}));
        list.add(Tuple.create("first,second,third", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"}));
        list.add(Tuple.create("first,,third", ',', StringSplitOptions.None, new String[]{"first", "", "third"}));
        list.add(Tuple.create("first,,third", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "third"}));
        list.add(Tuple.create(",first,second,third", ',', StringSplitOptions.None, new String[]{"", "first", "second", "third"}));
        list.add(Tuple.create(",first,second,third", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"}));
        list.add(Tuple.create("first,second,third,", ',', StringSplitOptions.None, new String[]{"first", "second", "third", ""}));
        list.add(Tuple.create("first,second,third,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"}));
        list.add(Tuple.create(",first,second,third,", ',', StringSplitOptions.None, new String[]{"", "first", "second", "third", ""}));
        list.add(Tuple.create(",first,second,third,", ',', StringSplitOptions.RemoveEmptyEntries, new String[]{"first", "second", "third"}));
        list.add(Tuple.create("first,second,third", ' ', StringSplitOptions.None, new String[]{"first,second,third"}));
        list.add(Tuple.create("first,second,third", ' ', StringSplitOptions.RemoveEmptyEntries, new String[]{"first,second,third"}));
        list.add(Tuple.create("Foo Bar Baz", ' ', StringSplitOptions.None, new String[]{"Foo", "Bar", "Baz"}));
        list.add(Tuple.create("Foo Bar Baz", ' ', StringSplitOptions.RemoveEmptyEntries, new String[]{"Foo", "Bar", "Baz"}));
        return list;
    }

    private void SplitCharSeparator(String value, char separator, StringSplitOptions options, String[] expected) {
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

    @Test
    public void SplitCharSeparator() {
        for (Tuple4<String, Character, StringSplitOptions, String[]> data : this.SplitCharSeparatorData()) {
            this.SplitCharSeparator(data.getItem1(), data.getItem2(), data.getItem3(), data.getItem4());
        }
    }

    private List<Tuple4<String, String, StringSplitOptions, String[]>> SplitStringSeparatorData() {
        List<Tuple4<String, String, StringSplitOptions, String[]>> list = new ArrayList<>();
        list.add(Tuple.create("a,b,c", null, StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", "", StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("aaabaaabaaa", "aa", StringSplitOptions.None, new String[]{"", "ab", "ab", "a"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", ", ", StringSplitOptions.None, new String[]{"this", "is", "a", "string", "with some spaces"}));
        list.add(Tuple.create("a,b,c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", "", StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("aaabaaabaaa", "aa", StringSplitOptions.RemoveEmptyEntries, new String[]{"ab", "ab", "a"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", ", ", StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with some spaces"}));
        return list;
    }

    private void SplitStringSeparator(String value, String separator, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separator, options));
        assertEquals(Linq.of(expected), Linq.split(value, new String[]{separator}, options));
        if (options == StringSplitOptions.None) {
            assertEquals(Linq.of(expected), Linq.split(value, separator));
            assertEquals(Linq.of(expected), Linq.split(value, new String[]{separator}));
        }
    }

    @Test
    public void SplitStringSeparator() {
        for (Tuple4<String, String, StringSplitOptions, String[]> data : this.SplitStringSeparatorData()) {
            this.SplitStringSeparator(data.getItem1(), data.getItem2(), data.getItem3(), data.getItem4());
        }
    }

    @Test
    public void SplitNullCharArraySeparator_BindsToCharArrayOverload() {
        String value = "a b c";
        String[] expected = new String[]{"a", "b", "c"};
        // Ensure Split(null) compiles successfully as a call to Split(char[])
        assertEquals(Linq.of(expected), Linq.split(value, (char[]) null));
    }

    private List<Tuple4<String, char[], StringSplitOptions, String[]>> SplitCharArraySeparatorData() {
        List<Tuple4<String, char[], StringSplitOptions, String[]>> list = new ArrayList<>();
        list.add(Tuple.create("a b c", null, StringSplitOptions.None, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a b c", new char[0], StringSplitOptions.None, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,b,c", null, StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new char[0], StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{' '}, StringSplitOptions.None, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{' ', ','}, StringSplitOptions.None, new String[]{"this", "", "is", "", "a", "", "string", "", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{',', ' '}, StringSplitOptions.None, new String[]{"this", "", "is", "", "a", "", "string", "", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{',', ' ', 's'}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "a", "", "", "tring", "", "with", "", "ome", "", "pace", ""}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{',', ' ', 's', 'a'}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "", "", "", "", "tring", "", "with", "", "ome", "", "p", "ce", ""}));
        list.add(Tuple.create("a b c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a b c", new char[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,b,c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new char[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{' '}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{' ', ','}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{',', ' '}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{',', ' ', 's'}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "a", "tring", "with", "ome", "pace"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new char[]{',', ' ', 's', 'a'}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "tring", "with", "ome", "p", "ce"}));
        return list;
    }

    private void SplitCharArraySeparator(String value, char[] separators, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separators, options));
        assertEquals(Linq.of(expected), Linq.split(value, ToStringArray(separators), options));
    }

    @Test
    public void SplitCharArraySeparator() {
        for (Tuple4<String, char[], StringSplitOptions, String[]> data : this.SplitCharArraySeparatorData()) {
            this.SplitCharArraySeparator(data.getItem1(), data.getItem2(), data.getItem3(), data.getItem4());
        }
    }

    private List<Tuple4<String, String[], StringSplitOptions, String[]>> SplitStringArraySeparatorData() {
        List<Tuple4<String, String[], StringSplitOptions, String[]>> list = new ArrayList<>();
        list.add(Tuple.create("a b c", null, StringSplitOptions.None, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a b c", new String[0], StringSplitOptions.None, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,b,c", null, StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new String[0], StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new String[]{null}, StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new String[]{""}, StringSplitOptions.None, new String[]{"a,b,c"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{" "}, StringSplitOptions.None, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{" ", ", "}, StringSplitOptions.None, new String[]{"this", "is", "a", "string", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{", ", " "}, StringSplitOptions.None, new String[]{"this", "is", "a", "string", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{",", " ", "s"}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "a", "", "", "tring", "", "with", "", "ome", "", "pace", ""}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{",", " ", "s", "a"}, StringSplitOptions.None, new String[]{"thi", "", "", "i", "", "", "", "", "", "", "tring", "", "with", "", "ome", "", "p", "ce", ""}));
        list.add(Tuple.create("a b c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a b c", new String[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a", "b", "c"}));
        list.add(Tuple.create("a,b,c", null, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new String[0], StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new String[]{null}, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("a,b,c", new String[]{""}, StringSplitOptions.RemoveEmptyEntries, new String[]{"a,b,c"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{" "}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this,", "is,", "a,", "string,", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{" ", ", "}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{", ", " "}, StringSplitOptions.RemoveEmptyEntries, new String[]{"this", "is", "a", "string", "with", "some", "spaces"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{",", " ", "s"}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "a", "tring", "with", "ome", "pace"}));
        list.add(Tuple.create("this, is, a, string, with some spaces", new String[]{",", " ", "s", "a"}, StringSplitOptions.RemoveEmptyEntries, new String[]{"thi", "i", "tring", "with", "ome", "p", "ce"}));
        return list;
    }

    private void SplitStringArraySeparator(String value, String[] separators, StringSplitOptions options, String[] expected) {
        assertEquals(Linq.of(expected), Linq.split(value, separators, options));
    }

    @Test
    public void SplitStringArraySeparator() {
        for (Tuple4<String, String[], StringSplitOptions, String[]> data : this.SplitStringArraySeparatorData()) {
            this.SplitStringArraySeparator(data.getItem1(), data.getItem2(), data.getItem3(), data.getItem4());
        }
    }

    @Test
    public void testSplitEmptySource() {
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
    public void testSplitOverflow() {
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
    public void testSplit() {
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


    static final class RepeatCharWithTailString implements CharSequence {
        private final char value;
        private final char tail;
        private final int count;

        RepeatCharWithTailString(char value, char tail, int count) {
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
