package it.xpug.kata.birthday_greetings;

import static org.junit.Assert.*;

import org.junit.*;

import com.dumbster.smtp.*;


public class BirthayServicesShould {

	private static final int NONSTANDARD_PORT = 9999;
    private final String host = "localhost";
    private BirthdayService birthdayService;
	private SimpleSmtpServer mailServer;
    private String employees = "employee_data.txt";

    @Before
	public void setUp() throws Exception {
		mailServer = SimpleSmtpServer.start(NONSTANDARD_PORT);
		birthdayService = new BirthdayService();
	}

	@After
	public void tearDown() throws Exception {
		mailServer.stop();
		Thread.sleep(200);
	}

    @Test
    public void send_a_greet_message_on_employee_birthday() throws Exception {
        BirthdayService service = new BirthdayService(employees, host, NONSTANDARD_PORT);
        service.sendGreetings(new XDate("2008/10/08"));
        assertEquals("message not sent?", 1, mailServer.getReceivedEmailSize());
        SmtpMessage message = (SmtpMessage) mailServer.getReceivedEmail().next();
        assertEquals("Happy Birthday, dear John!", message.getBody());
        assertEquals("Happy Birthday!", message.getHeaderValue("Subject"));
        String[] recipients = message.getHeaderValues("To");
        assertEquals(1, recipients.length);
        assertEquals("john.doe@foobar.com", recipients[0].toString());

    }

    @Test
	public void not_send_emails_when_nobodys_birthday() throws Exception {
        BirthdayService service = new BirthdayService(employees, host, NONSTANDARD_PORT);
        service.sendGreetings(new XDate("2008/01/01"));
        assertEquals("what? messages?", 0, mailServer.getReceivedEmailSize());
    }
}
