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
        List<Sentence> corpus;
        Sentence s1=new Sentence("h1 h2 h9 h10 h6 h3 h4 h8");//
        Sentence s2=new Sentence("h2 h3 h5 h1");
        Sentence s3=new Sentence("h1 h3 h5 h2 h7");
        Sentence s4=new Sentence("h4 h1 h2 h3 h9 h10");//
        Sentence s5=new Sentence("h5 h3 h1");
        Sentence s6=new Sentence("h7 h5 h1 h4 h1");
        Sentence s7=new Sentence("h1 h3 h9 h10");//
        corpus = Arrays.asList(s1,s2,s3,s4,s5,s6,s7);
        corpus.stream().forEach(sss->sss.println());
        
        //Initials
        List<Rule> output=new ArrayList<>();
        boolean stop=false;
        int loopCounter=0;
        SPMiningAlgorithm aa=new AlgoCMSPAM();
        GI gi=new GI();
        double minSup1=0.4;
        double minSup2=0.5;
        
        //the algorithm
        while(!stop){
            System.out.println();
            System.out.println("loop:"+loopCounter++);
            //(1) find frequent patterns
            //--------------------------
            List<FrequentPattern> result= aa.runAlgorithm(corpus, minSup1);
            //result.stream().forEach((fp) -> { fp.println(); });
            
            //(2) find best frequent pattern
            //------------------------------
            LongestMostFrequent lmf=new LongestMostFrequent();
            FrequentPattern bsetFI1=lmf.chooseFrequentPattern(result);

            MostCohesiveLongest js=new MostCohesiveLongest(aa.verticalDB);
            FrequentPattern bsetFI2=js.chooseFrequentPattern(result);

            //(3) make rules
            //--------------
            
            List<Rule> newRules=Rule.makeRules(corpus, bsetFI2,minSup2);
            if(!newRules.isEmpty()) {
                System.out.println("-- New Rules -----");
                newRules.stream().forEach(aa1->aa1.println());
            }
            output.addAll(newRules);

            if(newRules.isEmpty())
                stop=true;
            
            //(4) update the corpus
            //---------------------
            corpus=gi.updateTheCorpus(corpus, newRules);
            System.out.println("-- The Corpus -----");
            corpus.stream().forEach(qq-> qq.println());
            
        }
        
    }
    
}
