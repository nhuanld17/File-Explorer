package TEST2;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class FileExplorer extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField filePath_textField;
	private JTextField find_textField;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileExplorer frame = new FileExplorer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FileExplorer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 877, 494);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setForeground(new Color(17, 24, 39));
		panel.setBackground(new Color(244, 245, 249));
		panel.setBounds(0, 0, 861, 455);
		contentPane.add(panel);
		panel.setLayout(null);
		
		filePath_textField = new JTextField();
		filePath_textField.setBounds(0, 0, 719, 37);
		panel.add(filePath_textField);
		filePath_textField.setColumns(10);
		
		find_textField = new JTextField();
		find_textField.setBounds(729, 0, 132, 37);
		panel.add(find_textField);
		find_textField.setColumns(10);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("DISK");
		JTree tree = new JTree(root);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);;
		tree.setBounds(0, 41, 215, 414);
		
		String[] columnNames = {"Name","Date Modify" ,"Type", "Size"};
		tableModel = new DefaultTableModel(columnNames,0);
		table = new JTable(tableModel);
		table.setBounds(222, 41, 639, 414);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(222, 41, 639, 414);
		panel.add(scrollPane);
		
		scrollPane_1 = new JScrollPane(tree);
		scrollPane_1.setBounds(0, 41, 215, 414);
		panel.add(scrollPane_1);
		
		File[] drives = File.listRoots();
		for (File drive : drives) {
			DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(drive.getAbsolutePath());
			root.add(driveNode);
			displayFolder(drive, driveNode);
		}
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				if (selectedNode.getUserObject() instanceof String) {
					String filePath = (String) selectedNode.getUserObject();
					File file = new File(filePath);
					updataTable(file);
					filePath_textField.setText(file.getPath());
				}
			}
		});
	}

	protected void updataTable(File folder) {
		File[] files = folder.listFiles();
		tableModel.setRowCount(0);
		if (files != null) {
			for (File file : files) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				Path path = Paths.get(file.getAbsolutePath());
				FileTime fileTime = null;
				Date date = null;
				try {
					fileTime = Files.getLastModifiedTime(path);
					date = new Date(fileTime.toMillis());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String type = file.isDirectory() ? "Folder" : "File";
				String size = file.isDirectory() ? Long.toString(calculateFolderSize(file)) + " bytes" : Long.toString(file.length()) + " bytes";
				tableModel.addRow(new Object[] {file.getName(), sdf.format(date), type, size});
			}
		}
	}

	private long calculateFolderSize(File folder) {
		long length = 0;
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					length += file.length();
				}else {
					length += calculateFolderSize(file);
				}
			}
		}
		return length;
	}

	private void displayFolder(File folder, DefaultMutableTreeNode parentNode) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getAbsolutePath());
				parentNode.add(childNode);
				if (file.isDirectory()) {
					displayFolder(file, childNode);
				}
			}
		}
	}
}
