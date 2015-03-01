package langid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Jiarui Xu
 */
public class wordLangId{
    //Dictionaries
    private static final HashMap enDict = new HashMap();
    private static final HashMap frDict = new HashMap();
    private static final HashMap itDict = new HashMap();
    
    /*
    En/Fr/It Words HashMaps to calculate the total number of words in that  langugae
    allWords HashMap it used to calculate the total number of words in all languages
    */
    private static final HashMap enWords = new HashMap();
    private static final HashMap itWords = new HashMap();
    private static final HashMap frWords = new HashMap();
    private static final HashMap allWords = new HashMap();
    
    //filenames under root
    private static final String en = "LangId.train.English";
    private static final String fr = "LangId.train.French";
    private static final String it = "LangId.train.Italian";
        
    public static void dictInit() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        

        enrichLibDict();
        
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(en), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    splitWords(line, "en");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fr), "UTF-8"))) {
                String line = br.readLine();
                while (line != null) {
                    splitWords(line, "fr");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(it), "UTF-8"))) {
                String line = br.readLine();
                while (line != null) {
                    splitWords(line, "it");
                    line = br.readLine();
                }
            }
    }

    public static void testLang() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String textFile = "LangId.test";
        int count=0;
        try (
            PrintWriter writer = new PrintWriter("wordLangId.out", "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    count++;
                    String result=testWords(line);
                    writer.println(count+ " " +result);
                    line = br.readLine();
                }
            writer.close();
        }
            
    }
    
    private static void enrichLibDict() throws UnsupportedEncodingException, FileNotFoundException, IOException{
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(en), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    splitWords(line, "all");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fr), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    splitWords(line, "all");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(it), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    splitWords(line, "all");
                    line = br.readLine();
                }
            }
    }

    private static void splitWords(String line, String op) {
        String delims = " ";
        int count = 0;
        List<String> wordArray=new ArrayList<>();
        
        StringTokenizer st = new StringTokenizer(line, delims);
        String temp;
        
        while (st.hasMoreElements()) {
            temp = (st.nextElement()).toString();
            wordArray.add(temp);
        }
        dictWords((ArrayList<String>) wordArray, op);
    }
    
    private static void dictWords(ArrayList<String> warray,String op){
        for(int i=0;i<warray.size();i++){
            String par=warray.get(i);
            
            String chi;
            if(i==warray.size()-1)
                chi="|THIS IS ENDING|";
            else
                chi=warray.get(i+1);
            hashWords(par, chi,op);
        }
    }

    private static void hashWords(String par, String chi, String op){
        switch (op) {
            case "all":
                allWords.put(par,1);
                if (enDict.containsKey(par)) {
                        bidict newdict;
                        newdict = (bidict)enDict.get(par);
                        newdict.addLibOne(chi);
                        enDict.put(par, newdict);
                    } else {
                        enDict.put(par, new bidict(chi, "lib"));
                    }
                if (frDict.containsKey(par)) {
                        bidict newdict;
                        newdict = (bidict)frDict.get(par);
                        newdict.addLibOne(chi);
                        frDict.put(par, newdict);
                    } else {
                        frDict.put(par, new bidict(chi, "lib"));
                    }
                if (itDict.containsKey(par)) {
                        bidict newdict;
                        newdict = (bidict)itDict.get(par);
                        newdict.addLibOne(chi);
                        itDict.put(par, newdict);
                    } else {
                        itDict.put(par, new bidict(chi, "lib"));
                    }
                break;
                
            case "en":
                    enWords.put(par,1);
                    bidict newdict1;
                    newdict1 = (bidict)enDict.get(par);
                    newdict1.add(chi);
                    enDict.put(par, newdict1);    
                break;
            case "fr":
                    frWords.put(par,1);
                    bidict newdict2;
                    newdict2 = (bidict)frDict.get(par);
                    newdict2.add(chi);
                    frDict.put(par, newdict2);
                break;
            case "it":
                    itWords.put(par,1);
                    bidict newdict3;
                    newdict3 = (bidict)itDict.get(par);
                    newdict3.add(chi);
                    itDict.put(par, newdict3);
                break;     
        }
                
    }

    private static String testWords(String line){
        String delims = " ";
        List<String> wordArray=new ArrayList<>();
        
        StringTokenizer st = new StringTokenizer(line, delims);
        String temp;
        
        while (st.hasMoreElements()) {
            temp = (st.nextElement()).toString();
            wordArray.add(temp);
        }
        return calcuWords((ArrayList<String>) wordArray);
    }
    
    private static String calcuWords(ArrayList<String> wordArray){
        double probEn=1.;
        double probFr=1.;
        double probIt=1.;
        bidict dictWord;
        
        double dictWordSum;
        dictWordSum=allWords.size();
        double notSeenButEn=(double)enWords.size()/(double)dictWordSum;
        double notSeenButFr=(double)frWords.size()/(double)dictWordSum;
        double notSeenButIt=(double)itWords.size()/(double)dictWordSum;
        
        
        for(int i=0;i<wordArray.size();i++){
            String par=wordArray.get(i);
            String chi;
            
            if(i==wordArray.size()-1)
                chi="|THIS IS ENDING|";
            else
                chi=wordArray.get(i+1);
            
            if(enDict.containsKey(par)){
                dictWord=(bidict)enDict.get(par);
                double probEnTemp=dictWord.prob(chi);
                probEn=probEn*probEnTemp;
            }
            else probEn=probEn*notSeenButEn;
            
            if(frDict.containsKey(par)){
                dictWord=(bidict)frDict.get(par);
                double probFrTemp=dictWord.prob(chi);
                probFr=probFr*probFrTemp;
            }
            else probFr=probFr*notSeenButFr;
            
            
            if(itDict.containsKey(par)){
                dictWord=(bidict)itDict.get(par);
                double probItTemp=dictWord.prob(chi);
                probIt=probIt*probItTemp;
            }
            else probIt=probIt*notSeenButIt;
            
        }

        if(probEn>=probIt && probEn>probFr)
            return "English";
        else if (probIt>=probEn && probIt>=probFr){
            return "Italian";
        }
        else 
            return "French";
    }
}
