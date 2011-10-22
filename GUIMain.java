
import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Polygon;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/*
 * Version 2.0
 * Matthias Guenther
 * Hauptprogramm - simply the best
 */
public class GUIMain {

    public TextArea myTextArea;
    public TextField absender, empfaenger, empfaengerCC, subject;
    public Panel buttonPanel;
    public Menu file, accounts, kontenMenu, info;
    public MenuItem saveItem, exit, accountnew, accountedit, readmail,
            writemail, accountavailable, managelocalfiles;
    public CheckboxMenuItem autoSave;
    public JLabel edit;
    public MenuBar menucomplete;
    public List postList;
    public List editalbeAccounts;
    // write in the konto dates of user and will be overwritten everytime the
    // account changes
    public static String[] userInfos = new String[7];
    // to save the current data of the user
    public static String userTmpInfos = "";
    private JFrame mainframe, frameEditable;
    public File[] files;
    public static File f = new File(
            "/home/helex/NetBeansProjects/Test2/src/");
    public static String dir = "/home/helex/NetBeansProjects/Test2/src/";
    public String currentUserDir = "";

    // display main-frame of the client
    public void createMainFrame() {
        mainframe = new JFrame("Email-Client");
        mainframe.setVisible(false);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setSize(800, 600);

        // user can change the size of the window
        mainframe.setResizable(true);

        // create the file menu
        file = new Menu("File");
        readmail = new MenuItem("Read Emails");
        file.add(readmail);
        readmail.addActionListener(new ReadMailListener());
        writemail = new MenuItem("Write Email");
        file.add(writemail);
        writemail.addActionListener(new WriteMailListener());
        file.addSeparator();
        managelocalfiles = new MenuItem("Manage Emails");
        managelocalfiles.addActionListener(new MangeMailsListener());
        file.add(managelocalfiles);
        file.addSeparator();

        exit = new MenuItem("Quit");
        file.add(exit);
        exit.addActionListener(new ExitItemListener());

        info = new Menu("Info");
        info.add("Info");
        info.addActionListener(new InfoListener());

        menucomplete = new MenuBar();
        menucomplete.add(file);
        menucomplete.add(accounts);
        menucomplete.add(info);

        mainframe.setMenuBar(menucomplete);

        mainframe.setVisible(true);
    }

    // the fucking Listeners
    class InfoListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Frame test = new JFrame("Mail-Client Infos");
            JPanel aboutPanel = new JPanel();
            aboutPanel.setLayout(new GridLayout(4, 1));
            test.setSize(400, 200);
            JLabel about = new JLabel(
                    "Mail-Client:  Designt by Matthias Guenther");
            JLabel about1 = new JLabel("Version:  2.0");
            JLabel about2 = new JLabel("Contact: lordmatze@googlemail.com");
            JLabel about3 = new JLabel(
                    "Performed in the 'Programmierpraktikum 2, SS 2007'");
            aboutPanel.add(about);
            aboutPanel.add(about1);
            aboutPanel.add(about2);
            aboutPanel.add(about3);
            test.add(aboutPanel);
            test.setVisible(true);
        }
    }

    class ReadMailListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            GUIMailsget test = new GUIMailsget();
            test.guiMailsget();
        }
    }

    class WriteMailListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            GUIMailsend send = new GUIMailsend();
            send.guiMailsend();
        }
    }

    class ExitItemListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
        }
    }

    class NewAccountListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            GUIKontoInformation test = new GUIKontoInformation();
            test.GUIKontoInformationCreate();
        }
    }

    class MangeMailsListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            try {
                GUITreeInsert treeFrame = new GUITreeInsert();
                treeFrame.setSize(400, 400);
                treeFrame.setVisible(true);
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    // to list alle available Accounts
    public void searchAccounts(File dir) throws IOException {
        files = dir.listFiles();
        accounts = new Menu("Account(s)");
        accountnew = new MenuItem("New Account");
        accountnew.addActionListener(new NewAccountListener());
        accountedit = new MenuItem("Edit Account");
        accountedit.addActionListener(new EditAccountListener());
        accounts.add(accountnew);
        accounts.add(accountedit);
        accounts.addSeparator();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                String tmp = files[i].getAbsolutePath();

                if (tmp.endsWith("kondat")) {
                    String tmpinfos = "";
                    FileReader f = new FileReader(tmp);
                    for (int c; (c = f.read()) != -1;) {
                        tmpinfos = tmpinfos + (char) c;
                    }

                    userInfos = tmpinfos.split("#######");
                    accountavailable = new MenuItem(userInfos[1]);
                    accountavailable.addActionListener(new accountlistener());
                    accounts.add(accountavailable);

                }

            }

        }

    }

    class accountlistener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            // dirty cast to get name of the Label
            String name = ((MenuItem) e.getSource()).getLabel();

            FileReader file = null;
            String tmp = "";
            try {
                file = new FileReader(dir + name + " kontoinfos.kondat");

                for (int c; (c = file.read()) != -1;) {
                    tmp = tmp + (char) c;
                }

                userInfos = tmp.split("#######");

            } catch (IOException es) {
                System.out.println("Error reading file!");
            } finally {
                try {
                    file.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        }
    }

    class EditAccountListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            try {

                editableAccounts(f);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // to list all available editable accounts
    public void editableAccounts(File dir) throws IOException {
        frameEditable = new JFrame("Editable Accounts");
        editalbeAccounts = new List();
        frameEditable.setSize(200, 200);
        frameEditable.setLayout(new BorderLayout());
        edit = new JLabel("Available Accounts: ");
        frameEditable.add(edit, "North");
        JPanel tmpe = new JPanel();
        files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                String tmp = files[i].getAbsolutePath();

                if (tmp.endsWith("kondat")) {
                    String tmpinfos = "";
                    FileReader f = new FileReader(tmp);
                    for (int c; (c = f.read()) != -1;) {
                        tmpinfos = tmpinfos + (char) c;
                    }
                    userInfos = tmpinfos.split("#######");
                    editalbeAccounts.add(userInfos[1]);

                    tmpe.add(editalbeAccounts, "Center");

                }

            }

        }
        editalbeAccounts.addItemListener(new EditListener());
        frameEditable.add(tmpe);
        frameEditable.setVisible(true);

    }

    class EditListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            String tmp2[] = e.getSource().toString().split("=");
            String tmp1 = tmp2[1].replace("]", "");
            FileReader file = null;
            String tmp = "";
            try {
                file = new FileReader(
                        "/home/helex/NetBeansProjects/Test2/src/" + tmp1 + " kontoinfos.kondat");

                for (int c; (c = file.read()) != -1;) {
                    tmp = tmp + (char) c;
                }

                userInfos = tmp.split("#######");

            } catch (IOException es) {
                System.out.println("Error reading file!");
            } finally {
                try {
                    file.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }

            // close the editableAccount frame
            frameEditable.setVisible(false);

            System.out.println(userInfos[0]);
            GUIKontoInformation test = new GUIKontoInformation();
            test.emailadressinput.setText(userInfos[0]);
            test.usernameinput.setText(userInfos[1]);
            test.uservalidationnameinput.setText(userInfos[2]);
            test.userpassinput.setText(userInfos[3]);
            test.postentranceinput.setText(userInfos[4]);
            test.postexitinput.setText(userInfos[6]);
            test.GUIKontoInformationCreate();
        }
    }

    // needed for the class GUIMailsget
    class ComplexCellRenderer implements ListCellRenderer {

        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Font theFont = null;
            Color theForeground = null;
            Icon theIcon = null;
            String theText = null;

            JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            if (value instanceof Object[]) {
                Object values[] = (Object[]) value;
                theFont = (Font) values[0];
                theForeground = (Color) values[1];
                theIcon = (Icon) values[2];
                theText = (String) values[3];
            } else {
                theFont = list.getFont();
                theForeground = list.getForeground();
                theText = "";
            }
            if (!isSelected) {
                renderer.setForeground(theForeground);
            }
            if (theIcon != null) {
                renderer.setIcon(theIcon);
            }
            renderer.setText(theText);
            renderer.setFont(theFont);
            return renderer;
        }
    }

    // needet for GUIMailsget for the icon color
    class DiamondIcon implements Icon {

        private Color color;
        private boolean selected;
        private int width;
        private int height;
        private Polygon poly;
        private static final int DEFAULT_WIDTH = 10;
        private static final int DEFAULT_HEIGHT = 10;

        public DiamondIcon(Color color) {
            this(color, true, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        public DiamondIcon(Color color, boolean selected) {
            this(color, selected, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        public DiamondIcon(Color color, boolean selected, int width, int height) {
            this.color = color;
            this.selected = selected;
            this.width = width;
            this.height = height;
            initPolygon();
        }

        private void initPolygon() {
            poly = new Polygon();
            int halfWidth = width / 2;
            int halfHeight = height / 2;
            poly.addPoint(0, halfHeight);
            poly.addPoint(halfWidth, 0);
            poly.addPoint(width, halfHeight);
            poly.addPoint(halfWidth, height);
        }

        public int getIconHeight() {
            return height;
        }

        public int getIconWidth() {
            return width;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.translate(x, y);
            if (selected) {
                g.fillPolygon(poly);
            } else {
                g.drawPolygon(poly);
            }
            g.translate(-x, -y);
        }
    }

    public static void main(String[] args) {
        try {

            GUIMain test = new GUIMain();

            // create the search path
            test.searchAccounts(f);
            test.createMainFrame();

        } catch (Exception msg) {
            System.out.println(msg);
        }

    }
}
