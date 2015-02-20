/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.FrequentPattern;
import datastructure.Rule;
import datastructure.Sentence;
import heuristic.MostCohesiveLongest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import spm.spam.AlgoCMSPAM;
import spm.spam.SPMiningAlgorithm;

/**
 *
 * @author reda
 */
public class Inducer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        //Read the input
        String path="/Users/reda/Documents/test.txt";
        List<String> inin=GI.read(path);
        List<Sentence> corpus= GI.buildSentencesCorpus(inin);
        
        //Initials
        List<Rule> output=new ArrayList<>();
        boolean stop=false;
        int loopCounter=0;
        GI gi=new GI(new AlgoCMSPAM(),0.4,0.5);
        
        //the algorithm
        while(!stop){
            System.out.println();
            System.out.println("loop:"+loopCounter++);
            //(1) find frequent patterns
            //--------------------------
            List<FrequentPattern> result= gi.runAlgorithm(corpus);
            //result.stream().forEach((fp) -> { fp.println(); });
            
            //(2) find best frequent pattern
            //------------------------------
            //LongestMostFrequent lmf=new LongestMostFrequent();
            //FrequentPattern bsetFI1=lmf.chooseFrequentPattern(result);

            MostCohesiveLongest js=new MostCohesiveLongest(gi.algo.verticalDB);
            FrequentPattern bsetFI2=js.chooseFrequentPattern(result);

            //(3) make rules
            //--------------
            
            List<Rule> newRules=Rule.makeRules(corpus, bsetFI2,gi.minSup2);
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
            //System.out.println("-- The Corpus -----");
            //corpus.stream().forEach(qq-> qq.println());
            
        }
        System.out.println("Start time (ms): "+gi.algo.startTime);
        System.out.println("End time (ms): "+gi.algo.endTime);
        
        GI.writeRules(output, "/Users/reda/Documents/", "rules");
    }
    
}
