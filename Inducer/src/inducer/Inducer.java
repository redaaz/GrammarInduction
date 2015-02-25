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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.omg.SendingContext.RunTime;
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
        List<Long> times=new ArrayList<>();
        
        //Read the input
        String folderPath="/Users/reda/Documents/NewAlgoTests/";
        String fileName="50K";
        
        gi.startReading();
        List<Sentence> corpus= gi.readTheCorpus(folderPath,fileName);
        gi.corpusSizes.add(corpus.size());
        gi.endReading();
        
        System.out.println("corpus size:"+corpus.size());
        System.out.println(Runtime.getRuntime().maxMemory());
        
        gi.startPointForFreeMemory();
        gi.startExecution();
        //the algorithm
        while(!stop){
            
            //System.out.println();
            System.out.println("loop:"+loopCounter++);
            //(1) find frequent patterns
            //--------------------------
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            List<FrequentPattern> result= gi.runAlgorithm(corpus);
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            
            //result.stream().forEach((fp) -> { fp.println(); });
            
            //(2) find best frequent pattern
            //------------------------------
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            FrequentPattern bestFI=gi.heuristic.chooseFrequentPattern(result);
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            if(bestFI==null){
                stop=true;
                continue;
            }
            //bestFI.println();
            //(3) make rules
            //--------------
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            List<Rule> newRules=Rule.makeRules(corpus, bestFI,gi.minSup2);
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
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
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            corpus=gi.updateData(corpus, newRules);
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            gi.corpusSizes.add(corpus.size());
            //System.out.println("-- The Corpus -----");
            //corpus.stream().forEach(qq-> qq.println());
            System.out.println("corpus size= "+corpus.size());
            Runtime.getRuntime().gc();
        }
        gi.setNumOfLoops(loopCounter);
        
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
        
        
        for(int i=0;i<times.size();i=i+8){
            if(i+7<times.size()){
                System.out.println("Algorithm : "+(times.get(i+1)-times.get(i)));
                System.out.println("heuristic : "+(times.get(i+3)-times.get(i+2)));
                System.out.println("makeRules : "+(times.get(i+5)-times.get(i+4)));
                System.out.println("update    : "+(times.get(i+7)-times.get(i+6)));
                System.out.println("-------------------------");
            }
        }
        System.out.println("# of elems"+times.size());
        System.out.println("sum:"+times.stream().collect(Collectors.reducing(Long::sum)));
    }
    
}