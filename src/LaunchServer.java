import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;

public class LaunchServer {

    /**
     * This main method is for testing purposes only.
     * @param args - the command line arguments
     */
    public static void main(String[] args) {
        // Create a ChatServer and start it
        (new ChatServer(new User[0], 200)).run();
    }

}
