/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.FrequentPattern;
import datastructure.Sentence;
import datastructure.WordsDictionary;
import heuristic.MostCohesiveLongest;
import heuristic.LongestMostFrequent;
import java.util.ArrayList;
import java.util.List;
import spm.spam.AlgoCMSPAM;


/**
 *
 * @author reda
 */
public class Inducer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        // TODO code application logic here
        //this code is JUST to test
        List<String> test=new ArrayList<>();
        Sentence s1=new Sentence("h1 h2 h9 h10 h6 h3 h4 h8");
        Sentence s2=new Sentence("h2 h3 h5 h1");
        Sentence s3=new Sentence("h1 h3 h5 h2 h7");
        Sentence s4=new Sentence("h4 h1 h2 h3 h9 h10");
        Sentence s5=new Sentence("h5 h3 h1");
        Sentence s6=new Sentence("h7 h5 h1 h4 h1");
        Sentence s7=new Sentence("h1 h3 h9 h10");
        //System.out.println(s1.toStringCM_SPAM());
        test.add(s1.toStringCM_SPAM());
        test.add(s2.toStringCM_SPAM());
        test.add(s3.toStringCM_SPAM());
        test.add(s4.toStringCM_SPAM());
        test.add(s5.toStringCM_SPAM());
        test.add(s6.toStringCM_SPAM());
        test.add(s7.toStringCM_SPAM());
        
        AlgoCMSPAM aa=new AlgoCMSPAM();
        WordsDictionary gtt=new WordsDictionary();
        
        List<FrequentPattern> result= aa.runAlgorithm(test, 0.4);
        
        for(FrequentPattern fp:result){
            System.out.print("["+fp.getCohesion()+"]"); 
            fp.println();
        }
        
        LongestMostFrequent lmf=new LongestMostFrequent();
        FrequentPattern bsetFI1=lmf.chooseFrequentPattern(result);
        
        MostCohesiveLongest js=new MostCohesiveLongest(aa.verticalDB);
        FrequentPattern bsetFI2=js.chooseFrequentPattern(result);
        
        
        System.out.println("LongestMostFrequent");
        bsetFI1.println();
        
        System.out.println("MostCohesiveLongest");
        bsetFI2.println();
        System.out.println(js.getMaxSim());
        
        int i=9+0;
    }
    
}
