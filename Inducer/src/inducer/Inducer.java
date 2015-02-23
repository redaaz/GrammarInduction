/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.FrequentPattern;
import datastructure.Rule;
import datastructure.RuleType;
import datastructure.Sentence;
import heuristic.LongestMostFrequent;
import heuristic.MostCohesiveLongest;
import heuristic.MostFrequentLongest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.AlgoCMSPAM;
import text.PreTextOperation;

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
        
        //Initials
        boolean stop=false;
        int loopCounter=0;
        GI gi=new GI(new AlgoCMSPAM(),new MostCohesiveLongest(),0.005,0.2);
        gi.setTextPreprocessing(null, PreTextOperation.RemovePunctuations);
        
        //Read the input
        String folderPath="/Users/reda/Documents/NewAlgoTests/";
        String fileName="Test10K_1";
        
        gi.startReading();
        List<Sentence> corpus= gi.readTheCorpus(folderPath,fileName);
        gi.endReading();
        
        System.out.println("corpus size:"+corpus.size());
        
        gi.startPointForFreeMemory();
        gi.startExecution();
        //the algorithm
        while(!stop){
            //System.out.println();
            //System.out.println("loop:"+loopCounter++);
            //(1) find frequent patterns
            //--------------------------
            List<FrequentPattern> result= gi.runAlgorithm(corpus);
            //result.stream().forEach((fp) -> { fp.println(); });
            
            //(2) find best frequent pattern
            //------------------------------
            
            FrequentPattern bestFI=gi.heuristic.chooseFrequentPattern(result);
            if(bestFI==null){
                stop=true;
                continue;
            }
            //bestFI.println();
            //(3) make rules
            //--------------
            
            List<Rule> newRules=Rule.makeRules(corpus, bestFI,gi.minSup2);
            if(!newRules.isEmpty()) {
                //System.out.println("-- New Rules ("+newRules.size()+")-----");
                //newRules.stream().forEach(aa1->aa1.println());
            }
            
            if(newRules.isEmpty()){
                stop=true;
                continue;
            }
            
            gi.InducedRules.addAll(newRules);

            //(4) update the corpus
            //---------------------
            //corpus=gi.replaceWithNewRules(corpus, newRules);
            corpus=gi.updateData(corpus, newRules);
            //System.out.println("-- The Corpus -----");
            //corpus.stream().forEach(qq-> qq.println());
            
        }
        gi.endExecution();
        gi.endPointForFreeMemory();
        //System.out.println("-- The Corpus -----");
        //System.out.println("corpus size:"+corpus.size());
        //corpus.stream().forEach(qq-> qq.println());
        
        gi.startWriting();
        gi.writeRules(folderPath, fileName +"_rules");
        gi.writeTheCorpus(corpus, folderPath, fileName+"_output");
        gi.endWriting();
        //Report
        gi.writeExperimentReport(folderPath,fileName);
    }
    
}