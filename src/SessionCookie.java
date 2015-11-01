import java.util.ArrayList;

/**
 * Project 4 -- ChatServer -- SessionCookie
 *
 *
 *
 * @author Ben Maxfield, 815
 *
 * @version 11/01/15
 */

public class SessionCookie
{
    private static final int TIMEOUT = 300; // seconds. Default

    private static ArrayList<Integer> IDs;   // List of all currently used identification numbers. (may need to be synchronized)

    private int UID;    // Personal (unique) identification number
    private long last_update;   // seconds, using System.currentTimeMillis()
    private int timeout = -1;    // Overwriting


    public SessionCookie()
    {
        UID = 0; // Change to check IDs for duplicate after generating Random.
        IDs.add(new Integer(UID));

        updateTimeOfActivity();
    }

    /**
     * Compares last_update to current time to check if the cookie has timed out from TIMEOUT or timeout, depending on
     *  if timeout != -1 (means TIMEOUT has not been overwritten).
     * @return - true if current time is lower than last_update + TIMEOUT/timeout, false otherwise.
     */
    public boolean hasTimedOut()
    {
        return false;
    }

    public void updateTimeOfActivity()
    {
        last_update = (long) (System.currentTimeMillis() / 1000.0);
    }

    public long getID()
    {
        return UID;
    }

    /**
     * Method lets you override the default timeout.
     * Primarily used for debugging...
     * @param timeout - timeout must be >= 1, seconds.
     * @return - true if was able to override the default TIMEOUT, otherwise false.
     */
    public boolean setTimeout(int timeout)
    {
        if (timeout < 1)
            return false;

        this.timeout = timeout;
        return true;
    }
}
