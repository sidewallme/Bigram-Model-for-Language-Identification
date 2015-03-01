package langid;
import java.util.HashMap;

/**
 *
 * @author Jiarui Xu
 */
public class bidict {
    
    HashMap hm=new HashMap();//[String: Frequency]
    HashMap freq=new HashMap();//[freq: Frequency of freq]

    HashMap newpGT=new HashMap();//[String: new Frequency]
    HashMap newpGT_norm=new HashMap();//[String: probability]
    
    double sum;//sum of all post words
    int nonzeroSum=0;//number of distinct words

    //Constructors
    bidict(){
        sum=1.;
    }
    bidict(String pair){
        sum=1.;
        hm.put(pair, 1);
    }
    bidict(String pair, String op){
        sum=1.;
        hm.put(pair, 1);
    }
    
    //add method
    public void add(String newChar){
        sum++;
        int count=0;
        count=(Integer)hm.get(newChar);
        count++;
        nonzeroSum++;
        hm.put(newChar, count);
    }
    
    //Add-one dicts
    public void addLibOne(String newChar){
        if(hm.containsKey(newChar)){   
        }
        else{
            sum++;
            hm.put(newChar, 1);
        }
    }
    public double prob(String theChar){
        if(hm.containsKey(theChar)){
            int count=(Integer)hm.get(theChar);
            double den=count;
            return den/sum;
        }
        else
            return 1.;
    }
    
    //Good Turing dicts
    public void addLibGt(String newChar){
        if(hm.containsKey(newChar)){         
        }
        else{
            hm.put(newChar, 0);
        }
    }
    public double probGTuring(String postWord){
        int newSum=0;
        
        for (Object key : hm.keySet()) {
            //get the frequency of the key
            int val=(int)hm.get(key);
            //newSum+=val;
            
            /*
            put the frequecy into the "freq" hashMap
            which is the frequency table of frequencies
            [0 : 100]
            [1 : 50]
            [2 : 10]
            [10: 1]
            */
            if(freq.containsKey(val)){
                int count=(Integer)freq.get(val);
                count++;     
                freq.put(val, count);
            }
            else{
                freq.put(val, 1);
            }
        }

        //get N1, the frequency of 1 
        //if there is no1 as baseFrequency, then we add 1 until we get the base 
        int freqBase=1;
        int freqBaseCount=0;
        while(freqBaseCount==0){
            if(freq.containsKey(freqBase))
                freqBaseCount=(int)freq.get(freqBase);
            else
                freqBaseCount++;
        }
        //now we have N[base]=freqBaseCount
        
        //Calculate the sum of all newN (for normalization purpose)
        for (Object key : freq.keySet()){
            int val=(int)freq.get(key);
            newSum+=val;
        }
        
        //Construct the new probability lookup table (un-normalized)
        for (Object key : hm.keySet()){
            int val=(int)hm.get(key);//old frequency
            int newval=0;//new frequency
            int oldCount;//old frequency Count
            int nextCount;//N(val+1)
            
            
            if(val!=0){//newN[n]=(n+1)N[n+1]/N[n]
                if(freq.containsKey(val+1)){
                    nextCount=(int)freq.get(val+1);
                    newval=(val+1)*freqBaseCount/nextCount;
                }
                else{
                    oldCount=(int)freq.get(val);
                    newval=(val+1)*freqBaseCount/oldCount;
                }
            }
            else{//N0=N1
                val=0;
                newval=freqBaseCount;
            }
            newpGT.put(key, newval);
            
        }
        
        //normalize the probability table
        for (Object key : newpGT.keySet()){
            int newval=(int)newpGT.get(key);
            double normed=(newval*1.0)/(newSum*1.0);
            newpGT_norm.put(key, normed);
        }
        double zeroCountNorm=(freqBaseCount*1.0)/(newSum*1.0);
        double returnProb=0;//the return Probability
        
        if(newpGT_norm.containsKey(postWord))//if seen
            returnProb=(double) newpGT_norm.get(postWord);
        else 
            returnProb=zeroCountNorm;//if unseen, p_gt[1]
        
        return returnProb;
    }
    
}