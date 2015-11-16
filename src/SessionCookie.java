
/**
 * Project 4 -- ChatServer -- SessionCookie
 *
 *
 *
 * @author Cole Elam, Benjamin Maxfield <(elamc@purdue.edu)> && <(bmaxfie@purdue.edu)>
 *
 * @lab 814 && 815
 *
 * @version 11/01/15
 */

public class SessionCookie
{
    private static final int TIMEOUT = 300; // seconds. Default

    private long UID;    // Personal (unique) identification number
    private double last_update;   // seconds, using System.currentTimeMillis()
    private int timeout = -1;    // Overwriting

    //      Randomization of the UIDs must occur "outside" of the SessionCookie class, as the SessionCookie constructor
    //      will be changed to accept (long id).

    public SessionCookie(long id)
    {
        this.UID = id;

        updateTimeOfActivity();
    }

    /**
     * Compares last_update to current time to check if the cookie has timed out from TIMEOUT or timeout, depending on
     *  if timeout != -1 (means TIMEOUT has not been overwritten).
     * @return - true if current time is lower than last_update + TIMEOUT/timeout, false otherwise.
     */
    public boolean hasTimedOut()
    {

        System.out.println((System.currentTimeMillis() / 1000.0) );
        System.out.println((last_update + timeout));

        if (timeout != -1) {
            if ((System.currentTimeMillis() / 1000.0) < (last_update + timeout)) {
                return false;
            }
        }
        else if ((System.currentTimeMillis() / 1000.0) < (last_update + TIMEOUT)) {
            return false;
        }

        return true;
    }

    public void updateTimeOfActivity()
    {
        last_update = (System.currentTimeMillis() / 1000.0);
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
