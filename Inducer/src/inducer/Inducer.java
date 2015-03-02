/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import com.carrotsearch.sizeof.RamUsageEstimator;
import datastructure.FrequentPattern;
import datastructure.Rule;
import datastructure.Sentence;
import heuristic.MostCohesiveLongest;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import spm.spam.AlgoCMSPAM;
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
    public static void main(String[] args) throws IOException {
        /* Total memory currently in use by the JVM */
        System.out.println("Total memory (bytes): " + 
        Runtime.getRuntime().totalMemory()/(1024*1024));
        Integer u=0;
        System.out.println(u.getClass().toString());
        
        
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
        GI gi=new GI(new AlgoCMSPAM(),new MostCohesiveLongest(),0.005,0.2);
        gi.setTextPreprocessing(null, PreTextOperation.RemovePunctuations);
        List<Long> times=new ArrayList<>();
        MemoryLogger mem=new MemoryLogger();
        
        //Read the input
        String folderPath="/Users/reda/Documents/NewAlgoTests/";
        String fileName="100k";
        
        gi.startReading();
        List<Sentence> corpus= gi.readTheCorpus(folderPath,fileName);
        gi.corpusSizes.add(corpus.size());
        gi.endReading();
        //check memory
        mem.checkMemory();
        
        System.out.println("corpus size:"+corpus.size());
       
        gi.startExecution();
        //the algorithm
        while(!stop){
            //check memory
            mem.checkMemory();
            //System.out.println();
            System.out.println("loop:"+loopCounter++);
            //(1) find frequent patterns
            //--------------------------
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            System.out.println("verticalDB size B:" + RamUsageEstimator.sizeOf(gi.algo.verticalDB)/(1024d*1024d));
            List<FrequentPattern> result= gi.runAlgorithm(corpus);
            /*PERFORMANCE TEST*/times.add(System.currentTimeMillis());
            
            //result.stream().forEach((fp) -> { fp.println(); });
            
            //check memory
            mem.checkMemory();
            
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
            //check memory
            mem.checkMemory();
            
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
        //check memory
        mem.checkMemory();
        
        gi.setNumOfLoops(loopCounter);
        
        gi.endExecution();
        
        //System.out.println("-- The Corpus -----");
        //System.out.println("corpus size:"+corpus.size());
        //corpus.stream().forEach(qq-> qq.println());
        
        gi.startWriting();
        gi.writeRules(folderPath, fileName +"_rules");
        gi.writeTheCorpus(corpus, folderPath, fileName+"_output");
        gi.endWriting();
        
        gi.setUsedMemory(mem.getMaxMemory());
        
        for(int i=0;i<times.size();i=i+8){
            if(i+7<times.size()){
                System.out.println("Algorithm : "+(times.get(i+1)-times.get(i)));
                System.out.println("heuristic : "+(times.get(i+3)-times.get(i+2)));
                System.out.println("makeRules : "+(times.get(i+5)-times.get(i+4)));
                System.out.println("update    : "+(times.get(i+7)-times.get(i+6)));
                System.out.println("-------------------------");
            }
        }
        System.out.println("# of elems "+times.size());
        
        //Report
        gi.writeExperimentReport(folderPath,fileName);          
        
        
        InetAddress ip;
        ip = InetAddress.getLocalHost();
        System.out.println("Current host name : " + ip.getHostName());
        System.out.println("Current IP address : " + ip.getHostAddress());
        String nameOS= System.getProperty("os.name");
        System.out.println("Operating system Name=>"+ nameOS);
        String osType= System.getProperty("os.arch");
        System.out.println("Operating system type =>"+ osType);
        String osVersion= System.getProperty("os.version");
        System.out.println("Operating system version =>"+ osVersion);
         
        System.out.println(System.getenv("PROCESSOR_IDENTIFIER"));
        System.out.println(System.getenv("PROCESSOR_ARCHITECTURE"));
        System.out.println(System.getenv("PROCESSOR_ARCHITEW6432"));
        System.out.println(System.getenv("NUMBER_OF_PROCESSORS"));
        
         /* Total number of processors or cores available to the JVM */
    System.out.println("Available processors (cores): " + 
        Runtime.getRuntime().availableProcessors());
 
    /* Total amount of free memory available to the JVM */
    System.out.println("Free memory (bytes): " + 
        Runtime.getRuntime().freeMemory()/(1024*1024));
 
    /* This will return Long.MAX_VALUE if there is no preset limit */
    long maxMemory = Runtime.getRuntime().maxMemory();
    /* Maximum amount of memory the JVM will attempt to use */
    System.out.println("Maximum memory (bytes): " + 
        (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory/(1024*1024)));
 
    /* Total memory currently in use by the JVM */
    System.out.println("Total memory (bytes): " + 
        Runtime.getRuntime().totalMemory()/(1024*1024));
    }
}