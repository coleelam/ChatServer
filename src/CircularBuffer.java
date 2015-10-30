import java.util.Arrays;

/**
 * Project 4 -- ChatServer -- CircularBuffer
 *
 *
 *
 * @author Ben Maxfield, 815
 *
 * @version 10/29/15
 */

public class CircularBuffer
{
    // INSTANCE VARS:
    private String[] buffer;

    private int size;
    private int current_index;
    private int message_count;

    // CONSTRUCTORS:
    public CircularBuffer()
    {
        size = 25;
        buffer = new String[size];
        current_index = 0;
        message_count = 0;
    }
    public CircularBuffer(int size)
    {
        this.size = size;
        buffer = new String[this.size];
        current_index = 0;
        message_count = 0;
    }

    /**
     * Puts message in the circular loop and replaces the oldest message in the loop.
     * @param message - message to be stored in the buffer.
     */
    public void put(String message)
    {
        if (message == null)
            return;

        buffer[current_index] = message;

        if (current_index >= buffer.length - 1)
            current_index = 0;
        else
            current_index++;
        message_count++;
    }

    /**
     * Retrieves the freshest messages in the buffer (the one just behind the current_index).
     * @param numMessages - the number of messages to retrieve.
     * @return - the message -1 from current_index in buffer.
     */
    public String[] getNewest(int numMessages)
    {
        if (numMessages <= 0 || size <= 0 || (current_index == 0 && buffer[size - 1] == null)
                             || (current_index != 0 && buffer[current_index - 1] == null))
            return null;

        // Makes sure numMessages isn't more than the size of the buffer.
        numMessages = Math.min(numMessages, size);

        String[] messages = new String[numMessages];    // Inits return array

        // Gets messages in proper order
        int temp = current_index - 1;   // temp = Holds the reverse iterator of buffer.
        for (int i = numMessages - 1; i >= 0; i--)   // i = Holds the reverse iterator of messages.
        {
            if (temp < 0)
                temp = size - 1;
            messages[i] = buffer[temp];
            temp--;
        }

        // Finds first nonNull index in messages
        int firstNonNull = -1;
        for (int i = 0; i < numMessages && firstNonNull == -1; i++)
            if (messages[i] != null)
                firstNonNull = i;

        // Cuts out null in messages.
        if (firstNonNull != -1)
            messages = Arrays.copyOfRange(messages, firstNonNull, messages.length);

        return messages;
    }

    // TESTs general functionality of CircularBuffer with debug writeout.
    /*public static void main(String[] args)
    {
        CircularBuffer buffer = new CircularBuffer(5);
        buffer.put("1");
        System.out.println(Arrays.toString(buffer.getNewest(1)));
        buffer.put("2");
        buffer.put("3");
        buffer.put("4");
        System.out.println(Arrays.toString(buffer.getNewest(5)));
        buffer.put("5");
        buffer.put("6");
        System.out.println(Arrays.toString(buffer.getNewest(3)));
    }*/

}
