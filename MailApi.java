
import java.util.*;
import java.io.*;
import javax.mail.*;

/**
 * @since 04.05.2007
 * @author Matthias Guenther
 * @version 1.0
 * get emails with Mailapi
 */
public class MailApi {

    // user informations
    private String userId;
    private String userPass;
    private String connectURL;
    private boolean checkOn = true;
    // Readers are visible for all methods
    private BufferedReader user;

    public void getConnection() {
        try {
            System.out.println("Insert the adress of the POP3-Server: ");
            user = new BufferedReader(new InputStreamReader(System.in));
            // Beispiel: pop3.web.de
            connectURL = user.readLine();
            getUser();

        } catch (Exception msg) {
            System.out.println("\nHost is not available - try again later");
        }

    }

    public void getUser() {
        try {
            System.out.println("\nInput your username");
            System.out.println("---------------------");
            userId = user.readLine();

            getUserPass();
        } catch (Exception msg) {
        }

    }

    public void getUserPass() {
        try {
            System.out.println("\nInput your password");
            System.out.println("---------------------");
            userPass = user.readLine();
            terminalApi();
        } catch (Exception msg) {
        }

    }

    public void terminalApi() {

        try {

            Session session = Session.getInstance(new Properties());
            // get a Store object
            Store store = session.getStore("pop3");
            store.connect(connectURL, userId, userPass);

            // get "INBOX"
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            int count = folder.getMessageCount();

            System.out.println(count + " new messages");
            Message[] message = folder.getMessages();
            Message msg;
            for (int i = 1; i <= message.length; i++) {
                msg = message[i - 1];
                System.out.println("Message number: " + i);
                System.out.println("Date: " + msg.getSentDate());
            }

            do {
                System.out.println("Which Message do you want to read? (Quit with Q)");
                String terminalInput = user.readLine();
                if (terminalInput.startsWith("Q")) {
                    System.out.println("Sayonara");
                    checkOn = false;
                }
                int number = Integer.parseInt(terminalInput);
                if (number <= count && number > 0) {
                    Message m = folder.getMessage(number);
                    System.out.println("\nMessage number : " + m.getMessageNumber());
                    System.out.println("Subject : " + m.getSubject());
                    System.out.println("From : " + m.getFrom()[0]);
                    System.out.println("Content: " + m.getContent());
                    System.out.println(m.getSize());

                } else {
                    System.out.println("Your Input is invalid");
                }
            } while (checkOn == true);

            // keep messages on server
            folder.close(false);
            store.close();

        } catch (IOException msg) {
            System.out.println(msg);
        } catch (MessagingException msg) {
            System.out.println(msg);
        }

    }

    public static void main(String[] args) {
        MailApi userOne = new MailApi();
        userOne.getConnection();

    }
}
