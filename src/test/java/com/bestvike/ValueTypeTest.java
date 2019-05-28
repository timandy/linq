package com.bestvike;

import org.junit.Test;

/**
 * Created by 许崇雷 on 2019-05-28.
 */
public class ValueTypeTest extends TestCase {
    @Test
    public void testEquals() {
        NoFieldBean noFieldBean = new NoFieldBean();
        NoFieldBean noFieldBeanExpected = new NoFieldBean();
        assertNotSame(noFieldBeanExpected, noFieldBean);
        assertEquals(noFieldBeanExpected, noFieldBean);

        OneFieldBean oneFieldBean = new OneFieldBean("Robert");
        OneFieldBean oneFieldBeanExpected = new OneFieldBean("Robert");
        assertNotSame(oneFieldBeanExpected, oneFieldBean);
        assertEquals(oneFieldBeanExpected, oneFieldBean);

        TwoFieldBean twoFieldBean = new TwoFieldBean("Robert", 45);
        TwoFieldBean twoFieldBeanExpected = new TwoFieldBean("Robert", 45);
        assertNotSame(twoFieldBeanExpected, twoFieldBean);
        assertEquals(twoFieldBeanExpected, twoFieldBean);

        MoreFieldBean moreFieldBean = new MoreFieldBean("Robert", '"', '\'', null, oneFieldBean, twoFieldBean);
        MoreFieldBean moreFieldBeanExpected = new MoreFieldBean("Robert", '"', '\'', null, oneFieldBeanExpected, twoFieldBeanExpected);
        assertNotSame(moreFieldBeanExpected, moreFieldBean);
        assertEquals(moreFieldBeanExpected, moreFieldBean);
    }

    @Test
    public void testHashCode() {
        NoFieldBean noFieldBean = new NoFieldBean();
        NoFieldBean noFieldBeanExpected = new NoFieldBean();
        assertEquals(noFieldBeanExpected.hashCode(), noFieldBean.hashCode());

        OneFieldBean oneFieldBean = new OneFieldBean("Robert");
        OneFieldBean oneFieldBeanExpected = new OneFieldBean("Robert");
        assertEquals(oneFieldBeanExpected.hashCode(), oneFieldBean.hashCode());

        TwoFieldBean twoFieldBean = new TwoFieldBean("Robert", 45);
        TwoFieldBean twoFieldBeanExpected = new TwoFieldBean("Robert", 45);
        assertEquals(twoFieldBeanExpected.hashCode(), twoFieldBean.hashCode());

        MoreFieldBean moreFieldBean = new MoreFieldBean("Robert", '"', '\'', null, oneFieldBean, twoFieldBean);
        MoreFieldBean moreFieldBeanExpected = new MoreFieldBean("Robert", '"', '\'', null, oneFieldBeanExpected, twoFieldBeanExpected);
        assertEquals(moreFieldBeanExpected.hashCode(), moreFieldBean.hashCode());
    }

    @Test
    public void testToString() {
        NoFieldBean noFieldBean = new NoFieldBean();
        String noFieldBeanExpected = "NoFieldBean{}";
        assertEquals(noFieldBeanExpected, noFieldBean.toString());

        OneFieldBean oneFieldBean = new OneFieldBean("Robert");
        String oneFieldBeanExpected = "OneFieldBean{Name='Robert'}";
        assertEquals(oneFieldBeanExpected, oneFieldBean.toString());

        TwoFieldBean twoFieldBean = new TwoFieldBean("Robert", 45);
        String twoFieldBeanExpected = "TwoFieldBean{Name='Robert', Score=45}";
        assertEquals(twoFieldBeanExpected, twoFieldBean.toString());

        MoreFieldBean moreFieldBean = new MoreFieldBean("Robert\"'Tim'\"", '"', '\'', null, oneFieldBean, twoFieldBean);
        String moreFieldBeanExpected = "MoreFieldBean{One='Robert\"'Tim'\"', Two=\", Three=', noFieldBean=null, oneFieldBean=OneFieldBean{Name='Robert'}, twoFieldBean=TwoFieldBean{Name='Robert', Score=45}}";
        assertEquals(moreFieldBeanExpected, moreFieldBean.toString());
    }


    private static class NoFieldBean extends ValueType {
    }

    private static class OneFieldBean extends ValueType {
        final String Name;

        private OneFieldBean(String name) {
            this.Name = name;
        }
    }

    private static class TwoFieldBean extends ValueType {
        final String Name;
        final int Score;

        private TwoFieldBean(String name, int score) {
            this.Name = name;
            this.Score = score;
        }
    }

    private static class MoreFieldBean extends ValueType {
        final String One;
        final char Two;
        final char Three;
        final NoFieldBean noFieldBean;
        final OneFieldBean oneFieldBean;
        final TwoFieldBean twoFieldBean;

        private MoreFieldBean(String one, char two, char three, NoFieldBean noFieldBean, OneFieldBean oneFieldBean, TwoFieldBean twoFieldBean) {
            this.One = one;
            this.Two = two;
            this.Three = three;
            this.noFieldBean = noFieldBean;
            this.oneFieldBean = oneFieldBean;
            this.twoFieldBean = twoFieldBean;
        }
    }
}
