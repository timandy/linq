package com.bestvike.linq.util;

import com.bestvike.TestCase;
import com.bestvike.ValueType;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.Linq;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
public class ReflectionUtilsTest extends TestCase {
    @Test
    public void testGetFields() {
        Bean bean = new Bean("basePri", "basePro", 123, "Tim", "Andy", 456);

        assertEquals(Integer.MAX_VALUE, Base.STA);
        assertEquals(Integer.MAX_VALUE, Bean.STATIC);

        assertEquals("Tim", ((Base) bean).getPri());
        assertEquals("Andy", ((Base) bean).getPro());
        assertEquals(123, bean.getScore());

        assertEquals("Tim", bean.getPri());
        assertEquals("Andy", bean.getPro());
        assertEquals(456, bean.getPub());

        Field[] fields = ReflectionUtils.getFields(Bean.class);
        assertEquals(6, fields.length);
        IEnumerable<String> fieldEnumerable = Linq.of(fields).select(Field::getName);
        assertEquals(2, fieldEnumerable.count("pri"::equals));
        assertEquals(2, fieldEnumerable.count("pro"::equals));
        assertEquals(1, fieldEnumerable.count("pub"::equals));
        assertEquals(1, fieldEnumerable.count("score"::equals));
    }


    private static class Base extends ValueType {
        private static final int STA = Integer.MAX_VALUE;
        public final int score;
        protected final String pro;
        private final String pri;

        public Base(String pri, String pro, int score) {
            this.pri = pri;
            this.pro = pro;
            this.score = score;
        }

        public String getPri() {
            return this.pri;
        }

        public String getPro() {
            return this.pro;
        }

        public int getScore() {
            return this.score;
        }
    }

    private static class Bean extends Base {
        private static final int STATIC = Integer.MAX_VALUE;
        public int pub;
        protected String pro;
        private String pri;

        public Bean(String pri, String pro, int score, String pri1, String pro1, int pub) {
            super(pri, pro, score);
            this.pri = pri1;
            this.pro = pro1;
            this.pub = pub;
        }

        @Override
        public String getPri() {
            return this.pri;
        }

        @Override
        public String getPro() {
            return this.pro;
        }

        public int getPub() {
            return this.pub;
        }
    }
}
