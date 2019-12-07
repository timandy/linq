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
    void testExpression() {
        LacksBrace lacksBrace = new LacksBrace();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplay(lacksBrace));

        ErrorExpression errorExpression = new ErrorExpression();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplay(errorExpression));
    }

    @Test
    void testMethod() {
        StaticMethod staticMethod = new StaticMethod();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplay(staticMethod));

        InstanceMethod instanceMethod = new InstanceMethod();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplay(instanceMethod));

        ErrorMethod errorMethod = new ErrorMethod();
        assertThrows(RuntimeException.class, () -> Debugger.getDebuggerDisplay(errorMethod));

        NoMethod noMethod = new NoMethod();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplay(noMethod));
    }

    @Test
    void testField() {
        StaticField staticField = new StaticField();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplay(staticField));

        InstanceField instanceField = new InstanceField();
        assertEquals("value=\"hello\"", Debugger.getDebuggerDisplay(instanceField));

        NoField noField = new NoField();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebuggerDisplay(noField));
    }

    @Test
    void testDebugView() {
        People people = new People();
        IDebugView peopleDebugView = Debugger.getDebugView(people);
        assertIsType(PeopleDebugView.class, peopleDebugView);

        Apple apple = new Apple();
        assertThrows(InvalidOperationException.class, () -> Debugger.getDebugView(apple));
    }


    @DebuggerDisplay("hello {world")
    private static class LacksBrace {
    }

    @DebuggerDisplay("value={value}")
    private static class ErrorExpression {
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
        private static String value = "hello";
    }

    @DebuggerDisplay("value={value}")
    private static class InstanceField {
        private String value = "hello";
    }

    @DebuggerDisplay("value={value2}")
    private static class NoField {
        private String value = "hello";
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

    private static class AppleDebugView implements IDebugView {
        private final Apple apple;

        private AppleDebugView() {
            this.apple = null;
        }

        private AppleDebugView(Object apple) {
            throw new RuntimeException();
        }

        @Override
        public Object getProxyObject() {
            return this.apple;
        }
    }

    @DebuggerTypeProxy(AppleDebugView.class)
    private static class Apple {
    }
}
