
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.*;

/*
 * Version 2.0
 * Matthias Guenther
 * drag'n drop Baum zur Mail-Verwaltung
 */
public class GUITreeInsert extends JFrame implements ActionListener {

    protected DefaultMutableTreeNode root;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    public JLabel file;
    public JTextArea filename;
    public JButton namefile;
    public JFrame filenewFrame;
    private String tmp = "d";
    public TreePath tp, sourcePath;
    public DefaultMutableTreeNode node;
    public DefaultMutableTreeNode selectedNode;
    public DefaultMutableTreeNode goal;
    public DefaultMutableTreeNode Messages;

    // create Jtree with one selection
    public GUITreeInsert() {
        root = new DefaultMutableTreeNode("Root");

        // search for current Ordners
        directorieSearch();

        // create the new tree model where every node can have children
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);

        tree.setDragEnabled(true);
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        // activate the drop function
        tree.setDropMode(DropMode.USE_SELECTION);
        tree.setVisible(true);
        tree.setDropTarget(new DropTarget(tree, TransferHandler.MOVE,
                new DropTargetAdapter() {

                    public void drop(DropTargetDropEvent dtde) {

                        TreePath selectionPath = tree.getSelectionPath();

                        sourcePath = selectionPath.getParentPath();

                        selectedNode = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();

                        Point dropLocation = dtde.getLocation();

                        TreePath targetPath = tree.getClosestPathForLocation(
                                dropLocation.x, dropLocation.y);

                        System.out.println("srcPath: " + sourcePath);

                        System.out.println("Parent" + sourcePath.getLastPathComponent());
                        System.out.println("targetPath: " + targetPath);

                        // writeContent();

                        // select node
                        System.out.println("selectedNode: " + selectedNode);

                        if (isDropAllowed(sourcePath, targetPath, selectedNode)) {

                            goal = (DefaultMutableTreeNode) targetPath.getLastPathComponent();
                            DefaultMutableTreeNode targetParentNode = (DefaultMutableTreeNode) targetPath.getLastPathComponent();

                            DefaultMutableTreeNode sourceParentNode = (DefaultMutableTreeNode) sourcePath.getLastPathComponent();

                            sourceParentNode.remove(selectedNode);

                            targetParentNode.add(selectedNode);

                            System.out.println("Goal: " + goal);
                            changeEmailContent();
                            dtde.dropComplete(true);

                            changeEmailContent();
                            tree.updateUI();

                        } else {

                            System.out.println("drop: reject");

                            dtde.rejectDrop();

                            dtde.dropComplete(false);

                        }

                    }

                    private boolean isDropAllowed(TreePath sourcePath,
                            TreePath targetPath,
                            DefaultMutableTreeNode selectedNode) {

                        if (((DefaultMutableTreeNode) sourcePath.getLastPathComponent()).isLeaf()) {
                        } else if (targetPath.equals(sourcePath)) {

                            return false;

                        }

                        return selectedNode.isLeaf();

                    }
                }// closing new Drop
                ));

        // make the root visible
        tree.setRootVisible(true);

        // insert the tree in a component
        Container cp = getContentPane();
        cp.add(new JScrollPane(tree), BorderLayout.CENTER);
        // ButtonPanel
        JPanel panel = new JPanel(new FlowLayout());

        // create the fucking buttons
        String[] buttons = new String[]{"Add File"};
        for (int i = 0; i < buttons.length; ++i) {
            JButton button = new JButton(buttons[i]);
            button.addActionListener(this);
            panel.add(button);
        }
        cp.add(panel, BorderLayout.SOUTH);
    }

    // for the events
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        tp = tree.getLeadSelectionPath();
        if (tp != null) {
            node = (DefaultMutableTreeNode) tp.getLastPathComponent();

            if (cmd.equals("Add File")) {

                // create a frame for the input of the file
                filenewFrame = new JFrame("File Name");
                filenewFrame.setSize(200, 200);
                filenewFrame.setLayout(new GridLayout());

                file = new JLabel("File Name: ");
                filename = new JTextArea();
                namefile = new JButton("Ok");
                namefile.addActionListener(new ButtonNameFileListener());

                filenewFrame.setVisible(true);
                filenewFrame.add(file);
                filenewFrame.add(filename);
                filenewFrame.add(namefile);
            }

        }
    }

    // method to list all directories with files
    public void directorieSearch() {
        File driveC = new File(
                "/home/helex/NetBeansProjects/Test2/src/");
        File files[] = driveC.listFiles();
        File filestmp[];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() == true) {
                Messages = new DefaultMutableTreeNode(files[i].getName());
                // list the messages in the directories
                filestmp = files[i].listFiles();
                System.out.println(filestmp.length);
                // if there are files in then mail then add leafs
                if (filestmp.length != 0) {
                    for (int j = 0; j < filestmp.length; j++) {
                        System.out.println("1");
                        Messages.add(new DefaultMutableTreeNode(filestmp[j].getName()));
                    }

                    root.add(Messages);
                } else {
                    // to add empty files
                    root.add(Messages);
                }

            }

        }

    }

    // this method will be invoked, if the user changes the positions of the
    // messages
    public void changeEmailContent() {
        File source = new File(
                "/home/helex/NetBeansProjects/Test2/src/" + sourcePath.getLastPathComponent() + "/" + selectedNode);
        File ziel = new File(
                "/home/helex/NetBeansProjects/Test2/src/" + goal + "/" + selectedNode);

        source.renameTo(ziel);

    }

    // listener if the user add a directory
    class ButtonNameFileListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            filenewFrame.setVisible(false);
            tmp = filename.getText();
            DefaultMutableTreeNode child;
            child = new DefaultMutableTreeNode(tmp);
            File userName = new File(child.toString());
            userName.mkdir();
            treeModel.insertNodeInto(child, node, node.getChildCount());
            TreeNode[] path = treeModel.getPathToRoot(node);
            tree.expandPath(new TreePath(path));
        }
    }

    public static void main(String[] args) {
        try {
            GUITreeInsert treeFrame = new GUITreeInsert();
            treeFrame.setVisible(true);
            treeFrame.setSize(300, 300);
            treeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            treeFrame.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
