//Code authored by Shiva

import javax.swing.*;
import java.util.ArrayList;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class GUI extends JFrame{
	private static final long serialVersionUID = 1L;

	File file = null;
	public void findDir(){		
		int i = JOptionPane.showConfirmDialog(null,
				"<html><body style ='width: 250px'>You have not chosen a default directory yet. In order to continue and run this software, "
					+ "you will need to choose a new default email directory. Would you like to do so now?</html>",
					"Spam Detector ",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
		if(i==0) {
			JFileChooser jc = new JFileChooser();
			jc.setCurrentDirectory(new File("."));
			jc.setDialogTitle("Choose Email Directory");
			jc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = jc.showSaveDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION)
				file = jc.getSelectedFile();
		}else {
			JOptionPane.showMessageDialog(null,"You cannot run this software until a directory is set.  Try again when you want to set one!",
					"Spam Detector",JOptionPane.WARNING_MESSAGE);
		}
	}
	public void addSpam() throws IOException {
		int i = JOptionPane.showConfirmDialog(null, "<html><body style = 'width: 250px'>We would like to take some data from the emails to improve your spam filter, would you like to allow this?" +
				" Warning: Double check to make sure the emails in the spam folder are all spam before running this!</html>",
				"Spam Detector",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
		if(i==0) {
			SpamDetection sd = new SpamDetection();
			ArrayList<Data> data = new ArrayList<Data>();
			//try {
				//data = sd.readTxt();
			//} catch(IOException e) {e.printStackTrace();System.exit(0);}

			File file = new File("pref.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String path = br.readLine();
			br.close();
			//Add SPAM
			//data = addData(path+"\\SPAM",data);
			//Add NOT_SPAM
			//data = addData(path+"\\NOT_SPAM",data);
		}else {
			JOptionPane.showMessageDialog(null, "Thank you for using this Spam Detector!","Spam Detector ",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	private ArrayList<Data> addData(String path, ArrayList<Data> data) {
		for(int i = 0; i<data.size();i++) {
			
		}
		return data;
	}
	public String getPath() {
		findDir();
		return file.getPath();
	}
}