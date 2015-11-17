import org.junit.Assert;
import org.junit.Test;


/**
 * Created by Ben Maxfield and Cole Elam on 11/1/2015.
 */
public class SessionCookieTest {

    @Test
    public void testTimedOut1() throws InterruptedException
    {
        SessionCookie c = new SessionCookie((long) 0001);
        c.setTimeout(1);
        c.updateTimeOfActivity();
        Thread.sleep(1200);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut2() throws InterruptedException
    {
        SessionCookie c = new SessionCookie((long) 0001);
        c.setTimeout(1);
        c.updateTimeOfActivity();
        Thread.sleep(1000);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut3() throws InterruptedException
    {
        SessionCookie c = new SessionCookie((long) 0001);
        c.setTimeout(1);
        c.updateTimeOfActivity();
        Thread.sleep(800);
        Assert.assertEquals(c.hasTimedOut(), false);
    }
    @Test
    public void testTimedOut4() throws InterruptedException
    {
        SessionCookie c = new SessionCookie((long) 0001);
        c.setTimeout(3);
        c.updateTimeOfActivity();
        Thread.sleep(3100);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut5() throws InterruptedException
    {
        SessionCookie c = new SessionCookie((long) 0001);
        c.setTimeout(30);
        c.updateTimeOfActivity();
        Thread.sleep(31000);
        Assert.assertEquals(c.hasTimedOut(), true);
    }
    @Test
    public void testTimedOut6() throws InterruptedException
    {
        SessionCookie c = new SessionCookie((long) 0001);
        c.setTimeout(30);
        c.updateTimeOfActivity();
        Thread.sleep(29000);
        Assert.assertEquals(c.hasTimedOut(), false);
    }
    @Test
    public void testSessionCookieTimeOut() throws InterruptedException {
          //change timeout interval to 1 second

        SessionCookie cookie = new SessionCookie(0001);

        cookie.setTimeout(1);

        System.out.println(cookie.hasTimedOut()); // should print false

        Thread.sleep(1200); // Sleep for 1.2 seconds

        System.out.println(cookie.hasTimedOut()); // should print true
    }
}