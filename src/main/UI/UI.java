package main.UI;

import javax.swing.SwingUtilities;

public class UI {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Window window = new Window();
				window.setVisible(true);
			}
		});
	}

}
