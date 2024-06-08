package LazyLoading;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PropertiesGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;


	/**
	 * Create the frame.
	 */
	public PropertiesGUI(String name ,String type, String location, long size, String date, boolean isReadable, boolean isWrittable, boolean isExcutable) {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(PropertiesGUI.class.getResource("/icon/icons8-folder-48.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 379, 462);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(249, 249, 249));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(PropertiesGUI.class.getResource("/icon/icons8-folder-48.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 24, 48, 46);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField(name);
		textField.setForeground(SystemColor.desktop);
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		textField.setBorder(new LineBorder(SystemColor.desktop));
		textField.setBounds(104, 35, 249, 28);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(SystemColor.desktop);
		separator.setBackground(SystemColor.desktop);
		separator.setBounds(10, 77, 343, 2);
		contentPane.add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Type:");
		lblNewLabel_1.setForeground(SystemColor.desktop);
		lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(10, 90, 88, 28);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Location:");
		lblNewLabel_1_1.setForeground(SystemColor.desktop);
		lblNewLabel_1_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1_1.setBounds(10, 129, 88, 28);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_2 = new JLabel("Size:");
		lblNewLabel_1_2.setForeground(SystemColor.desktop);
		lblNewLabel_1_2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblNewLabel_1_2.setBounds(10, 168, 88, 28);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel label_TYPE = new JLabel(type);
		label_TYPE.setForeground(SystemColor.desktop);
		label_TYPE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_TYPE.setBounds(104, 90, 249, 28);
		contentPane.add(label_TYPE);
		
		JLabel label_LOCATION = new JLabel(location);
		label_LOCATION.setForeground(SystemColor.desktop);
		label_LOCATION.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_LOCATION.setBounds(104, 129, 249, 28);
		contentPane.add(label_LOCATION);
		
		JLabel label_SIZE = new JLabel(size+" bytes");
		label_SIZE.setForeground(SystemColor.desktop);
		label_SIZE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_SIZE.setBounds(104, 168, 249, 28);
		contentPane.add(label_SIZE);
		
		JLabel label_ = new JLabel("Mofidy:");
		label_.setForeground(SystemColor.desktop);
		label_.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_.setBounds(10, 207, 88, 28);
		contentPane.add(label_);
		
		JLabel label_READ = new JLabel("Readable:");
		label_READ.setForeground(SystemColor.desktop);
		label_READ.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_READ.setBounds(10, 265, 88, 28);
		contentPane.add(label_READ);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(SystemColor.desktop);
		separator_1.setBackground(SystemColor.desktop);
		separator_1.setBounds(10, 252, 343, 2);
		contentPane.add(separator_1);
		
		JLabel label_READABLE_1 = new JLabel("Writtable:");
		label_READABLE_1.setForeground(SystemColor.desktop);
		label_READABLE_1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_READABLE_1.setBounds(10, 304, 88, 28);
		contentPane.add(label_READABLE_1);
		
		JLabel lable_ex = new JLabel("Excutable:");
		lable_ex.setForeground(SystemColor.desktop);
		lable_ex.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lable_ex.setBounds(10, 349, 88, 28);
		contentPane.add(lable_ex);
		
		JButton btnNewButton = new JButton("Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		btnNewButton.setFocusable(false);
		btnNewButton.setHorizontalTextPosition(SwingConstants.CENTER);
		btnNewButton.setBackground(new Color(249, 249, 249));
		btnNewButton.setBorder(new LineBorder(SystemColor.desktop, 2));
		btnNewButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		btnNewButton.setForeground(SystemColor.desktop);
		btnNewButton.setBounds(264, 389, 89, 27);
		contentPane.add(btnNewButton);
		
		JLabel label_MODIFY = new JLabel(date);
		label_MODIFY.setHorizontalTextPosition(SwingConstants.CENTER);
		label_MODIFY.setHorizontalAlignment(SwingConstants.LEFT);
		label_MODIFY.setForeground(SystemColor.desktop);
		label_MODIFY.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_MODIFY.setBounds(104, 207, 249, 28);
		contentPane.add(label_MODIFY);
		
		JLabel label_READABLE = new JLabel(isReadable+"");
		label_READABLE.setHorizontalTextPosition(SwingConstants.CENTER);
		label_READABLE.setHorizontalAlignment(SwingConstants.LEFT);
		label_READABLE.setForeground(SystemColor.desktop);
		label_READABLE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_READABLE.setBounds(104, 265, 249, 28);
		contentPane.add(label_READABLE);
		
		JLabel label_WRITTABLE = new JLabel(isWrittable+"");
		label_WRITTABLE.setHorizontalTextPosition(SwingConstants.CENTER);
		label_WRITTABLE.setHorizontalAlignment(SwingConstants.LEFT);
		label_WRITTABLE.setForeground(SystemColor.desktop);
		label_WRITTABLE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_WRITTABLE.setBounds(104, 304, 249, 28);
		contentPane.add(label_WRITTABLE);
		
		JLabel label_EXCUTABLE = new JLabel(isExcutable+"");
		label_EXCUTABLE.setHorizontalTextPosition(SwingConstants.CENTER);
		label_EXCUTABLE.setHorizontalAlignment(SwingConstants.LEFT);
		label_EXCUTABLE.setForeground(SystemColor.desktop);
		label_EXCUTABLE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		label_EXCUTABLE.setBounds(104, 349, 249, 28);
		contentPane.add(label_EXCUTABLE);
	}
}
