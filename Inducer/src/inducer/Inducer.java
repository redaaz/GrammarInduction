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
import datastructure.SubRule;
import heuristic.MostCohesiveLongest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import spm.spade.AlgoCMSPADE;
import spm.spam.AlgoCMSPAM;
import spm.spam.AlgoTKS;
import spm.tools.MemoryLogger;
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
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        /* Total memory currently in use by the JVM */
        System.out.println("Total memory (bytes): " + 
        Runtime.getRuntime().totalMemory()/(1024*1024));
        
        /*
        IntArrayList fastset=new IntArrayList();
        List<Integer> set=new ArrayList<>();
        for(int i=0;i<100000;i++){
            fastset.add(i);
            set.add(i);
        }
        long t1=System.currentTimeMillis();
        fastset.retainAll(fastset);
        long t2=System.currentTimeMillis();
        set.retainAll(set);
        long t3=System.currentTimeMillis();
        System.out.println("fast: "+RamUsageEstimator.sizeOf(fastset));
        System.out.println("set: "+RamUsageEstimator.sizeOf(set));
        System.out.println("fast: "+(t2-t1));
        System.out.println("set: "+(t3-t2));
        */
        //Initials
        boolean stop=false;
        int loopCounter=0;
        //AlgoTKS
        //GI gi=new GI(new AlgoCMSPAM(),new MostCohesiveLongest(),0.005,0.2);
        //GI gi=new GI(new AlgoCMSPAM(),new MostCohesiveLongest(),0.5,0.5);
        GI gi=new GI(new AlgoCMSPADE(),new MostCohesiveLongest(),0.005,0.2);
        //0.1 0.03 0.01 0.003 0.001
        //gi.setSencondaryAnalysingParameters(0.05d,2);   
        //gi.setSencondaryAnalysingParameters(0.05d,20);   
        gi.setSencondaryAnalysingParameters(0.005d,20);
        
        gi.setTextPreprocessing(null, PreTextOperation.RemovePunctuations);
        
        MemoryLogger log=new MemoryLogger();
        
        //Read the input
        //String folderPath="/Users/reda/Documents/NewAlgoTests/";
        String fileName;
        String folderPath="/Users/reda/Documents/NewAlgoTests/";
        if(args.length==0){
            fileName="200K";
        }
        else{
            fileName=args[0];
        }
        
        List<Sentence> corpus= gi.readTheCorpus(folderPath,fileName);
        
        //check memory
        log.checkMemory();
        
        System.out.println("corpus size:"+corpus.size());
       
        gi.startBasicExecution();
        //the algorithm
        while(!stop){
            //check memory
            log.checkMemory();
            
            System.out.println("loop:"+loopCounter++);
            //(1) find frequent patterns
            //--------------------------
            /*PERFORMANCE TEST*/log.addTime();
            List<FrequentPattern> result= gi.runAlgorithm(corpus);
            /*PERFORMANCE TEST*/log.addTime();
            
            //result.stream().forEach((fp) -> { fp.println(); });
            
            //check memory
            log.checkMemory();
            
            //(2) find best frequent pattern
            //------------------------------
            /*PERFORMANCE TEST*/log.addTime();
            FrequentPattern bestFI=gi.heuristic.chooseFrequentPattern(result);
            /*PERFORMANCE TEST*/log.addTime();
            if(bestFI==null){
                stop=true;
                continue;
            }
            //bestFI.println();
            //(3) make rules
            //--------------
            /*PERFORMANCE TEST*/log.addTime();
            List<Rule> newRules=Rule.makeRules(corpus, bestFI,gi.algo.getClass().newInstance(),gi.minSup2);
            /*PERFORMANCE TEST*/log.addTime();
            
            //check memory
            log.checkMemory();
            
            if(newRules.isEmpty()){
                stop=true;
                continue;
            }
            
            gi.InducedRules.addAll(newRules);

            //(4) update the corpus
            //---------------------
            /*PERFORMANCE TEST*/log.addTime();
            corpus=gi.updateData(corpus, newRules,RuleType.Main);
            /*PERFORMANCE TEST*/log.addTime();
            gi.corpusSizes.add(corpus.size());
            //System.out.println("-- The Corpus -----");
            //corpus.stream().forEach(qq-> qq.println());
            System.out.println("corpus size= "+corpus.size());
            Runtime.getRuntime().gc();
        }
        //check memory
        log.checkMemory();
        
        gi.setBasicLoopsCount(loopCounter);
        gi.setBasicRulesCount();
        gi.endBasicExecution();
        
        List<Rule> tempRules=new ArrayList<>();
        boolean fail =false;
        
        List<SubRule> toAnalyse=new ArrayList<>(); 
        gi.InducedRules.stream().forEach(x->{
            if(x.getRuleType()==RuleType.Sub || x.getRuleType()==RuleType.SecondarySub)
                toAnalyse.add((SubRule) x);
        });
        
           
            
        gi.startSecondaryExecution(); 
        int SubLoop=0;
        while(!fail){
            System.out.println("toAnalyse size: "+toAnalyse.size());
            tempRules = gi.analyseSubRules(toAnalyse,gi,log);
            gi.InducedRules.addAll(tempRules);
            if(toAnalyse.isEmpty())
                fail=true;
            SubLoop++;
        }
        gi.setSecondaryLoopsCount(SubLoop);
        gi.endSecondaryExecution();
        gi.setUsedMemory(log.getMaxMemory());
        /*
        for(int i=0;i<log.times.size();i=i+8){
            if(i+7<log.times.size()){
                System.out.println("Algorithm : "+(log.times.get(i+1)-log.times.get(i)));
                System.out.println("heuristic : "+(log.times.get(i+3)-log.times.get(i+2)));
                System.out.println("makeRules : "+(log.times.get(i+5)-log.times.get(i+4)));
                System.out.println("update    : "+(log.times.get(i+7)-log.times.get(i+6)));
                System.out.println("-------------------------");
            }
        }
        System.out.println("# of elems "+log.times.size());
        */
        //Report
        gi.writeTheResults(folderPath,fileName,corpus);          
        
    }
}