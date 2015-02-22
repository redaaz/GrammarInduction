/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.FrequentPattern;
import datastructure.Rule;
import datastructure.Sentence;
import heuristic.LongestMostFrequent;
import heuristic.MostCohesiveLongest;
import heuristic.MostFrequentLongest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import spm.spam.AlgoCMSPAM;

/**
 *
 * @author reda
 */
public class Inducer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        //Read the input
        String folderPath="/Users/reda/Documents/NewAlgoTests/";
        String filename="Test10K_1";
        List<String> inin=GI.read(folderPath+filename);
        List<Sentence> corpus= GI.buildSentencesCorpus(inin);
        System.out.println("corpus size:"+corpus.size());
        //Initials
        List<Rule> output=new ArrayList<>();
        boolean stop=false;
        int loopCounter=0;
        GI gi=new GI(new AlgoCMSPAM(),0.002,0.3);
        
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
//            MostFrequentLongest lmf=new MostFrequentLongest();
//            FrequentPattern bestFI1=lmf.chooseFrequentPattern(result);
//            if(bestFI1==null){
//                stop=true;
//                continue;
//            }
//            bestFI1.println();
            
            MostCohesiveLongest js=new MostCohesiveLongest(gi.algo.verticalDB);
            FrequentPattern bestFI2=js.chooseFrequentPattern(result);
            if(bestFI2==null){
                stop=true;
                continue;
            }
            bestFI2.println();
            //(3) make rules
            //--------------
            
            List<Rule> newRules=Rule.makeRules(corpus, bestFI2,gi.minSup2);
            if(!newRules.isEmpty()) {
                System.out.println("-- New Rules -----");
                newRules.stream().forEach(aa1->aa1.println());
            }
            output.addAll(newRules);

            if(newRules.isEmpty()){
                stop=true;
                continue;
            }
            
            //(4) update the corpus
            //---------------------
            corpus=gi.updateTheCorpus(corpus, newRules);
            //System.out.println("-- The Corpus -----");
            //corpus.stream().forEach(qq-> qq.println());
            
        }
        System.out.println("-- The Corpus -----");
        System.out.println("corpus size:"+corpus.size());
        //corpus.stream().forEach(qq-> qq.println());
        
        System.out.println("Start time (ms): "+gi.algo.startTime);
        System.out.println("End time (ms): "+gi.algo.endTime);
        
        GI.writeRules(output,folderPath, filename +"_rules");
    }
    
}