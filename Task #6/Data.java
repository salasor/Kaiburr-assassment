//Code authored by Shiva

public class Data implements Comparable<Data>{
    String w;
    int spam_T;
    int not_T;
   
    public Data(String w, int spam_T, int not_T){
      this.w = w;
      this.spam_T = spamTotal;
      this.not_T = notTotal;
    } 
     
    public int compareTo(Data da){
      if (this.spam_T < da.spam_T)
        return 1;
      else if (this.spam_T > da.spam_T)
        return -1;
      return 0;
    }
    
    public String toString() {
        return w + "," + spam_T + "," + not_T;
    }
  }
 