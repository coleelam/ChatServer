import java.util.Comparator;

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

public class User implements Comparable<User>
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
        if (this.password.equals(password))
            return true;
        else
            return false;
    }

    public SessionCookie getCookie()
    {
        return cookie;
    }

    public void setCookie(SessionCookie cookie)
    {
        this.cookie = cookie;
    }


    @Override
    public int compareTo(User user) {
        return this.getName().compareTo(user.getName());
    }

    public static Comparator<User> UserNameComparator = new Comparator<User>() {
        public int compare(User user1, User user2)
        {
            return user1.getName().compareTo(user2.getName());
        }
    };
}