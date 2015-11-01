import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Ben Maxfield on 11/1/2015.
 */
public class SessionCookieTest {

    @Test
    public void testTimedOut1() throws InterruptedException
    {
        SessionCookie c = null;
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut2() throws InterruptedException
    {
        SessionCookie c = null;
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut3() throws InterruptedException
    {
        SessionCookie c = null;
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut4() throws InterruptedException
    {
        SessionCookie c = null;
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut5() throws InterruptedException
    {
        SessionCookie c = null;
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut6() throws InterruptedException
    {
        SessionCookie c = null;
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
}