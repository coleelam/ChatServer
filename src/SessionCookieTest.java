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
    public void testSessionCookieTimeOut() throws InterruptedException {
          //change timeout interval to 1 second

        SessionCookie cookie = new SessionCookie();

        cookie.setTimeout(1);

        System.out.println(cookie.hasTimedOut()); // should print false

        Thread.sleep(1200); // Sleep for 1.2 seconds

        System.out.println(cookie.hasTimedOut()); // should print true
    }
}