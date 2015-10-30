import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;

import java.util.Arrays;

import static org.junit.Assert.*;

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
        Assert.assertEquals(buffer.getNewest(1)[0], "fourth");
    }
    @Test
    public void test2() throws Exception {
        String[] expected = {"second", "third", "fourth"};
        Assert.assertEquals(buffer.getNewest(3), expected);
    }
    @Test
    public void test3() throws Exception {
        buffer.put("fifth");
        String[] expected = {"fourth", "fifth"};
        Assert.assertEquals(buffer.getNewest(2), expected);
    }
    @Test
    public void test4() throws Exception {
        CircularBuffer trash = new CircularBuffer(0);
        Assert.assertEquals(trash.getNewest(1), null);
    }
    @Test
    public void test5() throws Exception {
        CircularBuffer trash = new CircularBuffer(1);
        System.out.println(Arrays.toString(trash.getNewest(1)));
        Assert.assertEquals(trash.getNewest(1), null);
    }
    @Test
    public void test6() throws Exception {
        CircularBuffer trash = new CircularBuffer(1);
        trash.put("bad");
        trash.put("good");
        String[] expected = {"good"};
        Assert.assertEquals(trash.getNewest(2), expected);
    }

}