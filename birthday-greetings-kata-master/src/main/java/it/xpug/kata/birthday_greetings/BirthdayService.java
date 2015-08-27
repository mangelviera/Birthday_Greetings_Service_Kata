package it.xpug.kata.birthday_greetings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BirthdayService {
    private final MailService mailService;
    private String employees;

    public BirthdayService(String employees, MailService mailService) {
        this.employees = employees;
        this.mailService = mailService;
    }

    public void sendGreetings(XDate xDate) throws IOException, ParseException, AddressException, MessagingException {
        BufferedReader in = new BufferedReader(new FileReader(employees));
        String str = "";
        str = in.readLine(); // skip header
        while ((str = in.readLine()) != null) {
            String[] employeeData = str.split(", ");
            Employee employee = new Employee(employeeData[1], employeeData[0], employeeData[2], employeeData[3]);
            if (employee.isBirthday(xDate)) {
                String recipient = employee.getEmail();
                String body = "Happy Birthday, dear %NAME%!".replace("%NAME%", employee.getFirstName());
                String subject = "Happy Birthday!";
                mailService.sendMessage("sender@here.com", subject, body, recipient);
            }
        }

    }
}
