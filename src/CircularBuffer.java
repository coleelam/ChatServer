import java.util.Arrays;

/**
 * Project 4 -- ChatServer -- CircularBuffer
 *
 *
 *
 * @author Cole Elam, Benjamin Maxfield <(elamc@purdue.edu)> && <(bmaxfie@purdue.edu)>
 *
 * @lab 814 && 815
 *
 * @version 10/29/15
 */

public class CircularBuffer
{
    // INSTANCE VARS:
    private String[] buffer;

    private int size;
    private int currentIndex;
    private int messageCount;

    // CONSTRUCTORS:
    public CircularBuffer()
    {
        size = 25;
        buffer = new String[size];
        currentIndex = 0;
        messageCount = 0;
    }
    public CircularBuffer(int size)
    {
        this.size = size;
        buffer = new String[this.size];
        currentIndex = 0;
        messageCount = 0;
    }

    public int getMessageCount() {  return messageCount;   }

    /**
     * Puts message in the circular loop and replaces the oldest message in the loop.
     * @param message - message to be stored in the buffer.
     */
    public void put(String message)
    {
        if (message == null)
            return;

        buffer[currentIndex] = String.format("%04d) " + message, messageCount);

        if (messageCount >= 9999)
            messageCount = 0;
        else
            messageCount++;

        if (currentIndex >= buffer.length - 1)
            currentIndex = 0;
        else
            currentIndex++;
    }

    /**
     * Retrieves the freshest messages in the buffer (the one just behind the currentIndex).
     * @param numMessages - the number of messages to retrieve.
     * @return - the message -1 from currentIndex in buffer.
     */
    public String[] getNewest(int numMessages)
    {
        if (numMessages < 0)
            return null;

        if (numMessages == 0 || size <= 0 || messageCount <= 0 || (currentIndex == 0 && buffer[size - 1] == null)
                             || (currentIndex != 0 && buffer[currentIndex - 1] == null))
            return new String[0];

        // Makes sure numMessages isn't more than the size of the buffer.
        numMessages = Math.min(numMessages, size);

        String[] messages = new String[numMessages];    // Inits return array

        // Gets messages in proper order
        int temp = currentIndex - 1;   // temp = Holds the reverse iterator of buffer.
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
