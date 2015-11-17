import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.jws.soap.SOAPBinding;

/**
 * <b> CS 180 - Project 4 - Chat Server Test Samples </b>
 * <p>
 *
 *
 * @author Cole Elam and Benjamin Maxfield <(elamc@purdue.edu)> <(bmaxfie@purdue.edu)>
 *
 * @lab 814 and 815
 *
 * @version 11/16/15
 *
 */
public class Project4SampleTest {

	User u = null;
	CircularBuffer buffer;

	@Before
	public void setUp() {
		SessionCookie.timeoutLength = 300;

		u = new User("user", "pass1!", new SessionCookie(1111));

		buffer = new CircularBuffer(4);
		buffer.put("first");
		buffer.put("second");
		buffer.put("third");
		buffer.put("fourth");

	}

	// Auxilary function
	private static String  verifyErrorMessage(String msg, int code) {

		if (!msg.endsWith("\r\n"))
			return "Invalid Error Message Format";

		String[] st = msg.split("\t");
		if (st.length != 3) {
			return "Invalid Error Message Format";
		}
		String ta = String.valueOf(code);

		if (!"FAILURE".equals(st[0]))
			return "Invalid Error Message Format";
		if (!ta.equals(st[1]))
			return String.format("expected error code %d, but received %s", code, st[1]);

		return "";
	}

	private static boolean verifyMessages(String res, int idx, String user, String msg) {

		int idxP = res.indexOf(')');
		if (idxP < 0)
			return false;

		int idxS = res.indexOf(':');
		if (idxS < 0)
			return false;

		String number = res.substring(0, idxP).trim();
		String name = res.substring(idxP + 1, idxS).trim();
		String m = res.substring(idxS + 1).trim();

		return number.length() == 4 && name.equals(user) && msg.equals(m) && String.format("%04d", idx).equals(number);
	}

	/********************************************************************************************************
	 *
	 * SessionCookie
	 *
	 ********************************************************************************************************/

	@Test
	public void testTimedOut1() throws InterruptedException
	{
		SessionCookie c = new SessionCookie((long) 0001);
		c.setTimeout(1);
		c.updateTimeOfActivity();
		Thread.sleep(1200);
		Assert.assertEquals(c.hasTimedOut(), true);
	}
	@Test
	public void testTimedOut2() throws InterruptedException
	{
		SessionCookie c = new SessionCookie((long) 0001);
		c.setTimeout(1);
		c.updateTimeOfActivity();
		Thread.sleep(1000);
		Assert.assertEquals(c.hasTimedOut(), true);
	}
	@Test
	public void testTimedOut3() throws InterruptedException
	{
		SessionCookie c = new SessionCookie((long) 0001);
		c.setTimeout(1);
		c.updateTimeOfActivity();
		Thread.sleep(800);
		Assert.assertEquals(c.hasTimedOut(), false);
	}
	@Test
	public void testTimedOut4() throws InterruptedException
	{
		SessionCookie c = new SessionCookie((long) 0001);
		c.setTimeout(3);
		c.updateTimeOfActivity();
		Thread.sleep(3100);
		Assert.assertEquals(c.hasTimedOut(), true);
	}
	@Test
	public void testTimedOut5() throws InterruptedException
	{
		SessionCookie c = new SessionCookie((long) 0001);
		c.setTimeout(30);
		c.updateTimeOfActivity();
		Thread.sleep(31000);
		Assert.assertEquals(c.hasTimedOut(), true);
	}
	@Test
	public void testTimedOut6() throws InterruptedException
	{
		SessionCookie c = new SessionCookie((long) 0001);
		c.setTimeout(30);
		c.updateTimeOfActivity();
		Thread.sleep(29000);
		Assert.assertEquals(c.hasTimedOut(), false);
	}
	@Test
	public void testSessionCookieTimeOut() throws InterruptedException {
		//change timeout interval to 1 second

		SessionCookie cookie = new SessionCookie(0001);

		cookie.setTimeout(1);

		System.out.println(cookie.hasTimedOut()); // should print false

		Thread.sleep(1200); // Sleep for 1.2 seconds

		System.out.println(cookie.hasTimedOut()); // should print true
	}

	/********************************************************************************************************
	 *
	 * CircularBuffer
	 *
	 ********************************************************************************************************/

	@Test
	public void test1() throws Exception {
		Assert.assertEquals(buffer.getNewest(1)[0], "0003) fourth");
	}
	@Test
	public void test2() throws Exception {
		String[] expected = {"0001) second", "0002) third", "0003) fourth"};
		Assert.assertArrayEquals(buffer.getNewest(3), expected);
	}
	@Test
	public void test3() throws Exception {
		buffer.put("fifth");
		String[] expected = {"0003) fourth", "0004) fifth"};
		Assert.assertArrayEquals(buffer.getNewest(2), expected);
	}
	@Test
	public void test4() throws Exception {
		CircularBuffer trash = new CircularBuffer(0);
		String[] expected = null;
		Assert.assertNull(trash.getNewest(1).toString().trim(), expected);
	}
	@Test
	public void test5() throws Exception {
		CircularBuffer trash = new CircularBuffer(1);
		String[] expected = null;
		Assert.assertNull(trash.getNewest(1).toString().trim(), expected);
	}
	@Test
	public void test6() throws Exception {
		CircularBuffer trash = new CircularBuffer(1);
		trash.put("bad");
		trash.put("good");
		String[] expected = {"0001) good"};
		Assert.assertArrayEquals(trash.getNewest(2), expected);
	}

	/********************************************************************************************************
	 *
	 * User
	 *
	 ********************************************************************************************************/

	@Test
	public void testCheckPassword1()
	{
		Assert.assertEquals(u.checkPassword("pass1"), false);
	}

	@Test
	public void testCheckPassword2()
	{
		Assert.assertEquals(u.checkPassword("pass1!"), true);
	}

	@Test
	public void testCheckPassword3()
	{
		Assert.assertEquals(u.checkPassword("Pass1!"), false);
	}

	@Test
	public void testCheckPassword4()
	{
		Assert.assertEquals(u.checkPassword("pass2!"), false);
	}

	@Test
	public void testCheckPassword5()
	{
		Assert.assertEquals(u.checkPassword("pAss1!"), false);
	}

	@Test
	public void testCheckPassword6()
	{
		Assert.assertEquals(u.checkPassword("pas1!"), false);
	}

	/********************************************************************************************************
	 *
	 * ChatServer.addUser
	 *
	 ********************************************************************************************************/

	@Test(timeout = 1000)
	public void testAddUserNominal() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.addUser(new String[] { "ADD-USER", "42", "cs240", "hereicome" });
		String ta = "SUCCESS\r\n";

		assertEquals(
				"ChatServer: 'addUser' doesn't return correct success message or didn't succeed when it should have.",
				ta, student);

		student = chatServer.userLogin(new String[] { "USER-LOGIN", "cs240", "hereicome" });
		assertTrue("ChatServer: 'addUser' test fails because 'userLoggin' can not log with the newly added user",
				student.startsWith("SUCCESS"));
	}

	@Test(timeout = 1000)
	public void testAddUserInvalidUsername() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.addUser(new String[] { "ADD-USER", "42", "", "hereicome" });
		String msg = verifyErrorMessage(student, MessageFactory.INVALID_VALUE_ERROR);
		assertTrue("addUser:" + msg, "".equals(msg));

		student = chatServer.addUser(new String[] { "ADD-USER", "42", "aaa-bbb", "hereicome" });
		msg = verifyErrorMessage(student, MessageFactory.INVALID_VALUE_ERROR);
		assertTrue("addUser:" + msg, "".equals(msg));
	}

	@Test(timeout = 1000)
	public void testAddUserInvalidPassword() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.addUser(new String[] { "ADD-USER", "42", "cs240", "hereicome!" });
		String msg = verifyErrorMessage(student, MessageFactory.INVALID_VALUE_ERROR);
		assertTrue("addUser:" + msg, "".equals(msg));

		student = chatServer.addUser(new String[] { "ADD-USER", "42", "ee", "herei(c)ome" });
		msg = verifyErrorMessage(student, MessageFactory.INVALID_VALUE_ERROR);
		assertTrue("addUser:" + msg, "".equals(msg));
	}

	/********************************************************************************************************
	 *
	 * ChatServer.userLogin
	 *
	 ********************************************************************************************************/
	@Test(timeout = 1000)
	public void testRootUser() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.userLogin(new String[] { "USER-LOGIN", "root", "cs180" });
		assertTrue("ChatServer: can not log as root user", student.matches("SUCCESS\t\\d+\r\n"));
	}

	@Test(timeout = 1000)
	public void testUserLoginNominal() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", null);
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.userLogin(new String[] { "USER-LOGIN", "greg", "greg" });
        assertTrue("ChatServer: 'userLogin' incorrect response format", student.endsWith("\r\n"));
		assertTrue("ChatServer: 'userLogin' can not log in with a valid user", student.matches("SUCCESS\t\\d+\r\n"));
	}

	/********************************************************************************************************
	 *
	 * ChatServer.postMessage
	 *
	 ********************************************************************************************************/

	@Test(timeout = 1000)
	public void testPostMessageNominal() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String ta = "SUCCESS\r\n";
		String student = chatServer.postMessage(new String[] { "POST-MESSAGE", "42", "Hello, world!!" },  "varun");

		assertEquals("ChatServer: 'postMessage' failed when it shouldn't have.",
				ta, student);
	}

	@Test(timeout = 1000)
	public void testPostMessageEmptyMessage() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.postMessage(new String[] { "POST-MESSAGE", "42", " " }, "greg");
		String msg = verifyErrorMessage(student, MessageFactory.INVALID_VALUE_ERROR);
		assertTrue("postMessage:" + msg, "".equals(msg));
	}

	/********************************************************************************************************
	 *
	 * ChatServer.getMessages
	 *
	 ********************************************************************************************************/

	@Test(timeout = 1000)
	public void testGetMessagesNominal() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String msg = "Hello, world!!";

		String ta = "SUCCESS\r\n";
		String student = chatServer.postMessage(new String[] { "POST-MESSAGE", "42", msg },  "greg");

		assertEquals("ChatServer: 'getMessage' test failed because 'postMessage' did not work", ta, student);

		student = chatServer.getMessages(new String[] { "GET-MESSAGES", "42", "1" });

		assertTrue("ChatServer: 'getMessage' incorrect response format", student.endsWith("\r\n"));

		String[] tab = student.trim().split("\t");
		assertEquals("ChatServer: 'getMessage' invalid return value (1 msg sent, 1 requested)", 2,
				tab.length);
		assertTrue("ChatServer: 'getMessage' invalid return value (1 msg sent, 1 requested)",
				verifyMessages(tab[1], 0, "greg", msg));
	}

	/********************************************************************************************************
	 *
	 * ChatServer.parseRequest
	 *
	 ********************************************************************************************************/
	@Test(timeout = 1000)
	public void testAddUserWrongFormat() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.parseRequest("ADD-USER\t42\tcs240\thereicome\tmoreparam\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));

		student = chatServer.parseRequest("ADD-USER\r\n");
		msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));
	}

	@Test(timeout = 1000)
	public void testUserLoginWrongFormat() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.parseRequest("USER-LOGIN\troot\tcs180\tmoreparam\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));

		student = chatServer.parseRequest("USER-LOGIN\trootu\r\n");
		msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));
	}

	@Test(timeout = 1000)
	public void testGetMessageWrongFormat() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.parseRequest("GET-MESSAGES\t42\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));

		student = chatServer.parseRequest("GET-MESSAGES\r\n");
		msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));
	}

	@Test(timeout = 1000)
	public void testPostMessageWrongFormat() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.parseRequest("POST-MESSAGE\t42\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));

		student = chatServer.parseRequest("POST-MESSAGE\r\n");
		msg = verifyErrorMessage(student, MessageFactory.FORMAT_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));
	}

	@Test(timeout = 1000)
	public void testInvalidCommand() {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		String student = chatServer.parseRequest("GET-MESSAGE\t42\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.UNKNOWN_COMMAND_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));
	}

	@Test(timeout = 1000)
	public void testCookieValidity() throws InterruptedException {
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		SessionCookie.timeoutLength = 0;
		Thread.sleep(10);
		String student = chatServer.parseRequest("GET-MESSAGES\t42\t1\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.COOKIE_TIMEOUT_ERROR);
		assertTrue("parseRequest:" + msg, "".equals(msg));

		student = chatServer.parseRequest("GET-MESSAGES\t42\t1\r\n");
		msg = verifyErrorMessage(student, MessageFactory.LOGIN_ERROR);
		assertTrue("parseRequest:" + msg + " (after timeout)", "".equals(msg));
	}

	@Test(timeout = 10000)
	public void testUpdateCookieGetMessage() throws InterruptedException {

		/* Get messages */
		User[] users = new User[1];
		users[0] = new User("greg", "greg", new SessionCookie(42));
		ChatServer chatServer = new ChatServer(users, 100);

		SessionCookie.timeoutLength = 1;
		Thread.sleep(800);
		String student = chatServer.parseRequest("GET-MESSAGES\t42\t1\r\n");
		assertTrue("ChatServer: 'parseRequest' test failed because 'GET-MESSAGES' failed.",
				student.startsWith("SUCCESS"));
		Thread.sleep(400);
		student = chatServer.parseRequest("GET-MESSAGES\t42\t0\r\n");
		String msg = verifyErrorMessage(student, MessageFactory.COOKIE_TIMEOUT_ERROR);
		assertTrue("Check your Cookie Management:" + msg, "".equals(msg));
	}
}
