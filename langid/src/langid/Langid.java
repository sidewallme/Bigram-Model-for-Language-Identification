/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 '/Users/erichsu/Documents/LING406/LangId.train.French'
 */
package langid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static langid.testResult.compare;
/**
 *
 * @author erichsu
 */
public class Langid {

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        //Problem 1
        letterLangId.dictInit();
        letterLangId.testLang();
        compare("LangId.sol","letterLangId.out", "Q1: LetterGram Result:\n");
        //Problem 2
        
        wordLangId.dictInit();
        wordLangId.testLang();
        compare("LangId.sol","wordLangId.out","Q2: WordBiGram (Add-one smoothing) Result:\n");
        
        //Problem 3
        wordLangId2.dictInit();
        wordLangId2.testLang();
        compare("LangId.sol","wordLangId2.out","Q3: WordBiGram (Good-Turing smoothing) Result:\n");
    }

    
}
