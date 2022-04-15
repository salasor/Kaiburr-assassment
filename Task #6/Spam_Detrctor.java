//Code authored by Shiva
import java.io.*;
import java.util.*;
import org.apache.commons.io.*;

/*REPORT:
 * The overall accuracy of this spam filter is: 83% correct
 * The percent of false negatives: 11% //Meaning I let spam get into the mailbox about 13% of the time
 * The percent of false positives: 6% //Meaning I put nonspam emails into the spam box about 6% of the time
 * TODO:
 *  It would be easy to adapt the code to learn from previous data.  Here's what I mean:
 *  	1) The user goes to his/her email and looks at the the files and reorganizes it (as a normal person would get spam out of the spam box, and put spam into it)
 *	2) Update the database to have more accurate representation of what words are spam and which aren't as this happens
 *
 */

public class Spam_Detector {
	
	static ArrayList<String> seen = new ArrayList<String>();
	public static void main(String args[]) throws IOException {
		ArrayList<Data> data = readTxt();
		
		File check = new File("pref.txt");
		GUI gui = new GUI();
		String filePath = "";
		if(check.exists()){
			BufferedReader x = new BufferedReader(new FileReader("pref.txt"));
			filePath = x.readLine();
			x.close();
		}else {
			filePath = gui.getPath();
			PrintWriter pw = new PrintWriter(check);
			pw.write(filePath);
			pw.close();
		}
		
		if(filePath.contains("TRAINING")) {
			runTest(data);
		}else {
			File[] temp = new File(filePath).listFiles();
			ArrayList<File> fileAL = new ArrayList<File>();
			for(File f: temp) {
				String s = f.getName();
				if(s.contains(".eml"))
					fileAL.add(f);
			}
			File[] file = fileAL.toArray(new File[fileAL.size()]);
			String spamPath = filePath+"\\SPAM";
			File spam = new File(spamPath);
			if(!spam.exists()) spam.mkdir();

			String notSpamPath = filePath+"\\NOT_SPAM";
			File notSpam = new File(notSpamPath);
			if(!notSpam.exists()) notSpam.mkdir();	
			
			for(File f: file) {
				try {
					char c = naiveBayes(data,DataCreation.getEmail(f));
					if(c=='0') {
						File dest = new File(spam.getAbsolutePath()+"\\"+f.getName());
						FileUtils.moveFile(f, dest);
					}
					else{
						File dest = new File(notSpam.getAbsolutePath()+"\\"+f.getName());
						FileUtils.moveFile(f, dest);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			gui.addSpam();
		}
	}
	private static char naiveBayes(ArrayList<Data> data, String s) {
		if(s=="") return '0';
		String[] words = s.split(" ");
		double spam = 1; double notSpam = 1;
		int count = 0; int notSeenC = 0;
		for(int i = 0; i < words.length; i++){
			String w = words[i];
			if(w.compareTo(" ")!=0) {
				if(w.contains("sex"))
					return '0';
				int index = seen.indexOf(w);
				if(index != -1){
					Data d = data.get(index);
					int total = d.spamTotal+d.notTotal;
					count++;
					spam += ((double)(d.spamTotal+1))/(double)total;
					notSpam += ((double)(d.notTotal+1))/(double)total;
				}
				else {
					String[] temp = {"the","be","to","of","and","a","an","in","\\.bdiv","that","have","it","with"
							,"not","as","do","this","by","of"};		
					ArrayList<String> exclude = new ArrayList<String>(Arrays.asList(temp));
					if(!exclude.contains("^"+w+"$"))
						notSeenC++;
				}
			}
		}
		if((double)notSeenC > (double)count*1.75)
			return '0';
		double max = Math.max(spam, notSpam);
		if(notSpam==spam)
			return '0';
		else if (max == notSpam)
			return '1';
		else
			return '0';
	}
	private static void runTest(ArrayList<Data> data) throws IOException{
		ArrayList<Character> labels = readLabels();
		int correct = 0; int falsePos = 0; int falseNeg = 0;
		double lSize = labels.size();
		double size = lSize*3/4;
		for(int i = (int)size; i < labels.size(); i++){
			String path = "C:\\Users\\Shiva\\eclipse-workspace\\Spam_Detector\\src\\CSDMC2022_SPAM\\TRAINING\\";
			path = path.concat("TRAIN_"+String.format("%05d",i)+".eml");
			try{
				String s = DataCreation.getEmail(new File(path));
				char c = naiveBayes(data, s);
				if(c==labels.get(i))
					correct++;
				else {
					if(c == '0') falseNeg++;
					else if(c=='1') falsePos++;
				}				
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		System.out.printf("The total amount correctly assigned was %2.2f\n",(double)correct/(lSize-size)*100);
		System.out.printf("The total percent of false negatives were %2.2f\n",(double)falseNeg/(lSize-size)*100);
		System.out.printf("The total percent of false positives were %2.2f\n",(double)falsePos/(lSize-size)*100);
	}
	private static ArrayList<Character> readLabels() throws IOException{
		String path = "C:\\Users\\Shiva\\eclipse-workspace\\Spam_Detector\\src\\CSDMC2022_SPAM\\SPAM_T.txt";
		String line = "";
		  
		BufferedReader br = new BufferedReader(new FileReader(path));
	   
		ArrayList<Character> arr = new ArrayList<Character>();

		while((line = br.readLine())!=null)
			arr.add(line.charAt(0));
		
		br.close();
		return arr;
	}
	public static ArrayList<Data> readTxt() throws IOException {
		String path = "C:\\Users\\Shiva\\eclipse-workspace\\Spam_Detector\\spam.txt";
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader(path));

		ArrayList<Data> arr = new ArrayList<Data>();
		while((line = br.readLine())!=null) {
			String[] s = line.split(",");
			seen.add(s[0]);
			arr.add(new Data(s[0],Integer.parseInt(s[1]),Integer.parseInt(s[2])));
		}
		br.close();
		return arr;
	}
}