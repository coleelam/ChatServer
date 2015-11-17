import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Ben Maxfield on 10/29/2015.
 */
public class CircularBufferTest {

    CircularBuffer buffer;

    @Before
    public void setUp() throws Exception {
        buffer = new CircularBuffer(4);
        buffer.put("first");
        buffer.put("second");
        buffer.put("third");
        buffer.put("fourth");
    }

    @Test
    public void test1() throws Exception {
        Assert.assertEquals(buffer.getNewest(1)[0], "0003) fourth");
    }
    @Test
    public void test2() throws Exception {
        String[] expected = {"0001) second", "0002) third", "0003) fourth"};
        Assert.assertArrayEquals(buffer.getNewest(3), expected);
    }
    @Test
    public void test3() throws Exception {
        buffer.put("fifth");
        String[] expected = {"0003) fourth", "0004) fifth"};
        Assert.assertArrayEquals(buffer.getNewest(2), expected);
    }
    @Test
    public void test4() throws Exception {
        CircularBuffer trash = new CircularBuffer(0);
        String[] expected = null;
        Assert.assertNull(trash.getNewest(1).toString().trim(), expected);
    }
    @Test
    public void test5() throws Exception {
        CircularBuffer trash = new CircularBuffer(1);
        String[] expected = null;
        Assert.assertNull(trash.getNewest(1).toString().trim(), expected);
    }
    @Test
    public void test6() throws Exception {
        CircularBuffer trash = new CircularBuffer(1);
        trash.put("bad");
        trash.put("good");
        String[] expected = {"0001) good"};
        Assert.assertArrayEquals(trash.getNewest(2), expected);
    }

}