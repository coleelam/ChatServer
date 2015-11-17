
import java.util.*;

/**
 * <b> CS 180 - Project 4 - Chat Server Skeleton </b>
 * <p>
 *
 * This is the skeleton code for the ChatServer Class. This is a private chat
 * server for you and your friends to communicate.
 *
 * @author Cole Elam, Benjamin Maxfield <(elamc@purdue.edu)> && <(bmaxfie@purdue.edu)>
 *
 * @lab 814 && 815
 *
 * @version 11/13/15
 */
public class ChatServer {

    private ArrayList<User> users;
    private CircularBuffer messages;

    public ChatServer(User[] users, int maxMessages) {
        this.users = new ArrayList<>();
        this.messages = new CircularBuffer(maxMessages);

        User root = new User("root", "cs180", null);

        // Add contents of users[] passed in.
        this.users.addAll(Arrays.asList(users));
        Collections.sort(this.users, User.userNameComparator);

        // Cannot binary search unless there's more than 1 User in this.users.
        if (users.length > 0) {
            // Add Default User ROOT:
            int index = Collections.binarySearch(this.users, root);
            this.users.add(-index - 1, root);
        }
        else
            this.users.add(root);

    }

    /**
     * This method begins server execution.
     */
    public void run() {
        boolean verbose = false;
        System.out.printf("The VERBOSE option is off.\n\n");
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.printf("Input Server Request: ");
            String command = in.nextLine();

            // this allows students to manually place "\r\n" at end of command
            // in prompt
            command = replaceEscapeChars(command);

            if (command.startsWith("kill"))
                break;

            if (command.startsWith("verbose")) {
                verbose = !verbose;
                System.out.printf("VERBOSE has been turned %s.\n\n", verbose ? "on" : "off");
                continue;
            }

            String response = null;
            try {
                response = parseRequest(command);
            } catch (Exception ex) {
                response = MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR,
                        String.format("An exception of %s occurred.", ex.getMessage()));
                ex.printStackTrace();
            }

            // change the formatting of the server response so it prints well on
            // the terminal (for testing purposes only)
            if (response.startsWith("SUCCESS\t"))
                response = response.replace("\t", "\n");

            // print the server response
            if (verbose)
                System.out.printf("response:\n");
            System.out.printf("\"%s\"\n\n", response);
        }

        in.close();
    }

    /**
     * Replaces "poorly formatted" escape characters with their proper values.
     * For some terminals, when escaped characters are entered, the terminal
     * includes the "\" as a character instead of entering the escape character.
     * This function replaces the incorrectly inputted characters with their
     * proper escaped characters.
     *
     * @param str
     *            - the string to be edited
     * @return the properly escaped string
     */
    private static String replaceEscapeChars(String str) {
        str = str.replace("\\r", "\r");
        str = str.replace("\\n", "\n");
        str = str.replace("\\t", "\t");

        return str;
    }

    /**
     * Determines which client command the request is using and calls the
     * function associated with that command.
     *
     * @param request - the full line of the client request (CRLF included)
     * @return the server response
     */
    public String parseRequest(String request) {

        String response = null;
        String[] parsed = request.split("\t|\r\n");

        int errorCode = checkRequestValidity(parsed);

        User thisUser;
        if (errorCode == -1)
        // PERFORMS ACTIONS BASED ON COMMAND
            switch (COMMANDS.valueOf(parsed[0])) {
                case ADD_USER:
                    thisUser = findUser(parsed[1]);
                    if (thisUser == null) {
                        response = MessageFactory.makeErrorMessage(MessageFactory.LOGIN_ERROR);
                        break;
                    } else if (thisUser.getCookie().hasTimedOut()) {
                        response = userTimedOut(thisUser);
                        break;
                    }
                    response = addUser(parsed);
                    thisUser.getCookie().updateTimeOfActivity();
                    break;
                case USER_LOGIN:
                    response = userLogin(parsed);
                    break;
                case POST_MESSAGE:
                    thisUser = findUser(parsed[1]);
                    if (thisUser == null) {
                        response = MessageFactory.makeErrorMessage(MessageFactory.LOGIN_ERROR);
                        break;
                    } else if (thisUser.getCookie().hasTimedOut()) {
                        response = userTimedOut(thisUser);
                        break;
                    }
                    response = postMessage(parsed, findUsername(parsed[1]));
                    thisUser.getCookie().updateTimeOfActivity();
                    break;
                case GET_MESSAGES:
                    thisUser = findUser(parsed[1]);
                    if (thisUser == null) {
                        response = MessageFactory.makeErrorMessage(MessageFactory.LOGIN_ERROR);
                        break;
                    } else if (thisUser.getCookie().hasTimedOut()) {
                        response = userTimedOut(thisUser);
                        break;
                    }
                    response = getMessages(parsed);
                    break;
                default:
                    break;
            }
        else
            response = MessageFactory.makeErrorMessage(errorCode);

        return response;
    }
    /**
     *  Used in parseRequest() to perform initial validation of the user's request.
     *
     *  @param parsed - pre-split request into a String[] of exact length for command.
     *
     *  @return an error code for particular failed tests of the supplied String[] parsed.
     *  @return -1 if no error code.
     */
    private int checkRequestValidity(String[] parsed)
    {
        int code = -1;

        // This replaces the dashes in the input commands to fit the enum names w/ underscores.
        parsed[0] = parsed[0].replace('-', '_');

        System.out.println(Arrays.toString(parsed));

        try {
            switch(COMMANDS.valueOf(parsed[0]))
            {
                // Checks for 3 parameters, param1 should be convertible to a long.
                case ADD_USER:
                    try {
                        Long.parseLong(parsed[1]);
                        parsed[2].toString();
                        parsed[3].toString();
                        if (parsed.length > 4)
                            throw new NumberFormatException();
                    } catch (NumberFormatException | IndexOutOfBoundsException e)
                    {   return MessageFactory.FORMAT_COMMAND_ERROR;  }
                    break;
                // Checks for 2 parameters.
                case USER_LOGIN:
                    try {
                        parsed[1].toString();
                        parsed[2].toString();
                        if (parsed.length > 3)
                            throw new IndexOutOfBoundsException();
                    } catch (IndexOutOfBoundsException e)
                    {   return MessageFactory.FORMAT_COMMAND_ERROR;  }
                    break;
                // Checks for 2 parameters, param1 should be convertible to a long.
                case POST_MESSAGE:
                    try {
                        Long.parseLong(parsed[1]);
                        parsed[2].toString();
                        if (parsed.length > 3)
                            throw new NumberFormatException();
                    } catch (NumberFormatException e)
                    {   return MessageFactory.FORMAT_COMMAND_ERROR;  }
                    catch (IndexOutOfBoundsException ioobe)
                    {
                        return MessageFactory.INVALID_VALUE_ERROR;
                    }
                    break;
                // Checks for 2 parameters, param1 should be convertible to a long.
                // param2 should be convertible to an int.
                case GET_MESSAGES:
                    try {
                        Long.parseLong(parsed[1]);
                        Integer.parseInt(parsed[2]);
                        if (parsed.length > 3)
                            throw new NumberFormatException();
                    } catch (NumberFormatException | IndexOutOfBoundsException e)
                    {   return MessageFactory.FORMAT_COMMAND_ERROR;  }
                    break;
                default:
                    code = MessageFactory.UNKNOWN_COMMAND_ERROR;
                    break;
            }
        } catch (IllegalArgumentException iae)
        {
            code = MessageFactory.UNKNOWN_COMMAND_ERROR;
        }

        return code;
    }

    /**
     * Looks through the users ArrayList (linearly) to find the User with the given cookieID. Returns that User's
     * username.
     *
     * @param cookieID - String that contains a valid Long, in the format of a SessionCookie UID.
     * @return - the username of the User with the cookieID, if cookieID is null or if User doesn't exist, returns null.
     */
    private String findUsername(String cookieID)
    {
        if (cookieID == null)
            return null;

        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getCookie() != null && users.get(i).getCookie().getID() == Long.parseLong(cookieID))
                return users.get(i).getName();

        return null;
    }

    /**
     *  Looks through the users ArrayList (linearly) to find the User with the given cookieID. Returns that User.
     *
     *  @param cookieID - String that contains a valid Long, in the format of a SessionCookie UID.
     *  @return - the User object that has the matching cookieID. If cookieID is null or if User doesn't exist,
     *  returns null;
     */
    private User findUser(String cookieID)
    {
        if (cookieID == null)
            return null;

        for (int i = 0; i < users.size(); i++)
            if (users.get(i).getCookie() != null && users.get(i).getCookie().getID() == Long.parseLong(cookieID))
                return users.get(i);

        return null;
    }



    /**
     * enum COMMANDS:
     *  Holds constants for user request commands (param0 in request).
     *
     *  Allows for cleaner code to be written when parsing request Strings.
     *      Specifically, we now can use switch cases instead of elseifs.
     */
    public enum COMMANDS
    {
        // VARS:
        ADD_USER,
        USER_LOGIN,
        POST_MESSAGE,
        GET_MESSAGES

    }


    ////////////////////////////////////////////////////////////////////////////
    ////                          Command Methods:                          ////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Command method for ADD-USER command. Validates the parameters, checks for valid UID parameter, and if request
     *  passes these, then adds a new User to users.
     *
     * @param parsed - pre-parsed semi-validated String[] request
     * @return - properly formated response String.
     */
    public String addUser(String[] parsed)
    {
        String response = "SUCCESS\r\n";

        // Checks:
        //      Usernames and passwords can only contain alphanumerical values [A-Za-z0-9].
        for (String param : new String[]{parsed[2], parsed[3]})
            if (!param.matches("[A-Za-z0-9]+"))
                return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR,
                        "the username or password does " +
                                "not fit this regex: [A-Za-z0-9].");

        // Checks:
        //      Usernames must be between 1 and 20 characters in length (inclusive).
        //      Password must be between 4 and 40 characters in length (inclusive).
        if (parsed[2].length() < 1 || parsed[2].length() > 20
                || parsed[3].length() < 4 || parsed[3].length() > 40)
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR,
                    "the username or password does not " +
                            "fit the length requirements: 0 < username < 21 && 3 < password < 41.");

        // Finally:
        //      If this call passed the requirements... Add the User with specified CookieID or if not given, randomly
        //      generated one!
        if (response.equals("SUCCESS\r\n"))
        {
            User newUser = new User(parsed[2], parsed[3], null);
            if (users.size() > 1) {
                int index = Collections.binarySearch(users, newUser);
                // Checks:
                //      Username doesn't already exist.
                if (index >= 0)
                    return MessageFactory.makeErrorMessage(MessageFactory.USER_ERROR);
                else {
                    users.add(-index - 1, newUser);
                    Collections.sort(users);
                }
            }
            else {
                for (int i = 0; i < users.size(); i++)
                    // Make sure nothing is supposed to happen after this return...
                    if (users.get(i).getName().equals(parsed[2]))
                        return MessageFactory.makeErrorMessage(MessageFactory.USER_ERROR);
                users.add(newUser);
                Collections.sort(users);
            }
        }

        return response;
    }

    public String userLogin(String[] parsed)
    {
        String response = "SUCCESS\t";
        boolean validPass = false;
        boolean notLoggedIn = false;


        // Check for: User Exists -> Correct Password -> Null SessionCookie.
        int index = -1;
        User dummy = new User(parsed[1], parsed[2], null);
        if (users.size() > 1)
        {
            index = Collections.binarySearch(users, dummy);
        }
        else
            for (int i = 0; i < users.size(); i++)
                if (users.get(i).getName().equals(parsed[1]))
                    index = i;
        // If username EXISTS:
        if (index >= 0)
        {
            // If CORRECT password:
            if (users.get(index).checkPassword(parsed[2]))
                validPass = true;
            else
                return MessageFactory.makeErrorMessage(MessageFactory.AUTHENTICATION_ERROR);

            // If NULL SessionCookie:
            if (users.get(index).getCookie() == null && validPass)
                notLoggedIn = true;
            else
                response = MessageFactory.makeErrorMessage(MessageFactory.USER_CONNECTED_ERROR);

            // If ALL of the Above:
            if (validPass && notLoggedIn) {
                users.get(index).setCookie(new SessionCookie(setUniqueID()));
                response += users.get(index).getCookie().getID() + "\r\n";
            }
        }
        else
            response = MessageFactory.makeErrorMessage(MessageFactory.USERNAME_LOOKUP_ERROR);


        return response;
    }
    public String postMessage(String[] parsed, String name)
    {
        String response = MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR,
                        "Invalid message post format.");

        if (name == null)
            return MessageFactory.makeErrorMessage(MessageFactory.USERNAME_LOOKUP_ERROR);

        String message = null;

        //makes sure the trimmed string has a length greater than 1, so that blank messages can't be posted.
        if (parsed[2].trim().length() >= 1)
        {
            message = name + ": " + parsed[2];
            messages.put(message);
            response = "SUCCESS\r\n";
            return response;
        }

        return response;
    }

    public String getMessages(String[] parsed)
    {
        int numMessages = 0;

        String response = MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR,
                "Invalid getMessages request");
        try {

            numMessages = Integer.parseInt(parsed[2]);

        } catch (NumberFormatException e) {

            return response;

        }

        String[] messageRequest = messages.getNewest(numMessages);


        if (numMessages >= 1 && messageRequest != null)
        {
            response = "SUCCESS";
            for (int j = 0; j < messageRequest.length; j++)
            {
                response += "\t" + messageRequest[j];
            }
            response += "\r\n";
            return response;
        }

        return response;
    }


    ////////////////////////////////////////////////////////////////////
    ////                 SessionCookieID Generation                 ////
    ////////////////////////////////////////////////////////////////////

    private long generateRandomID()
    {

        long id = (long)((Math.random() * 9999) + 1);
        return id;

    }

    private long setUniqueID()
    {

        boolean uniqueID;
        long cookieID;

        do {
            uniqueID = true;
            cookieID = generateRandomID();

            for (User user : users)
            {
                if (user.getCookie() != null && user.getCookie().getID() == cookieID)
                {
                    uniqueID = false;
                    break;
                }

            }
        } while (!uniqueID);

        return cookieID;
    }

    private String userTimedOut(User user)
    {
        user.setCookie(null);
        return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
    }

}
