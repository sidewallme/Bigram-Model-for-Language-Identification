/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package langid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author erichsu
 */
public class testResult {
    
    public static void compare(String file1, String file2, String method) throws UnsupportedEncodingException, FileNotFoundException, IOException{
        
            BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "UTF-8"));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(file2), "UTF-8"));
                
                int count=0;
                String line1 = br1.readLine();
                String line2 = br2.readLine();
                while (line1 != null) {
                    if(!line1.equals(line2)){
                        //System.out.println(line1+ " | "+line2);
                        count++;
                    }
                        
                    line1 = br1.readLine();
                    line2 = br2.readLine();
                }
                int correct=300-count;
                double rate=correct/300.;
            System.out.println(method+ " Misclassified: "+count+"; Correctly classified: "+correct+ "; Correction Rate: "+rate);
            
    }
}
