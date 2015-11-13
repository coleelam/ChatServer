
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
 * @version 11/01/15
 *
 */
public class ChatServer {

    private ArrayList<User> users;
    private CircularBuffer messages;

    public ChatServer(User[] users, int maxMessages) {
        this.users = new ArrayList<>();
        this.messages = new CircularBuffer(maxMessages);
        
        // Add contents of users[] passed in.
        this.users.addAll(Arrays.asList(users));

        // Add Default User ROOT:
        this.users.add(new User("root", "cs180", new SessionCookie()));
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
     * This function replaces the incorrectly inputed characters with their
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
     * @param request
     *            - the full line of the client request (CRLF included)
     * @return the server response
     */
    public String parseRequest(String request) {

        String response = null;
        String[] parsed = request.split("\t|\r\n");

        int errorCode = checkRequestValidity(parsed);

        // CHECK COOKIES HERE:

        if (errorCode == -1)
        // PERFORMS ACTIONS BASED ON COMMAND
            switch(COMMANDS.valueOf(parsed[0]))
            {
                case ADD_USER:
                    response = addUser(parsed);
                    break;
                case USER_LOGIN:
                    response = loginUser(parsed);
                    break;
                case POST_MESSAGE:
                    response = postMessage(parsed);
                    break;
                case GET_MESSAGES:
                    response = getMessages(parsed);
                    break;
                default:
                    break;
            }
        else
            System.out.println(MessageFactory.makeErrorMessage(errorCode));

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

        switch(COMMANDS.valueOf(parsed[0]))
        {
            // Checks for 3 parameters, param1 should be convertible to a long.
            case ADD_USER:
                try {
                    Long.parseLong(parsed[1]);
                    parsed[2].toString();
                    parsed[3].toString();
                } catch (NumberFormatException | IndexOutOfBoundsException e)
                {   code = MessageFactory.FORMAT_COMMAND_ERROR;  }
                break;
            // Checks for 2 parameters.
            case USER_LOGIN:
                try {
                    parsed[1].toString();
                    parsed[2].toString();
                } catch (IndexOutOfBoundsException e)
                {   code = MessageFactory.FORMAT_COMMAND_ERROR;  }
                break;
            // Checks for 2 parameters, param1 should be convertible to a long.
            case POST_MESSAGE:
                try {
                    Long.parseLong(parsed[1]);
                    parsed[2].toString();
                } catch (NumberFormatException | IndexOutOfBoundsException e)
                {   code = MessageFactory.FORMAT_COMMAND_ERROR;  }
                break;
            // Checks for 2 parameters, param1 should be convertible to a long. param2 should be convertible to an int.
            case GET_MESSAGES:
                try {
                    Long.parseLong(parsed[1]);
                    Integer.parseInt(parsed[2]);
                } catch (NumberFormatException | IndexOutOfBoundsException e)
                {   code = MessageFactory.FORMAT_COMMAND_ERROR;  }
                break;
            default:
                code = MessageFactory.UNKNOWN_COMMAND_ERROR;
                break;
        }

        return code;
    }



    /**
     * enum COMMANDS:
     *  Holds constants for user request commands (param0 in request).
     *
     *  Allows for cleaner code to be written when parsing request Strings.
     *      Specifically, we now can use switch cases instead of elseifs.
     */
    public enum COMMANDS {
        // VARS:
        ADD_USER("ADD-USER"),
        USER_LOGIN("USER-LOGIN"),
        POST_MESSAGE("POST-MESSAGE"),
        GET_MESSAGES("GET-MESSAGES");

        private final String command;

        COMMANDS(final String command)
        {
            this.command = command;
        }
        @Override
        public String toString()
        {
            return command;
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    ////                          Command Methods:                          ////
    ////////////////////////////////////////////////////////////////////////////

    private String addUser(String[] parsed)
    {
        String response = "SUCCESS\r\n";

        // Checks:
        //      Usernames and passwords can only contain alphanumerical values [A-Za-z0-9].
        for (String param : new String[]{parsed[2], parsed[3]})
            if (!param.matches("[A-Za-z0-9]"))
                response = "FAILURE\t" + MessageFactory.USER_ERROR + "\t" +
                        MessageFactory.makeErrorMessage(MessageFactory.USER_ERROR);
        // Checks:
        //      Usernames must be between 1 and 20 characters in length (inclusive).
        //      Password must be between 4 and 40 characters in length (inclusive).
        if (parsed[2].length() < 1 || parsed[2].length() > 20
                || parsed[3].length() < 4 || parsed[3].length() > 40)
            response = "FAILURE\t" + MessageFactory.USER_ERROR + "\t" +
                    MessageFactory.makeErrorMessage(MessageFactory.USER_ERROR);

        return response;
    }

    private String loginUser(String[] parsed)
    {
        String response = "SUCCESS\t";

        return response;
    }

    private String postMessage(String[] parsed)
    {
        String response = "SUCCESS\r\n";

        return response;
    }

    private String getMessages(String[] parsed)
    {
        String response = "SUCCESS\t";

        return response;
    }

}
