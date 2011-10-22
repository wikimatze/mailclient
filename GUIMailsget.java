
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * Version 3.0
 * Matthias Guenther
 * mails abrufen - dies war ganz schoen schwierig
 */
public class GUIMailsget extends GUIMain implements ActionListener {

    private JList jlist;
    private TextArea definitionArea;
    private JPanel mailsReceiveComplete, subPanel, fromPanel, toPanel,
            messageData, abortPanel;
    private JButton abort, rewrite, answer;
    private JFrame messageFrame;
    private JLabel subLab, subLabOutput, fromLab, fromLabOutput, toLab,
            toLabOutput;
    // userInfos[2]
    private String userId = userInfos[2];
    // userInfos[3]
    private String userPass = userInfos[3];
    // userInfos[4]
    //private String connectURL = "pop3.web.de";
    private String connectURL = userInfos[4];
    private File userName;
    // store the messages
    private Message[] message;
    private boolean checkEqual = false;
    private int num;
    private Store store;
    Folder folder;

    public void guiMailsget() {
        try {
            messageFrame = new JFrame("Mail Informations");

            currentUserDir = dir + "Inbox " + userInfos[1];
            userName = new File(currentUserDir);

            messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Session session = Session.getInstance(new Properties());
            // get a Store object
            store = session.getStore("pop3");
            store.connect(connectURL, userId, userPass);

            // get "INBOX"
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // this variable counts the message number
            int count = folder.getMessageCount();

            System.out.println(count + " new messages");
            message = folder.getMessages();

            // set size of the frame for the messages
            messageFrame.setSize(600, 400);
            mailsReceiveComplete = new JPanel(new BorderLayout());
            mailsReceiveComplete.setSize(300, 100);

            // to mark and save new emails
            Object elements[][] = new Object[count][4];
            for (int j = 0; j < elements.length; j++) {
                num = j;
                try {
                    searchLocalDir(userName);
                    System.out.println("aktuell: " + checkEqual);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (int z = 0; z <= 3; z++) {
                    if (z == 0 && checkEqual == true) {
                        elements[j][z] = new Font("TimesRoman", Font.BOLD, 11);
                    } else if (z == 0) {
                        elements[j][z] = new Font("TimesRoman", Font.BOLD, 13);
                    } else if (z == 1 && checkEqual == true) {
                        elements[j][z] = Color.gray;
                    } else if (z == 1) {
                        elements[j][z] = Color.black;
                    } else if (z == 2 && checkEqual == true) {
                        elements[j][z] = new DiamondIcon(Color.gray);
                    } else if (z == 2) {
                        elements[j][z] = new DiamondIcon(Color.red);
                    } else {

                        elements[j][z] = message[j].getSubject() + "   " + message[j].getFrom()[0] + "   " + message[j].getSentDate();

                    }

                }

                checkEqual = false;

            }

            JFrame frame = new JFrame("Mail List");
            Container contentPane = frame.getContentPane();

            // create a new listwith the whole emails inside, with the specific
            // infos
            jlist = new JList(elements);

            ListCellRenderer renderer = new ComplexCellRenderer();
            jlist.setCellRenderer(renderer);
            jlist.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent event) {
                    try {
                        JList source = (JList) event.getSource();
                        int number = source.getSelectedIndex();
                        definitionArea.setText(message[number].getContent().toString());
                        subLabOutput.setText(message[number].getSubject().toString());
                        fromLabOutput.setText(message[number].getFrom()[0].toString());
                        toLabOutput.setText(message[number].getAllRecipients()[0].toString());

                        // to write that this message was written
                        FileWriter fw = null;

                        try {
                            String tmp = message[number].getSentDate().toString();
                            String tmpparse = tmp.replaceAll(":", "_");
                            fw = new FileWriter(currentUserDir + "/" + tmpparse + ".txt");
                            String tmp1 = "True\n[FROM]: " + (message[number].getFrom()[0].toString() + "\n");
                            String tmp2 = "[SUBJECT]: " + (message[number].getSubject().toString() + "\n");
                            String tmp3 = "[CONTENT]: " + (message[number].getContent().toString());
                            String messageContentTmp = tmp1 + tmp2 + tmp3;
                            fw.write(messageContentTmp);
                            tmp = "";
                            fw.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                }
            });
            mailsReceiveComplete.add("North", jlist);
            JScrollPane scrollPane = new JScrollPane(jlist);
            scrollPane.setSize(100, 300);
            contentPane.add(scrollPane, BorderLayout.CENTER);

            frame.setSize(450, 400);
            frame.setLocation(600, 0);
            frame.setVisible(true);

            userName.mkdir();

            subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            subLab = new JLabel("[SUBJECT]:");
            subLabOutput = new JLabel();
            subPanel.add(subLab);
            subPanel.add(subLabOutput);

            fromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            fromLab = new JLabel("[FROM]:");
            fromLabOutput = new JLabel();
            fromPanel.add(fromLab);
            fromPanel.add(fromLabOutput);

            toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            toLab = new JLabel("[To]:");
            toLabOutput = new JLabel();
            toPanel.add(toLab);
            toPanel.add(toLabOutput);

            abortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            abort = new JButton("Quit");
            abort.addActionListener(this);
            rewrite = new JButton("Reanswer");
            rewrite.addActionListener(this);
            answer = new JButton("Answer");
            answer.addActionListener(this);

            abortPanel.add(answer);
            abortPanel.add(rewrite);
            abortPanel.add(abort);

            messageData = new JPanel();
            messageData.setLayout(new GridLayout(5, 1));
            messageData.add(subPanel);
            messageData.add(fromPanel);
            messageData.add(toPanel);

            // mergin all Message Data from abovethe components together
            definitionArea = new TextArea();
            definitionArea.setEditable(false);

            mailsReceiveComplete.add("Center", messageData);
            mailsReceiveComplete.add("South", definitionArea);
            mailsReceiveComplete.add("East", abortPanel);
            messageFrame.add(mailsReceiveComplete);
            messageFrame.setVisible(true);
        } catch (MessagingException msg) {
            System.out.println(msg);
        }

    }

    // search for mails on the account
    public void searchLocalDir(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                try {
                    // to parse the Strings
                    String checktmp = message[num].getSentDate() + ".txt";
                    String check = checktmp.replaceAll(":", "_");
                    if (check.equals(files[i].getName())) {

                        // check if the file starts with True
                        String strLine = "";
                        try {
                            // open file to read it
                            BufferedReader oRead = new BufferedReader(
                                    new FileReader(currentUserDir + "/" + files[i].getName()));
                            while (null != (strLine = oRead.readLine())) {
                                if (strLine.startsWith("True")) {
                                    checkEqual = true;
                                }
                            }
                            oRead.close();
                        } catch (FileNotFoundException exFileNotFound) {
                            System.out.println(exFileNotFound);
                        } catch (IOException exIO) {
                            System.out.println(exIO);
                        } catch (Exception eAllg) {
                            System.out.println(eAllg);
                        }

                    }
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // for the buttons to ander
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == abort) {
            messageFrame.setVisible(false);
            try {
                folder.close(false);
                store.close();
            } catch (MessagingException e1) {
                e1.printStackTrace();
            }
        }
        if (obj == rewrite) {
            int index = jlist.getSelectedIndex();

            try {
                // get the contents of the selected message
                fromLabOutput.setText(message[index].getFrom()[0].toString());
                subLabOutput.setText(message[index].getSubject().toString());
                definitionArea.setText(message[index].getContent().toString());

                // get the strings of these tmp infos of the messages
                String tmp1 = fromLabOutput.getText();
                String tmp2 = subLabOutput.getText();
                String tmp3 = definitionArea.getText();

                GUIMailsend send = new GUIMailsend();

                // setting the parameter
                send.fromString = tmp1;
                send.subjectString = tmp2;
                send.contentString = tmp3;
                send.toString = "";
                send.guiMailsend();
            } catch (IOException e1) {
            } catch (MessagingException e1) {
            }
        }

        // the same as above
        if (obj == answer) {
            int index = jlist.getSelectedIndex();
            try {
                fromLabOutput.setText(message[index].getFrom()[0].toString());
                subLabOutput.setText(message[index].getSubject().toString());
                definitionArea.setText(message[index].getContent().toString());

                // get the strings of these tmp infos of the messages
                String tmp1 = fromLabOutput.getText();
                String tmp2 = subLabOutput.getText();
                String tmp3 = definitionArea.getText();
                String tmp4 = message[index].getSentDate().toString();

                // create a new object
                GUIMailsend send = new GUIMailsend();

                String[] ak = tmp3.split("\n");
                String content = "";
                String tmpp;
                for (int i = 0; i < ak.length; i++) {
                    tmpp = "> " + ak[i];
                    content = content + tmpp + "\n";
                }
                String infos = tmp1 + " wrote " + tmp4 + "\n" + content;
                send.fromString = tmp1;
                send.subjectString = "RE: " + tmp2;
                send.contentString = infos;
                send.guiMailsend();
            } catch (Exception msg) {
                System.out.println(msg);
            }

        }

    }

    public static void main(String[] args) {

        GUIMailsget test = new GUIMailsget();
        test.guiMailsget();
    }
}
