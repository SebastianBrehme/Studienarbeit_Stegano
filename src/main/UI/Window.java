package main.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Window extends JFrame {

	JTextArea imagePathArea;
	JTextArea filePathArea;
	JButton chooseFilePathButton;
	JTextArea messageArea;
	JTextArea cryptokeyArea;
	JTextArea datapathArea;
	JRadioButton messageRadioButton;
	JLabel imageLabel;
	
	public Window() {
		super();
		init();
	}
	
	private void init() {
		setTitle("Steganographie");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1050, 600);
		setResizable(false);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(1, 2, 5, 5));
		
		
		
		JPanel dataPanel = createDataPanel();
		JPanel imagePanel = createImagePanel();
		JPanel buttonPanel = createButtonsPanel();
		gridPanel.add(dataPanel);
		gridPanel.add(imagePanel);
		mainPanel.add(gridPanel);
		mainPanel.add(buttonPanel);
		
		add(mainPanel);
	}
	
	private JPanel createDataPanel() {
		JPanel panel = new JPanel();
		panel.setSize(512, 512);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(6, 2, 5, 7));
		
		//Fileinput
		gridPanel.add(new JLabel("Choose an image:"));
		gridPanel.add(createImageInputPanel());
		
		//choose data input 
		gridPanel.add(new JLabel("What kind of data would you like to hide?"));
		gridPanel.add(createChooseInputTypePanel());
		
		
		//file as input
		gridPanel.add(new JLabel("Choose File"));
		gridPanel.add(createFileChooserPanel());
		
		//message as input

		gridPanel.add(new JLabel("Message to hide:"));
		gridPanel.add(createMessageScrollPane());
		
		//cryptokey
		gridPanel.add(new JLabel("Cryptography Key:"));
		gridPanel.add(createCryptokeyScrollPane());
		
		
		//data path
		gridPanel.add(new JLabel("Information Path:"));
		gridPanel.add(createDatapathScrollPane());
		
		panel.add(gridPanel);
		return panel;
	}
	
	private JPanel createImageInputPanel() {
		JPanel imageinput = new JPanel();
		imagePathArea = new JTextArea();
		imagePathArea.setColumns(20);
		imagePathArea.setTabSize(20);
		imagePathArea.setLineWrap(false);
		
		imagePathArea.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				if (imagePathArea.getText().toLowerCase().endsWith(".jpg")) {
					showImage(imagePathArea.getText());
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		
		JScrollPane scroll = new JScrollPane(imagePathArea);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		JButton chooseButton = new JButton("Image File Path");
		chooseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg"));
				fc.setFileFilter(new FileFilter() {
					
					@Override
					public String getDescription() {
						return "JPG Images";
					}
					
					@Override
					public boolean accept(File f) {
						String name = f.getName().toLowerCase();
						return f.isDirectory() ||
								name.endsWith(".jpg")||
								name.endsWith(".jpeg");
					}
				});
				int state = fc.showOpenDialog(null);
				if (state == JFileChooser.APPROVE_OPTION) {
					String absolutefilepath = fc.getSelectedFile().getAbsolutePath();
					imagePathArea.setText(absolutefilepath);
					
					showImage(absolutefilepath);
				}
			}
		});
		
		imageinput.add(scroll);
		imageinput.add(chooseButton);
		return imageinput;
	}
	
	private void showImage(String absolutefilepath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(absolutefilepath));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ImageIcon ic = new ImageIcon(img);
		ic.setImage(getScaledImage(ic.getImage(), 512, 512));
		
		imageLabel.setIcon(ic);
	}
	
	private JPanel createChooseInputTypePanel() {
		JPanel chooseInputType = new JPanel();
		chooseInputType.setLayout(new BorderLayout());
		ButtonGroup bg = new ButtonGroup();
		JRadioButton fileRadioButton = new JRadioButton("File", true);
		fileRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileRadioButton.isSelected()) {
					messageArea.setEditable(false);
					filePathArea.setEditable(true);
					chooseFilePathButton.setEnabled(true);
				}
			}
		});
		messageRadioButton = new JRadioButton("Message", false);
		messageRadioButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (messageRadioButton.isSelected()) {
					messageArea.setEditable(true);
					filePathArea.setEditable(false);
					chooseFilePathButton.setEnabled(false);
				}
			}
		});
		bg.add(fileRadioButton);
		bg.add(messageRadioButton);
		chooseInputType.add(fileRadioButton, BorderLayout.WEST);
		chooseInputType.add(messageRadioButton, BorderLayout.CENTER);
		return chooseInputType;
	}
	
	private JPanel createFileChooserPanel() {
		JPanel fileinput = new JPanel();
		filePathArea = new JTextArea();
		filePathArea.setColumns(20);
		filePathArea.setTabSize(20);
		filePathArea.setLineWrap(false);
		JScrollPane fileinputscroll = new JScrollPane(filePathArea);
		fileinputscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		fileinputscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		chooseFilePathButton = new JButton("Choose File");
		chooseFilePathButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int state = fc.showOpenDialog(null);
				if (state == JFileChooser.APPROVE_OPTION) {
					filePathArea.setText(fc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		fileinput.add(fileinputscroll);
		fileinput.add(chooseFilePathButton);
		return fileinput;
	}
	
	private JScrollPane createMessageScrollPane() {
		messageArea = new JTextArea(4, 20);
		messageArea.setLineWrap(false);
		messageArea.setEditable(false);
		JScrollPane scrollableMessage = new JScrollPane(messageArea);
		return scrollableMessage;
	}
	
	private JScrollPane createCryptokeyScrollPane() {
		cryptokeyArea = new JTextArea(4, 20);
		cryptokeyArea.setLineWrap(false);
		JScrollPane scrollableCryptokey = new JScrollPane(cryptokeyArea);
		return scrollableCryptokey;
	}
	
	private JScrollPane createDatapathScrollPane() {
		datapathArea = new JTextArea(4, 20);
		datapathArea.setLineWrap(false);
		JScrollPane scrollableDataPath = new JScrollPane(datapathArea);
		return scrollableDataPath;
	}
	
	private JPanel createImagePanel() {
		JPanel imagePanel = new JPanel();
		imagePanel.setSize(512, 512);
		imageLabel = new JLabel();
		imagePanel.add(imageLabel);
		return imagePanel;
	}
	
	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.dispose();

	    return resizedImg;
	}
	
	private JPanel createButtonsPanel() {
		JPanel panel = new JPanel();
		JButton hideButton = new JButton("Hide Data");
		JButton retrieveButton = new JButton("Retrieve Data");
		
		hideButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				hideData();
			}
		});
		
		retrieveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String saveToFile = "Save to file";
				String justShowMessage = "Just show Message";
				String[] options = { saveToFile, justShowMessage };
				String selection = (String) JOptionPane.showInputDialog(null, "What would you like to do with the received data?", "Retrive Data", JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (saveToFile.equals(selection)) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int state = fc.showSaveDialog(null);
					if (state == JFileChooser.APPROVE_OPTION) {
						retrieveData(fc.getSelectedFile().getAbsolutePath());
					}
				} else if (justShowMessage.equals(selection)) {
					retrieveData(null);
				}
			}
		});
		
		panel.add(hideButton);
		panel.add(retrieveButton);
		return panel;
	}
	
	private void hideData() {
		SteganographieHandler steg = new SteganographieHandler();
		
		String imageInput = checkTextAreaText(imagePathArea, "Image File Path");
		if (imageInput == null) { return; }
		steg.imageInputPath = imageInput;
		
		if (messageRadioButton.isSelected()) {
			steg.message = messageArea.getText();
		} else {
			steg.dataInputPath = filePathArea.getText();
		}
		
		String usersCryptoKey = cryptokeyArea.getText();
		if (!"".equals(usersCryptoKey)) {
			if (usersCryptoKey.length() != 16) {
				JOptionPane.showMessageDialog(null, "The Cryptograhy Key needs to be 16-Bytes long", "Unusable Cryptography Key", JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				steg.cryptoKey = usersCryptoKey;
			}
		}
		
		String dataPathInput = checkTextAreaText(datapathArea, "Information Path");
		if (dataPathInput == null) { return; }
		steg.stegoKey = dataPathInput;
		
		if (!steg.startSteganographie()) {
			//display error message
		} else {
			JOptionPane.showMessageDialog(null,  "Successfully Done");
			cryptokeyArea.setText(steg.cryptoKey);
		}
	}
	
	private void retrieveData(String filePath) {
		SteganographieHandler steg = new SteganographieHandler();
		
		String imageInput = checkTextAreaText(imagePathArea, "Image File Path");
		if (imageInput == null) { return; }
		steg.imageInputPath = imageInput;
		
		String dataPathInput = checkTextAreaText(datapathArea, "Information Path");
		if (dataPathInput == null) { return; }
		steg.stegoKey = dataPathInput;		
		
		String cryptokeyInput = checkTextAreaText(cryptokeyArea, "Cryptography Key");
		if (cryptokeyInput == null) { return; }
		steg.cryptoKey = cryptokeyInput;
		
		String content = steg.retrieveDataOutOfImage(filePath);
		if (content == null) {
			JOptionPane.showMessageDialog(null, steg.error, "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			messageArea.setText(content);
		}
	}
	
	private String checkTextAreaText(JTextArea area, String src) {
		String text = area.getText();
		if ("".equals(text)) {
			CantBeEmptyError(src);
			return null;
		} else {
			return text;
		}
	}
	
	private void CantBeEmptyError(String src) {
		JOptionPane.showMessageDialog(null, src + " can not be empty!", "Information is missing", JOptionPane.ERROR_MESSAGE);
	}
}
