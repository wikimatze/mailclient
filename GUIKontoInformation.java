
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import java.io.*;

/*
 * Version 2.0
 * Matthias Guenther
 * speichert Infos ueber die Benutzer ab
 */
public class GUIKontoInformation extends GUIMain implements ActionListener {

    private JPanel complete, emailadress, username, uservalidationname,
            userpass, postentrance, postexit, check;
    public JTextField portin, postex;
    public JTextField emailadressinput = new JTextField(25);
    public JTextField usernameinput = new JTextField(25);
    public JTextField uservalidationnameinput = new JTextField(25);
    public JTextField userpassinput = new JTextField(25);
    public JTextField postentranceinput = new JTextField(25);
    public JTextField postexitinput = new JTextField(25);
    private JCheckBox save;
    private JButton ok, abort;
    private JFrame kontoInformationFame;
    public String informations;
    private boolean saveInformation = false;

    // constructor to get login-information
    public void GUIKontoInformationCreate() {

        // create the main frame
        kontoInformationFame = new JFrame("Email-Client");

        // to exit the frame when the x is pressed
        kontoInformationFame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        kontoInformationFame.setSize(500, 450);
        emailadress = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailadress.add(new JLabel("email Adresse"));
        emailadress.add(emailadressinput);

        username = new JPanel(new FlowLayout(FlowLayout.LEFT));
        username.add(new JLabel("Name:"));
        username.add(usernameinput);

        uservalidationname = new JPanel(new FlowLayout(FlowLayout.LEFT));
        uservalidationname.add(new JLabel("Benutzererkennung:"));
        uservalidationname.add(uservalidationnameinput);

        userpass = new JPanel(new FlowLayout(FlowLayout.LEFT));
        save = new JCheckBox("Passwort speichern");
        save.addItemListener(new CheckBoxListener());

        userpass.add(new JLabel("Passwort:"));
        userpass.add(userpassinput);
        userpass.add(save);

        postentrance = new JPanel(new FlowLayout(FlowLayout.LEFT));
        portin = new JTextField(6);
        portin.setText("110");
        postentrance.add(new JLabel("Posteingang:"));
        postentrance.add(postentranceinput);
        postentrance.add(new JLabel("Port:"));
        postentrance.add(portin);

        postexit = new JPanel(new FlowLayout(FlowLayout.LEFT));
        postexit.add(new JLabel("Postausgang:"));
        postex = new JTextField(6);
        postex.setText("10");
        postexit.add(postexitinput);
        postexit.add(new JLabel("Port:"));
        postexit.add(postex);

        // create the buttons for ok and abort
        check = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ok = new JButton("Ok");
        ok.addActionListener(this);
        abort = new JButton("Abbrechen");
        abort.addActionListener(this);
        check.add(ok);
        check.add(abort);

        // create a great panel complete which contain all other panels
        complete = new JPanel(new GridLayout(8, 1));
        complete.add(new JLabel("Benutzerdaten eingeben: "));
        complete.add(emailadress);
        complete.add(username);
        complete.add(uservalidationname);
        complete.add(userpass);
        complete.add(postentrance);
        complete.add(postexit);
        complete.add(check);

        // add the great panel complete to the frame
        kontoInformationFame.add(complete);
        kontoInformationFame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == ok) {
            if (saveInformation == true) {
                saveUserInfo();
                writeUserInfo();
                kontoInformationFame.setVisible(false);
            } else {
                writeUserInfo();
                kontoInformationFame.setVisible(false);
            }

        }

        if (obj == abort) {
            kontoInformationFame.setVisible(false);
        }

    }

    // action listener if the Account Infos should be saved permanetly
    class CheckBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent evt) {
            saveInformation = save.isSelected();
        }
    }

    // write the user infos in the global array
    public void writeUserInfo() {
        String emailadressin = emailadressinput.getText();
        String usernamein = usernameinput.getText();
        String uservalidationnamein = uservalidationnameinput.getText();
        String userpassin = userpassinput.getText();
        String postentrancein = postentranceinput.getText();
        String postinprt = portin.getText();
        String postexitin = postexitinput.getText();
        String postexitprt = postex.getText();


        informations = emailadressin + "#######" + usernamein + "#######" + uservalidationnamein + "#######" + userpassin + "#######" + postentrancein + "#######" + postinprt + "#######" + postexitin + "#######" + postexitprt;


        userInfos = informations.split("#######");
    }

    public void saveUserInfo() {
        File userName = new File(userInfos[1]);
        userName.mkdir();
        String emailadressin = emailadressinput.getText();
        String usernamein = usernameinput.getText();
        String uservalidationnamein = uservalidationnameinput.getText();
        String userpassin = userpassinput.getText();
        String postentrancein = postentranceinput.getText();
        String postinprt = portin.getText();
        String postexitin = postexitinput.getText();
        String postexitprt = postex.getText();

        String tmp = "";
        tmp = emailadressin + "#######" + usernamein + "#######" + uservalidationnamein + "#######" + userpassin + "#######" + postentrancein + "#######" + postinprt + "#######" + postexitin + "#######" + postexitprt;

        try {
            FileWriter fw = new FileWriter(usernameinput.getText() + " kontoinfos.kondat");
            fw.write(tmp);
            fw.close();
        } catch (IOException msg) {
            System.err.println("Cannot create file");
        }

    }

    public static void main(String[] args) {
        GUIKontoInformation test = new GUIKontoInformation();
        test.GUIKontoInformationCreate();
    }
}
