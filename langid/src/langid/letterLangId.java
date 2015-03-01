package langid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 *
 * @author Jiarui Xu
 */
public class letterLangId {
    private static final HashMap enDict = new HashMap();
    private static final HashMap frDict = new HashMap();
    private static final HashMap itDict = new HashMap();
    
    private static final String en = "LangId.train.English";
    private static final String fr = "LangId.train.French";
    private static final String it = "LangId.train.Italian";
        
    public static void dictInit() throws FileNotFoundException, UnsupportedEncodingException, IOException {

        enrichLibDict();
        
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(en), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    splitLetters(line, "en");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fr), "UTF-8"))) {
                String line = br.readLine();
                while (line != null) {
                    splitLetters(line, "fr");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(it), "UTF-8"))) {
                String line = br.readLine();
                while (line != null) {
                    splitLetters(line, "it");
                    line = br.readLine();
                }
            }
    }

    public static void testLang() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String textFile = "LangId.test";
        int count=0;
        try (
            PrintWriter writer = new PrintWriter("letterLangId.out", "UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    count++;
                    String result=testLetters(line);
                    writer.println(count+ " " +result);
                    line = br.readLine();
                }
            writer.close();
        }
    }
    
    private static void enrichLibDict() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(en), "UTF-8"))){
                String line = br.readLine();
                while (line != null) {
                    splitLetters(line, "all");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fr), "UTF-8"))) {
                String line = br.readLine();
                while (line != null) {
                    splitLetters(line, "all");
                    line = br.readLine();
                }
            }
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(it), "UTF-8"))) {
                String line = br.readLine();
                while (line != null) {
                    splitLetters(line, "all");
                    line = br.readLine();
                }
            }
    }
    
    private static void splitLetters(String word, String op) {
        switch (op) {
            case "all":
                for (int i = 0; i < word.length(); i++) {                    
                    String par = Character.toString(word.charAt(i));
                    String chi;
                    if(i==word.length()-1)
                        chi="|THIS IS ENDING|";
                    else
                        chi=Character.toString(word.charAt(i + 1));
                    
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
                
                }
                break;
                
            case "en":
                for (int i = 0; i < word.length(); i++) {                    
                    String par = Character.toString(word.charAt(i));
                    String chi;
                    if(i==word.length()-1)
                        chi="|THIS IS ENDING|";
                    else
                        chi=Character.toString(word.charAt(i + 1));
                    
                    bidict newdict;
                    newdict = (bidict)enDict.get(par);
                    newdict.add(chi);
                    enDict.put(par, newdict);  
                }   
                break;
            case "fr":
                for (int i = 0; i < word.length(); i++) {                    
                    String par = Character.toString(word.charAt(i));
                    String chi;
                    if(i==word.length()-1)
                        chi="|THIS IS ENDING|";
                    else
                        chi=Character.toString(word.charAt(i + 1));
                    
                    bidict newdict;
                    newdict = (bidict)frDict.get(par);
                    newdict.add(chi);
                    frDict.put(par, newdict);  
                }   
                break;
            case "it":
                for (int i = 0; i < word.length(); i++) {                    
                    String par = Character.toString(word.charAt(i));
                    String chi;
                    if(i==word.length()-1)
                        chi="|THIS IS ENDING|";
                    else
                        chi=Character.toString(word.charAt(i + 1));
                    
                    bidict newdict;
                    newdict = (bidict)itDict.get(par);
                    newdict.add(chi);
                    itDict.put(par, newdict);  
                }   
                break;
        }

    }
      
    private static String testLetters(String line){
        double probEn=1.;
        double probFr=1.;
        double probIt=1.;
        bidict dictLetter;
        
        for(int i=0;i<line.length();i++){
            String par=Character.toString(line.charAt(i));
            String chi;
            if(i==line.length()-1)
                chi="|THIS IS ENDING|";
            else
                chi=Character.toString(line.charAt(i + 1));
            
            
            if(enDict.containsKey(par)){
                dictLetter=(bidict)enDict.get(par);
                double probEnTemp=dictLetter.prob(chi);
                probEn=probEn*probEnTemp;
            }
            
            
            if(frDict.containsKey(par)){
                dictLetter=(bidict)frDict.get(par);
                double probFrTemp=dictLetter.prob(chi);
                probFr=probFr*probFrTemp;
            }
            
            
            if(itDict.containsKey(par)){
                dictLetter=(bidict)itDict.get(par);
                double probItTemp=dictLetter.prob(chi);
                probIt=probIt*probItTemp;
            }
            
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
