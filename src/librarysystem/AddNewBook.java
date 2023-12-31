package librarysystem;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Author;
import business.Book;
import business.LibrarySystemException;
import dataaccess.TestData;

public class AddNewBook extends JFrame implements LibWindow {
	public static final AddNewBook INSTANCE = new AddNewBook();
	private static final long serialVersionUID = -2738187895041301834L;
	private boolean isInit;
	private JTextField tf_BookIsbn;
	private JTextField tf_Title;
	private JTextField tf_MaxCheckout;
	private JTextField tf_Copies;
	private List<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	
	@Override
	public void init() {
		if (isInit == false) {
			this.setLocation(250, 250);
			TestData data = new TestData();
			List<Author> authors = data.allAuthors;
			
			JPanel mainPanel = new JPanel();
			BorderLayout bl = new BorderLayout();
			mainPanel.setLayout(bl);
			//1. Title
			JLabel lblTitle = new JLabel();
			lblTitle.setText("Add New Book");
			mainPanel.add(lblTitle, BorderLayout.NORTH);
			
			//2. Form Input
			JPanel formPanel = new JPanel();
			formPanel.setLayout(new GridLayout(6 + authors.size(),1));
			mainPanel.add(formPanel, BorderLayout.CENTER);
			
			//2.1 Book ISBN
			{
				JPanel rowPanel = new JPanel();
				rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel lbl = new JLabel();
				lbl.setText("Book Isbn");
				rowPanel.add(lbl);
				JTextField tf = new JTextField(20);
				rowPanel.add(tf);
				formPanel.add(rowPanel);
				
				this.tf_BookIsbn = tf;
			}
			
			//2.2 Title
			{
				JPanel rowPanel = new JPanel();
				rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel lbl = new JLabel();
				lbl.setText("Title");
				rowPanel.add(lbl);
				JTextField tf = new JTextField(50);
				rowPanel.add(tf);
				formPanel.add(rowPanel);
				
				this.tf_Title = tf;
			}
			
			//2.3 Max Checkout
			{
				JPanel rowPanel = new JPanel();
				rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel lbl = new JLabel();
				lbl.setText("Max Checkout Length");
				rowPanel.add(lbl);
				JTextField tf = new JTextField(10);
				rowPanel.add(tf);
				formPanel.add(rowPanel);
				
				this.tf_MaxCheckout = tf;
			}
			
			//2.4 Copy
			{
				JPanel rowPanel = new JPanel();
				rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel lbl = new JLabel();
				lbl.setText("Number of Copies");
				rowPanel.add(lbl);
				JTextField tf = new JTextField(10);
				rowPanel.add(tf);
				formPanel.add(rowPanel);
				
				this.tf_Copies = tf;
			}
			
			// 2.5 Authors title
			{
				JPanel rowPanel = new JPanel();
				rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel lbl = new JLabel();
				lbl.setText("Authors:");
				rowPanel.add(lbl);
				
				formPanel.add(rowPanel);
			}
			
			for (int i = 0; i < authors.size() ; ++i) {
				Author au = authors.get(i);
				JCheckBox checkBox = new JCheckBox( (i +1) +". " + au.getFirstName() + " " + au.getFirstName());
				if (i == 0) {
					checkBox.setSelected(true);
				}
				checkBoxes.add(checkBox);
				formPanel.add(checkBox);
			}
			
			//2.6 Buttons
			{
				JPanel rowBtns = new JPanel();
				rowBtns.setLayout(new FlowLayout(FlowLayout.CENTER));
				JButton btnAdd = new JButton(" Add ");
				btnAdd.addActionListener(evt -> {
					try {
						int max = Integer.parseInt(tf_MaxCheckout.getText().trim());
						int copies = Integer.parseInt(tf_Copies.getText().trim());
						String isbn = tf_BookIsbn.getText().trim();
						String title = tf_Title.getText().trim();
						List<Author> lists = new ArrayList<Author>();
						for (int i = 0; i < checkBoxes.size() ; i++) {
							JCheckBox cb = checkBoxes.get(i);
							if (cb.isSelected()) {
								lists.add(authors.get(i));
							}
						}
						LibrarySystem.INSTANCE.ci.addNewBook(isbn, title, max, lists, copies);
						JOptionPane.showMessageDialog(AddNewBook.this, "Successful Add New Book");
						LibrarySystem.INSTANCE.repaint();
						AddNewBook.this.backToMain();
					} catch (LibrarySystemException e) {
						JOptionPane.showMessageDialog(AddNewBook.this, e.getMessage());
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(AddNewBook.this, "Max Checkout Length and Number of Copies must be Integer");
					}
				});
				
				JButton btnClear = new JButton(" Clear ");
				btnClear.addActionListener(evt -> {
					clearData();
				});
				rowBtns.add(btnAdd);
				rowBtns.add(btnClear);
				formPanel.add(rowBtns);
			}
			
			
			//3. Back To Main
			JPanel lowerHalf = new JPanel();
			JButton backButton = new JButton("<= Back to Main");
			backButton.addActionListener(evt -> {
				backToMain();
			});
			lowerHalf.add(backButton);
			mainPanel.add(lowerHalf, BorderLayout.SOUTH);
			//
			getContentPane().add(mainPanel);
			isInit = true;
		}
	}

	@Override
	public boolean isInitialized() {
		return isInit;
	}

	@Override
	public void isInitialized(boolean val) {
		isInit = val;
		
	}

	private void clearData() {
		tf_BookIsbn.setText("");
		tf_Title.setText("");
		tf_Copies.setText("");
		tf_MaxCheckout.setText("");
	}
	
	private void backToMain() {
		LibrarySystem.hideAllWindows();
		LibrarySystem.INSTANCE.setVisible(true);
		clearData();
	}
}
