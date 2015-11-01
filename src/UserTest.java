import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Ben Maxfield on 11/1/2015.
 */
public class UserTest {

    User u = null;

    @Before
    public void setUp() throws Exception {
        u = new User("user", "pass1!", new SessionCookie());
    }

    @Test
    public void testCheckPassword1()
    {
        Assert.assertEquals(u.checkPassword("pass1"), false);
    }

    @Test
    public void testCheckPassword2()
    {
        Assert.assertEquals(u.checkPassword("pass1"), false);
    }

    @Test
    public void testCheckPassword3()
    {
        Assert.assertEquals(u.checkPassword("pass1"), false);
    }

    @Test
    public void testCheckPassword4()
    {
        Assert.assertEquals(u.checkPassword("pass1"), false);
    }

    @Test
    public void testCheckPassword5()
    {
        Assert.assertEquals(u.checkPassword("pass1"), false);
    }

    @Test
    public void testCheckPassword6()
    {
        Assert.assertEquals(u.checkPassword("pass1"), false);
    }
}