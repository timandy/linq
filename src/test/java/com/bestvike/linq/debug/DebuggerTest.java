package com.bestvike.linq.debug;

import com.bestvike.TestCase;
import com.bestvike.linq.exception.InvalidOperationException;
import org.junit.jupiter.api.Test;


/**
 * Created by 许崇雷 on 2019-12-07.
 */
@SuppressWarnings("unused")
class DebuggerTest extends TestCase {
    @Test
    void testGetDebuggerDisplayText() {
        Apple[] apples = new Apple[0];
        assertEquals("Count = 0", Debugger.getDebuggerDisplayText(apples));

        LacksBrace lacksBrace = new LacksBrace();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplayText(lacksBrace));

        StaticMethod staticMethod = new StaticMethod();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplayText(staticMethod));

        InstanceMethod instanceMethod = new InstanceMethod();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplayText(instanceMethod));

        ErrorMethod errorMethod = new ErrorMethod();
        assertThrows(RuntimeException.class, () -> Debugger.getDebuggerDisplayText(errorMethod));

        NoMethod noMethod = new NoMethod();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplayText(noMethod));

        StaticField staticField = new StaticField();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplayText(staticField));

        InstanceField instanceField = new InstanceField();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplayText(instanceField));

        NoField noField = new NoField();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplayText(noField));

        Apple apple = new Apple("fuji", "red");
        assertEquals("Style = fuji, Color = \"red\"", Debugger.getDebuggerDisplayText(apple));
    }

    @Test
    void testGetDebugView() {
        Pen pen = new Pen();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebugView(pen));

        People people = new People();
        IDebugView peopleDebugView = Debugger.getDebugView(people);
        assertIsType(PeopleDebugView.class, peopleDebugView);
    }


    @DebuggerDisplay("hello {world")
    private static class LacksBrace {
    }

    @DebuggerDisplay("value={value()}")
    private static class StaticMethod {
        private static String value() {
            return "hello";
        }
    }

    @DebuggerDisplay("value={value()}")
    private static class InstanceMethod {
        private String value() {
            return "hello";
        }
    }

    @DebuggerDisplay("value={value()}")
    private static class ErrorMethod {
        private String value() {
            throw new IllegalArgumentException();
        }
    }

    @DebuggerDisplay("value={value2()}")
    private static class NoMethod {
        private String value() {
            return "hello";
        }
    }

    @DebuggerDisplay("value={value}")
    private static class StaticField {
        private static final String value = "hello";
    }

    @DebuggerDisplay("value={value}")
    private static class InstanceField {
        private final String value = "hello";
    }

    @DebuggerDisplay("value={value2}")
    private static class NoField {
        private final String value = "hello";
    }

    @DebuggerDisplay("Style = {getStyle(),nq}, Color = {color}")
    private static class Apple {
        private final String style;
        private final String color;

        private Apple(String style, String color) {
            this.style = style;
            this.color = color;
        }

        public String getStyle() {
            return this.style;
        }

        public String getColor() {
            return this.color;
        }
    }

    private static class PenDebugView implements IDebugView {
        private final Pen pen;

        private PenDebugView() {
            this.pen = null;
        }

        private PenDebugView(Object apple) {
            throw new RuntimeException();
        }

        @Override
        public Object getProxyObject() {
            return this.pen;
        }
    }

    @DebuggerTypeProxy(PenDebugView.class)
    private static class Pen {
    }

    private static class PeopleDebugView implements IDebugView {
        private final People people;

        private PeopleDebugView(People people) {
            this.people = people;
        }

        private PeopleDebugView() {
            this.people = null;
        }

        private PeopleDebugView(Object people) {
            throw new RuntimeException();
        }

        @Override
        public Object getProxyObject() {
            return this.people;
        }
    }

    @DebuggerTypeProxy(PeopleDebugView.class)
    private static class People {
    }
}
