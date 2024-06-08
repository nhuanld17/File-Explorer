package LazyLoading;

// FileExplorer, UnOffical (lazy)
// FileExplorer, (TEST, PACK)
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class FileExplorer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField filePath_textField;
	private JTextField find_textField;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane scrollPane_1;
	private JPopupMenu popupMenu;
	private JTree tree;
	private JButton btnNewButton;

    private File copiedFile = null;
    private boolean isCut = false;
    private TreePath cutTreePath = null;
	
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
		setBounds(100, 100, 1044, 494);
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
		panel.setBounds(0, 0, 1028, 455);
		contentPane.add(panel);
		panel.setLayout(null);
		
		filePath_textField = new JTextField();
		filePath_textField.setBorder(new LineBorder(new Color(0, 0, 0)));
		filePath_textField.setForeground(SystemColor.desktop);
		filePath_textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		filePath_textField.setBounds(10, 5, 744, 30);
		panel.add(filePath_textField);
		filePath_textField.setColumns(10);
		
		find_textField = new JTextField();
		find_textField.setBorder(new LineBorder(SystemColor.desktop));
		find_textField.setForeground(SystemColor.desktop);
		find_textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		find_textField.setBounds(764, 5, 188, 30);
		panel.add(find_textField);
		find_textField.setColumns(10);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("DISK");
		tree = new JTree(root);
		tree.setForeground(new Color(244, 245, 249));
		tree.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tree.setBorder(new LineBorder(SystemColor.desktop));
		tree.setBackground(new Color(244, 245, 249));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);;
		tree.setBounds(10, 50, 215, 414);
		tree.setRowHeight(20);
		
		DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
		renderer.setClosedIcon(new ImageIcon(getClass().getResource("/icon/icons8-folder-24.png")));
        renderer.setOpenIcon(new ImageIcon(getClass().getResource("/icon/icons8-opened-folder-24.png")));
        renderer.setLeafIcon(new ImageIcon(getClass().getResource("/icon/icons8-folder-24.png")));
		
		String[] columnNames = {"Name","Date Modify" ,"Type", "Size","Path"};
		tableModel = new DefaultTableModel(columnNames,0);
		table = new JTable(tableModel);
		table.setRowHeight(24);
		table.setForeground(SystemColor.desktop);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setEnabled(false);
		table.setBounds(222, 41, 639, 414);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(249, 46, 769, 398);
		panel.add(scrollPane);
		
		scrollPane_1 = new JScrollPane(tree);
		scrollPane_1.setBounds(10, 46, 229, 398);
		panel.add(scrollPane_1);
		
		btnNewButton = new JButton("");
		btnNewButton.setFocusable(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBackground(new Color(244, 245, 249));
		btnNewButton.addActionListener(this);
		btnNewButton.setIcon(new ImageIcon("F:\\eclipse-workspace\\DACS\\src\\image\\icons8-find-30.png"));
		btnNewButton.setBounds(962, 5, 35, 30);
		panel.add(btnNewButton);
		
		File[] drives = File.listRoots();
        for (File drive : drives) {
            DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(drive.getAbsolutePath());
            root.add(driveNode);
            driveNode.add(new DefaultMutableTreeNode("Loading..."));  // Placeholder for lazy loading
        }
        
        /*
         * Khi các ổ đĩa được tạo ra, mỗi ổ đĩa được thêm vào cây như một nút mới và mỗi nút nầy
         * sẽ có 1 nút con duy nhất là "Loading..." như một giá trị trình giữ chỗ. Nó cho người 
         * dùng biết rằng có thêm nội dung có thể được tải, nhưng nội dung đó chưa được tải ngay
         * lập tức. 
         */
        
        // Xử lí các hoạt động cần thiết trước khi một nút trên cây được mở rộng
        tree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            // treeWillExpand là phương thức được gọi trước khi được mở rộng
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
                
                
                //Nếu trong node hiện tại có 1 nút là "Loading..." thì nút này sẽ được xóa.    
                if (node.getChildCount() == 1 && "Loading...".equals(node.getFirstChild().toString())) {
                    node.removeAllChildren();  
                    
                    /* - node là đối tượng của một lớp "DefaultMutableTreeNode", được sử dụng trong JTree 
                     * để đại diện cho một nút.
                     * - Mỗi nút trong JTree có thể chứa 1 đối tượng người dùng (user's object) liên kết với nó,
                     * thường được sử dụng để lưu trữ dữ liệu liên quan đến nút đó
                     * - getUserObject() dùng để truy xuất đối tượng người dùng
                     * - toString(): chuyển đổi đối tượng người dùng này thành một chuỗi, trong trường hợp này, đối
                     * tượng người dùng sẽ là 1 chuỗi chứa đường dẫn của File hoặc Folder, toString() sẽ trả về chuỗi
                     * đường dẫn đó
                     */
                    
                    File folder = new File(node.getUserObject().toString()); // Tạo file dựa trên đường dẫn
                    displayFolder(folder, node); // Hiện thị các Folder con trong nút hiện tại
                }
            }
            
            /*
             * event: là đối tượng chứa thông tin về sự kiện mở rộng hoặc thu gọn của 1 nút trên cây
             * getPath() trả về đối tượng treePath, là đường dẫn từ gốc đến nút đang xảy ra sự kiện
             * getLastPathComponent trả về đối tượng ở cuối của đường dẫn, tức là nút đang xảy ra sự kiện
             */

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            }
        });
        
//        DragSource ds = new DragSource();
//        ds.createDefaultDragGestureRecognizer(tree, DnDConstants.ACTION_MOVE, new DragGestureListener() {
//            @Override
//            public void dragGestureRecognized(DragGestureEvent dge) {
//                TreePath path = tree.getSelectionPath();
//                if (path != null) {
//                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
//                    if (selectedNode.getUserObject() instanceof String) {
//                        File file = new File((String) selectedNode.getUserObject());
//                        if (file.exists()) {
//                            Transferable transferable = new TransferableFile(file);
//                            ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable, new DragSourceListener() {
//                                @Override
//                                public void dragEnter(DragSourceDragEvent dsde) {
//                                }
//
//                                @Override
//                                public void dragOver(DragSourceDragEvent dsde) {
//                                }
//
//                                @Override
//                                public void dropActionChanged(DragSourceDragEvent dsde) {
//                                }
//
//                                @Override
//                                public void dragExit(DragSourceEvent dse) {
//                                }
//
//                                @Override
//                                public void dragDropEnd(DragSourceDropEvent dsde) {
//                                    if (dsde.getDropSuccess()) {
//                                        System.out.println("File moved successfully");
//                                    }
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        });
//
//        new DropTarget(tree, new DropTargetListener() {
//            @Override
//            public void dragEnter(DropTargetDragEvent dtde) {
//            }
//
//            @Override
//            public void dragOver(DropTargetDragEvent dtde) {
//            }
//
//            @Override
//            public void dropActionChanged(DropTargetDragEvent dtde) {
//            }
//
//            @Override
//            public void dragExit(DropTargetEvent dte) {
//            }
//
//            @Override
//            public void drop(DropTargetDropEvent dtde) {
//                try {
//                    Transferable tr = dtde.getTransferable();
//                    if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
//                        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
//                        List<File> fileList = (List<File>) tr.getTransferData(DataFlavor.javaFileListFlavor);
//                        TreePath destPath = tree.getPathForLocation(dtde.getLocation().x, dtde.getLocation().y);
//                        if (destPath != null) {
//                            DefaultMutableTreeNode destNode = (DefaultMutableTreeNode) destPath.getLastPathComponent();
//                            File destDir = new File(destNode.getUserObject().toString());
//                            if (destDir.isDirectory()) {
//                                for (File file : fileList) {
//                                    File newFile = new File(destDir, file.getName());
//                                    Files.move(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                                }
//                                displayFolder(destDir, destNode);
//                                updataTable(destDir);
////                                tree.updateUI();
//                            }
//                        }
//                        dtde.dropComplete(true);
//                    } else {
//                        dtde.rejectDrop();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    dtde.rejectDrop();
//                }
//            }
//        });
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				/*
				 * - e.getPath():trả về đường dẫn của nút được chọn, VD : Chọn DISK->F:\->F:\C++ thì 
				 * nó sẽ trả về mảng các nút theo thứ tự lựa chọn [DISK, F:\, F:\C++]
				 * - getLastPathComponent: trả về phần tử cuối cùng của [DISK, F:\, F:\C++] tức là F:\C++
				 * - Sau đó ép thành kiểu DefaultMutableTreeNode
				 */
				TreePath path = e.getPath();
				System.out.println(path);
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
				
				// Nếu đối tượng người dùng của nút được chọn là 1 chuỗi hay không
				if (selectedNode.getUserObject() instanceof String) {
					String filePath =  (String) selectedNode.getUserObject(); // Lấy đường dẫn của nút
					File file = new File(filePath); // Tạo file từ đường dẫn
					updataTable(file); // Cập nhật thông tin của các Folder con và file con lên Jtable
					filePath_textField.setText(file.getPath());
				}
			}
		});
		createMenu();
	}

	private void createMenu() {
		popupMenu = new JPopupMenu();
		JMenuItem newFolderItem = new JMenuItem("New Folder");
		JMenuItem deleteItem = new JMenuItem("Delete");
		JMenuItem renameItem = new JMenuItem("Rename");
		JMenuItem properties = new JMenuItem("Properties");
		JMenuItem CopyItem = new JMenuItem("Copy");
		JMenuItem PastesItem = new JMenuItem("Paste");
		JMenuItem CutItem = new JMenuItem("Cut");
		JMenuItem Open = new JMenuItem("Open");
		JMenuItem refreshItem = new JMenuItem("Refresh");
		
		popupMenu.add(newFolderItem);
		popupMenu.add(deleteItem);
		popupMenu.add(renameItem);
		popupMenu.add(properties);
		popupMenu.add(CopyItem);
		popupMenu.add(CutItem);
		popupMenu.add(PastesItem);
		popupMenu.add(Open);
		popupMenu.add(refreshItem);
		
		newFolderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createNewFolder();
			}
		});
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSelectedFolderOrFile();
			}
		});
		renameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				renameFileOrFolder();
			}
		});
		properties.addActionListener(new ActionListener() {
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
		Open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		refreshItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTree();
			}
		});
		
		
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void showMenu(MouseEvent e) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	protected void showProperties() {
		TreePath currentPath = tree.getSelectionPath();
		if (currentPath == null) {
			return;
		}
		
		DefaultMutableTreeNode seletedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		File selectedFile = new File(seletedNode.getUserObject().toString());
		
		long size = selectedFile.isDirectory() ? calculateFolderSize(selectedFile) : selectedFile.length();
		String name = selectedFile.getName();
		String type = selectedFile.isDirectory() ? "Folder" : "File";
		String location = selectedFile.getParent();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		Path path = Paths.get(selectedFile.getAbsolutePath());
		FileTime fileTime = null;
		Date date = null;
		try {
			fileTime = Files.getLastModifiedTime(path);
			date = new Date(fileTime.toMillis());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boolean isReadable = selectedFile.canRead();
		boolean isWrittable = selectedFile.canWrite();
		boolean isExcutable = selectedFile.canExecute();
		
		new PropertiesGUI(name, type, location, size, 
				sdf.format(date), isReadable, isWrittable, isExcutable).setVisible(true);
	}

	protected void renameFileOrFolder() {
		TreePath currentPath = tree.getSelectionPath();
		if (currentPath == null) {
			return;
		}
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		File selectedFile = new File(selectedNode.getUserObject().toString());
		
		String newName = JOptionPane.showInputDialog(this, "Enter new name: ", selectedFile.getName());
		if (newName != null && !newName.equals(selectedFile.getName())) {
			File newFile = new File(selectedFile.getParent(), newName);
			if (selectedFile.renameTo(newFile)) {
				selectedNode.setUserObject(newFile.getAbsolutePath());
				((DefaultTreeModel) tree.getModel()).nodeChanged(selectedNode);
				updataTable(newFile.getParentFile());
			}
		}else {
			JOptionPane.showMessageDialog(this, newName);
		}
	}

	protected void deleteSelectedFolderOrFile() {
		TreePath currentPath = tree.getSelectionPath();
		if (currentPath == null) {
			return;
		}
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		File selectedFile = new File(selectedNode.getUserObject().toString());
		
		int respone = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this file ?","Confirm Delete",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		
		if (respone == JOptionPane.YES_OPTION) {
			if (deleteRecursive(selectedFile)) {
				((DefaultTreeModel)tree.getModel()).removeNodeFromParent(selectedNode);
				updataTable((File)((DefaultMutableTreeNode) selectedNode.getParent()).getUserObject());
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
	                if (!deleteRecursive(child)) {
	                    return false;
	                }
	            }
	        }
	    }
	    return file.delete();
	}
	
	private void openFile() {
		TreePath currentPath = tree.getSelectionPath();
		if (currentPath == null) {
			return;
		}
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		File selectedFile = new File(selectedNode.getUserObject().toString());
		
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Cannot open the file", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Desktop is not supported on this system", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	protected void createNewFolder() {
		TreePath currentPath = tree.getSelectionPath();
		if (currentPath == null) {
			return;
		}
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
		File selectedFile = new File(selectedNode.getUserObject().toString());
		
		if (selectedFile.isDirectory()) {
			String folderName = JOptionPane.showInputDialog("Enter folder name: ");
			if (folderName != null && !folderName.trim().isEmpty()) {
				File newFolder = new File(selectedFile, folderName);
				if (newFolder.mkdir()) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFolder.getAbsolutePath());
					selectedNode.add(newNode);
					((DefaultTreeModel) tree.getModel()).reload(selectedNode);
					updataTable(selectedFile);
				}else {
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
                // Lưu TreePath của tệp hoặc thư mục đã cắt
                cutTreePath = path; 
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
                            // Di chuyển tệp hoặc thư mục đến vị trí mới
                            Files.move(copiedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            copiedFile = null; // Xóa tham chiếu tới tệp đã cắt

                            // Xóa nút gốc từ cây sau khi di chuyển tệp
                            if (cutTreePath != null) {
                                DefaultMutableTreeNode cutNode = (DefaultMutableTreeNode) cutTreePath.getLastPathComponent();
                                DefaultMutableTreeNode cutParentNode = (DefaultMutableTreeNode) cutNode.getParent();
                                if (cutParentNode != null) {
                                    cutParentNode.remove(cutNode);
                                    ((DefaultTreeModel) tree.getModel()).reload(cutParentNode);
                                }
                                cutTreePath = null; // Reset TreePath sau khi đã di chuyển
                            }
                        } else {
                            // Sao chép tệp hoặc thư mục đến vị trí mới
                            Files.copy(copiedFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }

                        // Cập nhật bảng hiển thị tệp và thư mục
                        updataTable(destinationDir);

                        // Thêm nút mới vào JTree
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newFile.getAbsolutePath());
                        selectedNode.add(newNode); // Thêm nút mới vào nút được chọn
                        ((DefaultTreeModel) tree.getModel()).reload(selectedNode); // Làm mới JTree để hiển thị nút mới

                        // Chọn và hiển thị nút mới trong JTree
                        selectTreeNode(newFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Hiển thị thông báo lỗi nếu có lỗi trong quá trình paste
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

	protected void updataTable(File folder) {
		File[] files = folder.listFiles(); 
		tableModel.setRowCount(0); // Xóa dữ liệu hiện tại của bảng
		if (files != null) {
			for (File file : files) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				Path path = Paths.get(file.getAbsolutePath()); // tạo 1 đối tượng path dựa trên đường dẫn của file
				FileTime fileTime = null;
				Date date = null;
				try {
					fileTime = Files.getLastModifiedTime(path); // Lấy ngày thay đổi cuối cùng của file
					date = new Date(fileTime.toMillis()); // 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String type = file.isDirectory() ? "Folder" : "File";
				String size = file.isDirectory() ? Long.toString(calculateFolderSize(file)) + " bytes" : Long.toString(file.length()) + " bytes";
				String filePath = file.getAbsolutePath();
				tableModel.addRow(new Object[] {file.getName(), sdf.format(date), type, size, filePath});
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
        File[] files = folder.listFiles(); // Liệt kê các File và Folder con trong Folder
        if (files != null) {
            for (File file : files) {
            	// tạo các nút con với tên là đường dẫn của file
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getAbsolutePath());
                // Thêm node con vào node cha
                parentNode.add(childNode);
                if (file.isDirectory()) {
                    childNode.add(new DefaultMutableTreeNode("Loading..."));  // Add placeholder to indicate more items can be loaded
                }
            }
        }
    }
	
	private void findFile(String fileName, File file) {
	    if (file.isDirectory()) {
	        File[] listFile = file.listFiles();
	        if (listFile != null) {
	            for (File file2 : listFile) {
	                // Kiểm tra file hoặc thư mục có chứa tên cần tìm
	                if (file2.getName().contains(fileName)) {
	                    addFileToTable(file2); // Thêm thông tin file vào bảng
	                }
	                // Tiếp tục đệ quy tìm trong thư mục con
	                if (file2.isDirectory()) {
	                    findFile(fileName, file2);
	                }
	            }
	        }
	    }
	}

	private void addFileToTable(File file) {
	    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	    Path path = Paths.get(file.getAbsolutePath());
	    try {
	        FileTime fileTime = Files.getLastModifiedTime(path);
	        Date date = new Date(fileTime.toMillis());
	        String type = file.isDirectory() ? "Folder" : "File";
	        String size = file.isDirectory() ? Long.toString(calculateFolderSize(file)) + " bytes" : Long.toString(file.length()) + " bytes";
	        tableModel.addRow(new Object[] {file.getName(), sdf.format(date), type, size, file.getAbsolutePath()});
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private void refreshTree() {
	    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
	    DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
	    root.removeAllChildren();

	    File[] drives = File.listRoots();
	    for (File drive : drives) {
	        DefaultMutableTreeNode driveNode = new DefaultMutableTreeNode(drive.getAbsolutePath());
	        root.add(driveNode);
	        driveNode.add(new DefaultMutableTreeNode("Loading..."));  // Placeholder for lazy loading
	    }
	    model.reload();
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNewButton) {
			if (find_textField.getText().isBlank()) {
				return;
			}
			TreePath currentPath = tree.getSelectionPath();
			if (currentPath == null) {
				return;
			}
			
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
			File selectedFile = new File(selectedNode.getUserObject().toString());
			String fileName = find_textField.getText();
		    tableModel.setRowCount(0);
			findFile(fileName, selectedFile);
		}
	}
}
