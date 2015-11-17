
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

    private long uID;    // Personal (unique) identification number
    private double lastUpdate;   // seconds, using System.currentTimeMillis()
    public static int timeoutLength = -1;    // Overwriting, TODO: May want to be changed to private.

    //      Randomization of the UIDs must occur "outside" of the SessionCookie class, as the SessionCookie constructor
    //      will be changed to accept (long id).

    public SessionCookie(long id)
    {
        this.uID = id;

        updateTimeOfActivity();
    }

    /**
     * Compares lastUpdate to current time to check if the cookie has timed out from
     * TIMEOUT or timeoutLength, depending on
     *  if timeoutLength != -1 (means TIMEOUT has not been overwritten).
     * @return - true if current time is lower than lastUpdate + TIMEOUT/timeoutLength, false otherwise.
     */
    public boolean hasTimedOut()
    {
        if (timeoutLength != -1) {
            if ((System.currentTimeMillis() / 1000.0) < (lastUpdate + timeoutLength)) {
                return false;
            }
        }
        else if ((System.currentTimeMillis() / 1000.0) < (lastUpdate + TIMEOUT)) {
            return false;
        }

        return true;
    }

    public void updateTimeOfActivity()
    {
        lastUpdate = (System.currentTimeMillis() / 1000.0);
    }

    public long getID()
    {
        return uID;
    }

    /**
     * Method lets you override the default timeoutLength.
     * Primarily used for debugging...
     * @param timeout - timeoutLength must be >= 1, seconds.
     * @return - true if was able to override the default TIMEOUT, otherwise false.
     */
    public boolean setTimeout(int timeout)
    {
        if (timeout < 1)
            return false;

        this.timeoutLength = timeout;
        return true;
    }
}
