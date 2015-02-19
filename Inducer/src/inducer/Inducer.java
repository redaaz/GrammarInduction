/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.Alternative;
import datastructure.FrequentPattern;
import datastructure.MainRule;
import datastructure.Rule;
import datastructure.Sentence;
import datastructure.SubRule;
import heuristic.LongestMostFrequent;
import heuristic.MostCohesiveLongest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spm.spam.AlgoCMSPAM;
import spm.spam.AlgoSPAM;
import spm.spam.SPMiningAlgorithm;


/**
 *
 * @author reda
 */
public class Inducer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        //Read the input
        List<Sentence> test;
        Sentence s1=new Sentence("h1 h2 h9 h10 h6 h3 h4 h8");//
        Sentence s2=new Sentence("h2 h3 h5 h1");
        Sentence s3=new Sentence("h1 h3 h5 h2 h7");
        Sentence s4=new Sentence("h4 h1 h2 h3 h9 h10");//
        Sentence s5=new Sentence("h5 h3 h1");
        Sentence s6=new Sentence("h7 h5 h1 h4 h1");
        Sentence s7=new Sentence("h1 h3 h9 h10");//
        test = Arrays.asList(s1,s2,s3,s4,s5,s6,s7);
        test.stream().forEach(sss->sss.println());
        
        //Initial the output
        List<Rule> output=new ArrayList<>();
        
        //the algorithm
        
        //(1) find frequent patterns
        //--------------------------
        SPMiningAlgorithm aa=new AlgoCMSPAM();
        List<FrequentPattern> result= aa.runAlgorithm(test, 0.4);
        //result.stream().forEach((fp) -> { fp.println(); });
        
        //(2) find best frequent pattern
        //------------------------------
        LongestMostFrequent lmf=new LongestMostFrequent();
        FrequentPattern bsetFI1=lmf.chooseFrequentPattern(result);
        
        MostCohesiveLongest js=new MostCohesiveLongest(aa.verticalDB);
        FrequentPattern bsetFI2=js.chooseFrequentPattern(result);
        
        //System.out.println("LongestMostFrequent");
        //bsetFI1.println();
        
        //System.out.println("MostCohesiveLongest");
        //bsetFI2.println();
        //System.out.println(js.getMaxSim());
        
        //(3) make rules
        //--------------
        System.out.println("-- New Rules -----");
        List<Rule> rrrr=Rule.makeRules(test, bsetFI2,0.5);
        rrrr.stream().forEach(aa1->aa1.println());
        output.addAll(rrrr);
        
        //(4) update the corpus
        //---------------------
        GI gi=new GI();
        List<Sentence> ee=gi.updateTheCorpus(test, rrrr);
        System.out.println("-- The Corpus -----");
        ee.stream().forEach(qq-> qq.println());
        
        
    }
    
}
