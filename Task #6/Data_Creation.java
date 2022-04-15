//Code authored by Shiva

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Data_Creation {

    private static ArrayList<Data> words = new ArrayList<Data>();

	public static void main(String args[]) throws Exception{	   
		String line = "";
		String labelPath = "C:\\Users\\Shiva\\eclipse-workspace\\Spam_Detector\\src\\CSDMC2022_SPAM\\SPAM_T.txt";
		BufferedReader br = new BufferedReader(new FileReader(labelPath));
		ArrayList<Character> labels = new ArrayList<Character>();

		while((line = br.readLine())!=null)
			labels.add(line.charAt(0));
		br.close();
		
		System.out.println("Start Reading");
		double size = labels.size()*3/4;
		
		for(int i = 0; i < (int)size; i++){
			String path = "C:\\Users\\Shiva\\eclipse-workspace\\Spam_Detector\\src\\CSDMC2022_SPAM\\TRAINING\\";
			path = path.concat("TRAIN_"+String.format("%05d",i)+".eml");
			addWords(new File(path),labels.get(i));
		}		
		System.out.println("Reading Done");		
		System.out.println("Starting Txt Creation");
		
		PrintWriter sp = new PrintWriter("spam.txt");
		StringBuilder spsb = new StringBuilder();

		Collections.sort(words);		
		for(Data d: words)
			spsb.append(d.toString()+"\n");
		
		sp.write(spsb.toString());
		sp.close();
		br.close();
		System.out.println("Done");
	}   
	public static String getEmail(File emlFile) throws Exception {
		Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
		InputStream source = new FileInputStream(emlFile);
		MimeMessage message = new MimeMessage(mailSession, source);
		ReadBody rb = new ReadBody();
		source.close();
		try {
			String s = rb.getText(message).toLowerCase();
			s = s.replaceAll("\n?\r?", "");
			s = s.replaceAll("<.*?>", "");
			s = s.replaceAll("(?m)^[ \t]*\r?\n", "");
			s = s.replaceAll("\\{.*?}", "");
			s = s.replaceAll("&.*?;","");
			s = s.replaceAll("'", "");
			s = s.replaceAll("[^\\p{Alnum}]"," ");
			return s;
		}
		catch(NullPointerException e) {
			return "";
		}
	}
	public static void addWords(File emlFile, Character label) throws Exception{
		String s = getEmail(emlFile);
		String[] temp = {"the","be","to","of","and","a","an","in","\\.bdiv","that","have","it","with"
				,"not","as","do","this","by","of"};		
		ArrayList<String> exclude = new ArrayList<String>(Arrays.asList(temp));
		String[] arr = s.split(" ");
		for(String t: arr){
			String str = t.replaceAll("\\s*","");
			if((str.compareTo("")) != 0){
				if(!exclude.contains(str)) {
					if(label=='1')
						addArr(str, false);
					else 
						addArr(str, true);
				}
			}
		}
	}
	private static void addArr(String str, boolean spam) {
		boolean flag = false;
		int i = 0;
		for(Data d: words){
			if(d.word.compareTo(str)==0) {
				flag = true;
				break;
			}
			else i++;			
		}		
		if(flag){
			Data d = words.get(i);
			if(spam) words.set(i,new Data(str,d.spamTotal+1,d.notTotal));
			else words.set(i,new Data(str,d.spamTotal,d.notTotal+1));				
		}
		else {
			if(spam) words.add(new Data(str,1,0));
			else words.add(new Data(str,0,1));
		}
	}       
}