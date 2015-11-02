/**
 * Project 4 -- ChatServer -- User
 *
 *
 *
 * @author Cole Elam, Benjamin Maxfield <(elamc@purdue.edu)> && <(bmaxfie@purdue.edu)>
 *
 * @lab 814 && 815
 *
 * @version 11/01/15
 */

public class User
{
    private String username;
    private String password;
    private SessionCookie cookie;

    public User(String username, String password, SessionCookie cookie)
    {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
    }

    public String getName()
    {
        return username;
    }

    public boolean checkPassword(String password)
    {
        return false;
    }

    public SessionCookie getCookie()
    {
        return cookie;
    }

    public void setCookie(SessionCookie cookie)
    {

    }
}