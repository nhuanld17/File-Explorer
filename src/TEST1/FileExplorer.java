package TEST1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class FileExplorer extends JFrame{
	private JTree tree;
	private JTable table;
	private DefaultTableModel tableModel;
	private JTextField textField;
	private JPopupMenu popupMenu;
	
    private File copiedFile = null;
    private boolean isCut = false;
	
	public FileExplorer() {
		super("Drive Explorer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        // Tạo textField chứa đường dẫn của file
        textField = new JTextField("");
        textField.setPreferredSize(new Dimension(200, 30));
        
        // Tạo cấu trúc cây thư mục
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("DISK");
        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        // Tạo bảng để hiển thị thông tin file và thư mục
        String[] columnNames = {"Name", "Type", "Size"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        
        // Hiển thị ổ cứng và các folder con
        File[] drives = File.listRoots();
        for (File drive : drives) {
			DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(drive.getAbsolutePath());
			root.add(driveNode);
			displayFolder(drive, driveNode);
		}
        
        tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getPath();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (selectedNode.getUserObject() instanceof String) {
					String filePath = (String) selectedNode.getUserObject();
					File file = new File(filePath);
					updateTable(file);
					textField.setText(file.getPath());
				}
			}
		});
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(textField, BorderLayout.NORTH);
        
        JScrollPane treeScrollPane = new JScrollPane(tree);
        JScrollPane tableScrollPane = new JScrollPane(table);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane,tableScrollPane);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        createContextMenu();
        pack();
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void createContextMenu() {
		popupMenu = new JPopupMenu();
		JMenuItem newFolderItem = new JMenuItem("New Folder");
		JMenuItem deleteItem = new JMenuItem("Delete");
		JMenuItem renameItem = new JMenuItem("Rename");
		JMenuItem propertiesItem = new JMenuItem("Properties");
		JMenuItem CopyItem = new JMenuItem("Copy");
		JMenuItem PastesItem = new JMenuItem("Paste");
		JMenuItem CutItem = new JMenuItem("Cut");
		
		popupMenu.add(newFolderItem);
		popupMenu.add(deleteItem);
		popupMenu.add(renameItem);
		popupMenu.add(propertiesItem);
		popupMenu.add(CopyItem);
		popupMenu.add(CutItem);
		popupMenu.add(PastesItem);
		
		newFolderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewFolder();
			}
		});
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedFileOrFolder();
			}
		});
		renameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renameSelectedFileOrFolder();
			}
		});
		
		propertiesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showProperties();
			}
		});
		
		CopyItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Copy();
			}
		});
		CutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cut();
			}
		});
		PastesItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Paste();
			}
		});
		
		tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) showMenu(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) showMenu(e);
            }

            private void showMenu(MouseEvent e) {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
		});
	}

	private void showProperties() {
	    TreePath currentPath = tree.getSelectionPath();
	    if (currentPath == null) return;

	    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
	    File selectedFile = new File(selectedNode.getUserObject().toString());

	    long size = selectedFile.isDirectory() ? calculateFolderSize(selectedFile) : selectedFile.length();
	    String info = "Name: " + selectedFile.getName() +
	                  "\nPath: " + selectedFile.getPath() +
	                  "\nSize: " + size + " bytes" +
	                  "\nLast Modified: " + new Date(selectedFile.lastModified()) +
	                  "\nReadable: " + selectedFile.canRead() +
	                  "\nWritable: " + selectedFile.canWrite() +
	                  "\nExecutable: " + selectedFile.canExecute();

	    JOptionPane.showMessageDialog(this, info, "Properties", JOptionPane.INFORMATION_MESSAGE);
	}

	private void deleteSelectedFileOrFolder() {
	    TreePath currentPath = tree.getSelectionPath();
	    if (currentPath == null) return;

	    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
	    File selectedFile = new File(selectedNode.getUserObject().toString());

	    int response = JOptionPane.showConfirmDialog(this,
	        "Are you sure you want to delete: " + selectedFile.getName() + "?",
	        "Confirm Delete",
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.WARNING_MESSAGE);

	    if (response == JOptionPane.YES_OPTION) {
	        if (deleteRecursive(selectedFile)) {
	            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(selectedNode);
	            updateTable((File) ((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject());
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to delete the file/folder.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}

	private boolean deleteRecursive(File file) {
	    if (file.isDirectory()) {
	        File[] children = file.listFiles();
	        if (children != null) {
	            for (File child : children) {
	                boolean success = deleteRecursive(child);
	                if (!success) {
	                    return false;
	                }
	            }
	        }
	    }
	    return file.delete();
	}

	private void renameSelectedFileOrFolder() {
        TreePath currentPath = tree.getSelectionPath();
        if (currentPath == null) return;

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
        File selectedFile = new File(selectedNode.getUserObject().toString());

        String newName = JOptionPane.showInputDialog(this, "Enter new name:", selectedFile.getName());
        if (newName != null && !newName.equals(selectedFile.getName())) {
            File newFile = new File(selectedFile.getParent(), newName);
            if (selectedFile.renameTo(newFile)) {
                selectedNode.setUserObject(newFile.getAbsolutePath());
                ((DefaultTreeModel) tree.getModel()).nodeChanged(selectedNode);
                updateTable(newFile.getParentFile());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to rename the file/folder.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

	private void createNewFolder() {
        TreePath currentPath = tree.getSelectionPath();
        if (currentPath == null) return;

        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
        File selectedFile = new File(selectedNode.getUserObject().toString());

        if (selectedFile.isDirectory()) {
            String folderName = JOptionPane.showInputDialog("Enter Folder Name:");
            if (folderName != null && !folderName.trim().isEmpty()) {
                File newFolder = new File(selectedFile, folderName);
                if (newFolder.mkdir()) {
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFolder.getAbsolutePath());
                    selectedNode.add(newNode);
                    ((DefaultTreeModel) tree.getModel()).reload(selectedNode);
                    updateTable(selectedFile);  // Cập nhật lại JTable nếu cần
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create new folder.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
	}
	
	protected void Copy() {
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (selectedNode.getUserObject() instanceof String) {
                copiedFile = new File((String) selectedNode.getUserObject());
                isCut = false;
            }
        }
    }

    protected void Cut() {
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (selectedNode.getUserObject() instanceof String) {
                copiedFile = new File((String) selectedNode.getUserObject());
                isCut = true;
            }
        }
    }

    protected void Paste() {
        TreePath path = tree.getSelectionPath();
        if (path != null && copiedFile != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (selectedNode.getUserObject() instanceof String) {
                File destinationDir = new File((String) selectedNode.getUserObject());
                if (destinationDir.isDirectory()) {
                    File newFile = new File(destinationDir, copiedFile.getName());
                    try {
                        if (isCut) {
                            Files.move(copiedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            copiedFile = null;
                        } else {
                            Files.copy(copiedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                        updateTable(destinationDir);
                        selectTreeNode(newFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error during paste operation", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    private void selectTreeNode(String path) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode node = findNode(root, path);
        if (node != null) {
            TreePath treePath = new TreePath(node.getPath());
            tree.setSelectionPath(treePath);
            tree.scrollPathToVisible(treePath);
        }
    }
    
    private DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, String path) {
        if (root.getUserObject().equals(path)) {
            return root;
        }
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
            DefaultMutableTreeNode result = findNode(child, path);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

	private void updateTable(File folder) {
		File[] files = folder.listFiles();
		tableModel.setRowCount(0); // Xóa dữ liệu hiện tại
		if (files != null) {
			for (File file : files) {
				String type = file.isDirectory() ? "Folder" : "File";
				String size = file.isDirectory() ? Long.toString(calculateFolderSize(file)) + " bytes" : Long.toString(file.length()) + " bytes";
				tableModel.addRow(new Object[] {file.getName(), type, size});
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
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new FileExplorer().setVisible(true);
		});
	}
}
