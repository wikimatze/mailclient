
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.mail.*;
import javax.mail.internet.*;

/*
 * Version 2.0
 * Matthias Guenther
 * um Mails zu versenden inklusive Anhaenge
 */
public class GUIMailsend extends GUIMain {

    private JPanel from, to, cc, subject, text, allinone, check;
    public JTextField frominput, toinput, ccinput, subjectinput;
    private TextArea messagebodyinput;
    private JButton send, delete, quit, attach;
    public JFrame mailSendFrame;
    // um mehrere Empf�nger reinzuschreiben
    private InternetAddress too[];
    // diese Informationen muss aus dem userInfos[] array gewonnen werden
    // userInfos[2]
    //private String username = "razinger";
    private String username = userInfos[2];
    // userInfos[3]
    //private String userpass = "Ratzinger";
    private String userpass = userInfos[3];
    // userInfos[6]
    //private String smtpServer = "smtp.web.de";
    private String smtpServer = userInfos[6];
    private String attachFile;
    public String fromString = "razinger@web.de";
    public String subjectString = "Test";
    public String toString = "lordmatze@googlemail.com";
    public String contentString = "<input text>";
    private boolean attachbool = false;

    public void guiMailsend() {
        // create the frame
        mailSendFrame = new JFrame("Message");
        mailSendFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mailSendFrame.setSize(500, 600);

        from = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frominput = new JTextField(25);
        frominput.setText(fromString);
        from.add(new JLabel("From:"));
        from.add(frominput);

        to = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toinput = new JTextField(25);
        toinput.setText(toString);
        to.add(new JLabel("To:"));
        to.add(toinput);

        cc = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ccinput = new JTextField(25);
        cc.add(new JLabel("CC:"));
        cc.add(ccinput);

        subject = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectinput = new JTextField(25);
        subjectinput.setText(subjectString);
        subject.add(new JLabel("Subject:"));
        subject.add(subjectinput);

        text = new JPanel(new FlowLayout(FlowLayout.LEFT));
        text.add(new JLabel("Message:"));

        messagebodyinput = new TextArea();
        messagebodyinput.setText(contentString);

        send = new JButton("Send");
        send.addActionListener(new ButtonSendListener());

        quit = new JButton("Quit");
        quit.addActionListener(new ButtonQuitListener());

        delete = new JButton("Delete");
        delete.addActionListener(new ButtonDeleteListener());

        attach = new JButton("Attach");
        attach.addActionListener(new ButtonAttachListener());
        // the buttons
        check = new JPanel(new FlowLayout(FlowLayout.CENTER));
        check.add(send);
        check.add(quit);
        check.add(delete);
        check.add(attach);

        allinone = new JPanel(new GridLayout(7, 1));
        allinone.add(from);
        allinone.add(to);
        allinone.add(cc);
        allinone.add(subject);
        allinone.add(text);
        allinone.add(messagebodyinput);
        allinone.add(check);

        mailSendFrame.add(allinone);
        mailSendFrame.setVisible(true);
    }

    // add the fucking listeners
    class ButtonSendListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            sendMail();
            mailSendFrame.setVisible(false);
        }
    }

    class ButtonQuitListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            mailSendFrame.setVisible(false);
        }
    }

    class ButtonDeleteListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            frominput.setText("");
            toinput.setText("");
            ccinput.setText("");
            subjectinput.setText("");
            messagebodyinput.setText("");
        }
    }

    class ButtonAttachListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            JFileChooser dialog = new JFileChooser();
            int tmp = dialog.showOpenDialog(dialog);
            getFilePath(dialog.getSelectedFile());

        }
    }

    public void sendMail() {

        MailAuthenticator auth = new MailAuthenticator(username, userpass);

        Properties properties = new Properties();

        // add the serveradress to properties
        properties.put("mail.smtp.host", smtpServer);

        properties.setProperty("mail.smtp.port", "587");

        // smtp needs authentication so it muss be true
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(properties, auth);

        try {
            String bla = toinput.getText();

            // this simple loop enable multiple recipients
            String[] tmp = new String[bla.split(",").length];
            tmp = bla.split(",");
            Message msg = new MimeMessage(session);
            too = new InternetAddress[tmp.length];
            for (int i = 0; i < tmp.length; i++) {
                too[i] = new InternetAddress(tmp[i]);
            }

            // set sender adress and recipients
            msg.setFrom(new InternetAddress(frominput.getText()));
            msg.setRecipients(Message.RecipientType.TO, too);

            if (attachbool == true) {
                Multipart multipart = new MimeMultipart();
                BodyPart messageBodyPart = new MimeBodyPart();
                File fileattach = new File(attachFile);
                System.out.println(attachFile);
                // create an object for the attach
                DataSource attach = new FileDataSource(fileattach);
                messageBodyPart.setDataHandler(new DataHandler(attach));

                // to give the attachement a name
                messageBodyPart.setFileName(attachFile);
                multipart.addBodyPart(messageBodyPart);
                msg.setSubject(subjectinput.getText());
                msg.setText(messagebodyinput.getText());
                msg.setContent(multipart);
                Transport.send(msg);

            } else {
                msg.setSubject(subjectinput.getText());
                msg.setText(messagebodyinput.getText());
                Transport.send(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // need to authentificate
    class MailAuthenticator extends Authenticator {

        private final String user;
        private final String password;

        public MailAuthenticator(String u, String p) {
            this.user = u;
            this.password = p;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(this.user, this.password);
        }
    }

    // method to get the path of the attached file and to open the dialog
    public void getFilePath(File datei) {
        try {
            JInternalFrame FrameChoose = new JInternalFrame(datei.toString(),
                    true, true, true, true);
            JTextArea text = new JTextArea(10, 40);
            text.setLineWrap(true);
            attachbool = true;
            text.read(new FileReader(datei), null);
            attachFile = datei.getAbsolutePath();
            System.out.println(attachbool);

        } catch (IOException ex) {
            System.out.println("Cant't find file");
        }
    }

    public static void main(String[] args) {
        GUIMailsend test = new GUIMailsend();
        test.guiMailsend();
    }
}
