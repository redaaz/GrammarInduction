/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.ObjectArrayList;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import datastructure.FrequentPattern;
import datastructure.MainRule;
import datastructure.Rule;
import datastructure.RuleType;
import datastructure.SecondaryMainRule;
import datastructure.SecondarySubRule;
import datastructure.Sentence;
import datastructure.SubRule;
import datastructure.WordsDictionary;
import heuristic.Heuristic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import spm.spam.SPMiningAlgorithm;
import spm.tools.MemoryLogger;
import text.General;
import text.PreTextOperation;
import text.Preprocessing;

/**
 *
 * @author reda
 */
public class GI {
    
    SPMiningAlgorithm algo;
    
    Heuristic heuristic;
    
    PreTextOperation punctuation;
    PreTextOperation numbers;
    
    double minSup1;
    double minSup2;
    double minSup3=0.05d;
    double minimumNumofSentence=10;
    
    long startBasicExecutionTime;
    long endBasicExecutionTime;
    
    long startSecondaryExecutionTime;
    long endSecondaryExecutionTime;
    
    long startReadingTime;
    long endReadingTime;
    
    long startWritingTime;
    long endWritingTime;
    
    double UsedMemory;
    
    int basicRules=-1;
    
    int BasicLoopsCount=-1;
    int SecondaryLoopsCount=-1;
    
    List<Integer> corpusSizes;
    int inputSize;
    int outputSize;
    
    List<Rule> InducedRules;
    double AvgInputLength;
    
    int totalWordsInInput;
    
    public GI(SPMiningAlgorithm alg,Heuristic heu,double min1,double min2){
        this.algo=alg;
        this.heuristic=heu;
        this.minSup1=min1;
        this.minSup2=min2;
        this.inputSize=0;
        this.outputSize=0;
        this.InducedRules=new ArrayList<>();
        this.AvgInputLength=0;
        this.totalWordsInInput=0;
        this.punctuation=null;
        this.numbers=null;
        this.BasicLoopsCount=0;
        this.corpusSizes=new ArrayList<>();
    }
    //@param num: number preprocessing operation
    //@param punc: punctuation preprocessing operation
    public void setTextPreprocessing(PreTextOperation num,PreTextOperation punc){
        this.numbers=num;
        this.punctuation=punc;
    }
    
    public void setBasicRulesCount(){
        this.basicRules=this.InducedRules.size();
    }
    
    public int getBasicRulesCount(){
        return this.basicRules;
    }
    
    public List<FrequentPattern> runAlgorithm(List<Sentence> in){
        return this.algo.runAlgorithm(in, minSup1);
    }
    
    public List<Sentence> replaceWithNewRules(List<Sentence> input,List<Rule> newRules,RuleType RT){
        List<MainRule> mains=new ArrayList<>();
        newRules.stream().filter((rr) -> (rr.getRuleType()==RT)).forEach((rr) -> {
            mains.add((MainRule)rr);
        }); 
//        mains.stream().forEach((mr) -> {
//            Sentence se=mr.toSentence();
//            if (mr.getReferencesIndexs().size()>=1) {
//                input.set(mr.getReferencesIndexs().get(0), se);
//                mr.getReferencesIndexs().subList(1, mr.getReferencesIndexs().size()).stream().forEach((ii) -> {
//                    toDelete.add(input.get(ii));
//                    oldIndexsToNewIndexs.add(ii);
//                });
//            }
//        });
//        return input.stream().filter(x->!toDelete.contains(x)).collect(Collectors.toList());
        
        mains.stream().forEach((mr) -> {
            Sentence se=mr.toSentence();
            for(IntCursor x:  mr.getReferencesIndexs()){
                input.set(x.value, se);
            }
            
        });
        return input; 
    }
    //keep one reference "less value" and 
    //return list of sentences indexs to be deleted
    public List<Integer> castRuleIndexs(Rule rule){
        
        if(rule.getReferencesIndexs().isEmpty())
            return null;
        if(rule.getReferencesIndexs().size()==1)
            return new ArrayList<>();
        int[] temp=rule.getReferencesIndexs().toArray();
        Arrays.sort(temp);
        rule.setReferencesIndexs(General.toIntArrayList(temp));
        
//        IntArrayList temp=rule.ge.getReferencesIndexs()
//        int[] tempArr=IndirectSort.mergesort(0, rule.getReferencesIndexs().size(), new AscendingIntComparator(rule.getReferencesIndexs().toArray()));
//        rule.setReferencesIndexs(IntArrayList.from(tempArr));
        
        
        int minIndex=rule.getReferencesIndexs().get(0);
        
        //List<Integer> res=rule.getReferencesIndexs().subList(1, rule.getReferencesIndexs().size());
        List<Integer> res=General.subList(rule.getReferencesIndexs(),1, rule.getReferencesIndexs().size());
        rule.setReferencesIndexs(IntArrayList.from(minIndex));
        if(rule.getRuleType()==RuleType.Main)
            ((MainRule)rule).relatedSubRules.stream().forEach(x->x.setReferencesIndexs(IntArrayList.from(minIndex)));
        
        return res;
    }
    
    //to update data
    //@param input: the corpus
    //@param newRules: list of new induced rule
    //@param rules: list of all rules to adapt the references
    public List<Sentence> updateData(List<Sentence> input,List<Rule> newRules,RuleType RT){
        List<Rule> rules=this.InducedRules;
        //apply the induced rules on the corpus, some redundunt sentence may produce after this step
        //output list size=input list size
        List<Sentence> cor=replaceWithNewRules(input,newRules,RT);
        
        Set<Integer> toDelete=new HashSet<>();
        
        rules.stream().forEach(x->{
            //update references for all rules regarding to one main rule
            //return: sentence's indexs to be removed from the corpus
            List<Integer> todelete=castRuleIndexs(x);
            toDelete.addAll(todelete);
        });
        List<Integer> toDel=new ArrayList<>( toDelete);
        Collections.sort(toDel);
        
        for(int i=toDel.size()-1;i>=0;i--){
            for(Rule rule:rules){
                for(int j=0;j<rule.getReferencesIndexs().size();j++){
                    if(rule.getReferencesIndexs().get(j)>toDel.get(i)){
                        rule.getReferencesIndexs().set(j, rule.getReferencesIndexs().get(j)-1);
                    }
                }
            }
        }
        
        for(int i= toDel.size()-1 ; i>=0;i--){
            cor.remove((int)toDel.get(i));
        }
        return cor;
    }
    
    //to update data
    //@param input: the corpus
    //@param newRules: list of new induced rule
    //@param rules: list of all rules to adapt the references
    public List<Sentence> updateSubData(List<Rule> subInducedRules,List<Sentence> input,List<Rule> newRules,RuleType RT){
        
        //apply the induced rules on the corpus, some redundunt sentence may produce after this step
        //output list size=input list size
        List<Sentence> cor=replaceWithNewRules(input,newRules,RT);
        
        Set<Integer> toDelete=new HashSet<>();
        
        subInducedRules.stream().forEach(x->{
            //update references for all rules regarding to one main rule
            //return: sentence's indexs to be removed from the corpus
            List<Integer> todelete=castRuleIndexs(x);
            toDelete.addAll(todelete);
        });
        List<Integer> toDel=new ArrayList<>( toDelete);
        Collections.sort(toDel);
        
        for(int i=toDel.size()-1;i>=0;i--){
            for(Rule rule:subInducedRules){
                for(int j=0;j<rule.getReferencesIndexs().size();j++){
                    if(rule.getReferencesIndexs().get(j)>toDel.get(i)){
                        rule.getReferencesIndexs().set(j, rule.getReferencesIndexs().get(j)-1);
                    }
                }
            }
        }
        
        for(int i= toDel.size()-1 ; i>=0;i--){
            cor.remove((int)toDel.get(i));
        }
        return cor;
    }
    
//    public List<Sentence> updateData(SubRule subRule,MainRule mainRule){
//        
//        //To be added
//        
//    }
    
    public List<Sentence> readTheCorpus(String folderPath,String fileName) throws IOException{
        startReading();
        List<String> inin=GI.read(folderPath+fileName);
        this.inputSize=inin.size();
        endReading();
        corpusSizes.add(inin.size());
        return buildSentencesCorpus(inin);
    }
    
    public static void write(List<String> records,String outputPath,String filename) throws IOException {
        int bufSize=(int) Math.pow(1024, 2);
        File file;
        if(outputPath==null){
            file= new File("foo", ".txt");
        }
        else{
            file=new File(outputPath,filename+".txt");
        }
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

            System.out.print("Writing buffered (buffer size: " + bufSize + ")... ");
            write1(records, bufferedWriter);
        } finally {
            // comment this out if you want to inspect the files afterward
            //file.delete();
        }
    }

    public void writeTheCorpus(List<Sentence> input,String folderPath, String fileName) throws IOException{
        List<String> in=input.stream().map(x-> x.toString()).collect(Collectors.toList());
        this.outputSize=input.size();
        write(in,folderPath,fileName);
    }
    
    public void setBasicLoopsCount(int x){
        this.BasicLoopsCount=x;
    }
    
    public int getBasicLoopsCount(){
        return this.BasicLoopsCount;
    }
    
    public void setSecondaryLoopsCount(int x){
        this.SecondaryLoopsCount=x;
    }
    
    public int getSecondaryLoopsCount(){
        return this.SecondaryLoopsCount;
    }
    
    public void writeExperimentReport(String folderPath,String inputFileName) throws IOException{
        List<String> rep=new ArrayList<>();
        Date date=new Date();
        
        rep.add("Experiment Report");
        rep.add("-----------------");
        rep.add("Date: "+ date.toString());
        rep.add("");
        rep.add("ALGORITHM INFO");
        rep.add("--------------");
        rep.add("Algorithm: "+this.algo.getAlgoName());
        rep.add("Heuristic: "+this.heuristic.getHeuristicName());
        rep.add("Primary Minimum Support: "+this.minSup1);
        rep.add("Secondary Minimum Support: "+this.minSup2);
        rep.add("# of Basic Loops: "+this.getBasicLoopsCount());
        rep.add("# of Seco. Loops: "+this.getSecondaryLoopsCount());
        rep.add("");
        rep.add("PERFORMACE INFO");
        rep.add("---------------");
        rep.add("Reading Time (ms): "+this.getReadingTimeInMillis());
        rep.add("Basic Execution Time (ms): "+this.getBasicExecutionTimeInMillis());
        rep.add("Seco. Execution Time (ms): "+this.getSecondaryExecutionTimeInMillis());
        rep.add("Total Execution Time (ms): "+this.getTotalExecutionTimeInMillis());
        rep.add("Writing Time (ms): "+this.getWritingTimeInMillis());
        rep.add("Used Memory (MB): "+this.getUsedMemoryInMB());
        rep.add("");
        rep.add("DATA INFO");
        rep.add("----------");
        rep.add("Initial Corpus Size: "+this.inputSize);
        rep.add("Final Corpus Size: "+this.outputSize);
        double compressionRatio=((this.inputSize-this.outputSize)*100)/(double)this.inputSize;
        rep.add("Compression Ratio: "+ Math.round(compressionRatio*100.0)/100.0+"%");
        rep.add("# of Induced Rules: "+this.InducedRules.size());
        rep.add("Basic Rules: "+this.basicRules);
        rep.add("Secondary Rules: "+(this.InducedRules.size()-this.basicRules));
        rep.add("Average Input Length (wps): "+this.totalWordsInInput/(double)this.inputSize);
        rep.add("# of Unique Input Words: "+WordsDictionary.getNumOfUniqueWords());
        rep.add("Non Productive Sen.: "+this.getNonProductiveInput());
        rep.add("Productive Sen.: "+(this.inputSize- this.getNonProductiveInput()));
        rep.add("Generating Power (sen.): "+this.getGenerativeCount());
        Double ratio=((double)100* this.getGenerativeCount())  /(this.inputSize- this.getNonProductiveInput()) ;
        rep.add("Generating Ratio : "+Math.round(ratio*100.0)/100.0  +"%");
        rep.add("");
        rep.add("TEXT PREPROCESSING INFO");
        rep.add("-----------------------");
        rep.add("Numbers: "+this.numbers);
        rep.add("Punctuations: "+this.punctuation);
        rep.add("");
        rep.add("CORPUS SIZES PER LOOPS");
        rep.add("----------------------");
        for(Integer i: this.corpusSizes){
            rep.add(""+i);
        }
        write(rep,folderPath,inputFileName);
    }
    
    private static void write1(List<String> records, Writer writer) throws IOException {
        long start = System.currentTimeMillis();
        for (String record: records) {
            writer.write(record+"\n");
        }
        writer.flush();
        writer.close();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000f + " seconds");
    }

    private static List<String> read(String filePath) throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(filePath+".txt"));
        String line;
        List<String> in = new ArrayList<>();
        
        while ((line = br.readLine()) != null) {
            in.add(line);
        }
        br.close();
        return in;
    }
    
    private List<Sentence> buildSentencesCorpus(List<String> input) {
        List<Sentence> res=new ArrayList<>();
        try{
            Preprocessing.initialization();
        }catch(NoSuchMethodException e){
            System.out.println(e.toString());
        }
        input.stream().forEach(x->{
            String str=Preprocessing.toLowerCase(x);
            
            if(this.numbers!=null){
                Object[] parameters = new String[1];
                parameters[0] = str;
                try {
                    str=(String)Preprocessing.getOperation.get(this.numbers).invoke(this, parameters);
                } catch (Exception e) {
                   System.out.println(e.toString());
                }
            }
            
            if(this.punctuation!=null){
                Object[] parameters = new String[1];
                parameters[0] = str;
                try {
                    str=(String)Preprocessing.getOperation.get(this.punctuation).invoke(null, parameters);
                } catch (Exception e) {
                   System.out.println(e.toString());
                }
            }
            
            str=Preprocessing.removeLongPrePostWhiteSpaces(str);
            Sentence se=new Sentence(str);
            res.add(se);
            this.totalWordsInInput+=se.getLength();
        });
        
        return res;
    }
    
    public void writeRules(String outputPath, String filename) throws IOException{
        List<Rule> rules=this.InducedRules;
         int bufSize=(int) Math.pow(1024, 2);
        File file;
        if(outputPath==null){
            file= new File("foo", ".txt");
        }
        else{
            file=new File(outputPath,filename+".txt");
        }
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer, bufSize);

            System.out.print("Writing buffered (buffer size: " + bufSize + ")... ");
            List<String> rul=rules.stream().map(x->x.toString()).collect(Collectors.toList());
            write1(rul, bufferedWriter);
        } finally {
            // comment this out if you want to inspect the files afterward
            //file.delete();
        }
    }
    
    public void startBasicExecution(){
        this.startBasicExecutionTime=System.currentTimeMillis();
    }
    
    public void endBasicExecution(){
        this.endBasicExecutionTime=System.currentTimeMillis();
    }
    
    public void startSecondaryExecution(){
        this.startSecondaryExecutionTime=System.currentTimeMillis();
    }
    
    public void endSecondaryExecution(){
        this.endSecondaryExecutionTime=System.currentTimeMillis();
    }
    
    public long getBasicExecutionTimeInMillis(){
        return this.endBasicExecutionTime-this.startBasicExecutionTime;
    }
    
    public long getSecondaryExecutionTimeInMillis(){
        return this.endSecondaryExecutionTime-this.startSecondaryExecutionTime;
    }
           
    public long getTotalExecutionTimeInMillis(){
        return this.getBasicExecutionTimeInMillis()+this.getSecondaryExecutionTimeInMillis();
    }
    
    public void startReading(){
        this.startReadingTime=System.currentTimeMillis();
    }
    
    public void endReading(){
        this.endReadingTime=System.currentTimeMillis();
    }
    
    public long getReadingTimeInMillis(){
        return this.endReadingTime-this.startReadingTime;
    }
    
    public void startWriting(){
        this.startWritingTime=System.currentTimeMillis();
    }
    
    public void endWriting(){
        this.endWritingTime=System.currentTimeMillis();
    }
    
    public long getWritingTimeInMillis(){
        return this.endWritingTime-this.startWritingTime;
    }
    
    public double setUsedMemory(double mem){
        return this.UsedMemory=mem;
    }
    
    public double getUsedMemoryInMB(){
        return this.UsedMemory;
    }
 
    public List<Rule> analyseSubRules(List<SubRule> toAnalyse,GI gi,MemoryLogger log){
        int loop=0;
        boolean stop=false;
        
        if(toAnalyse.isEmpty())
            return new ArrayList<>();
        
        //IntIntOpenHashMap inputIndexToOriginalIndex=new IntIntOpenHashMap();
        
        List<Rule> subInducedRules=new ArrayList<>();
        List<Rule> finalRules=new ArrayList<>();
        double min=Double.MAX_VALUE;
        
        //if(toAnalyse.get(0).getRuleType()==RuleType.Sub){
        SubRule subRule=toAnalyse.remove(0);
        List<Sentence> input=subRule.getAlternatives();
        
        min=(minSup3*input.size())>=minimumNumofSentence?minSup3:(1*minimumNumofSentence)/(double)input.size();
        
        if(minimumNumofSentence>input.size())
            return new ArrayList<>();
        
        while(!stop){
        
            System.out.println("sub loop:"+loop++);
            log.checkMemory();
            
            //(1) find frequent patterns
            //--------------------------
            /*PERFORMANCE TEST*/log.addTime();
            List<FrequentPattern> result= gi.algo.runAlgorithm(input,min);
            /*PERFORMANCE TEST*/log.addTime();
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
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
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            //(3) make rules
            //--------------
            /*PERFORMANCE TEST*/log.addTime();
            List<Rule> newRules=Rule.makeRules(input, bestFI,min);
            /*PERFORMANCE TEST*/log.addTime();
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            /************/System.out.println("newRules : "+newRules.size());
            log.checkMemory();
            
            if(newRules.isEmpty()){
                stop=true;
                continue;
            }
            
            subInducedRules.addAll(newRules);
            
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            
            //(4) update the corpus
            //---------------------
            /*PERFORMANCE TEST*/log.addTime();
            input=gi.updateSubData(subInducedRules,input, newRules,RuleType.Main);
            /*PERFORMANCE TEST*/log.addTime();
            //input.stream().forEach(x->x.println());
            //newSecRules.stream().forEach(x->x.getReferencesIndexs().toString());
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            List<Rule> newSecRules=Rule.convertToSecondaryRules(newRules,subRule);
            //List<Rule> newSecRules=Rule.convertToSecondaryRules(subInducedRules.subList(subInducedRules.size()-newRules.size(), subInducedRules.size()),subRule);
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            finalRules.addAll(newSecRules);
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            newSecRules.stream().filter(x->x.getRuleType()==RuleType.SecondarySub).forEach(x->toAnalyse.add((SecondarySubRule)x));
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            updateInducedRule(input,subRule,finalRules);
            /************/System.out.println("Rule.idCounter: "+Rule.idCounter);
            System.out.println("sub corpus size= "+input.size());
            Runtime.getRuntime().gc();
        }
        return finalRules;
    }
    
    public void updateInducedRule(List<Sentence> input,SubRule source,List<Rule> newSecRules){
        source.updateAlternatives(input,newSecRules);
    }
    
    public void setSencondaryAnalysingParameters(double min3,int minNumOfSentence){
        this.minSup3=min3;
        this.minimumNumofSentence=minNumOfSentence;
    }
    
    public void writeTheResults(String folderPath,String inputFileName,List<Sentence> corpus) throws IOException{
        String timeStamp=(new Date()).toString();
        
        String newFolderPath=folderPath+"/"+"("+inputFileName+") "+timeStamp;
        String ExperimentReportName="("+inputFileName+") "+timeStamp;
        boolean success=(new File(newFolderPath)).mkdirs();
        if(success){
            startWriting();
            writeRules(newFolderPath, inputFileName +"_rules");
            writeTheCorpus(corpus, newFolderPath, inputFileName+"_output");
            endWriting();
            writeExperimentReport(newFolderPath,ExperimentReportName);         
        }
        
    }
    
    public Long getGenerativeCount(){
        Long res=(long) 0;
        
        for(Rule r: this.InducedRules){
            
            if(r.getRuleType()==RuleType.Main){
                res+=((MainRule)r).getGenerativeCount();
            }
        }
        
        return res;
    }
    
    public int getNonProductiveInput(){
        int mains= (int)this.InducedRules.stream().filter(x->x.getRuleType()==RuleType.Main).count();
        return this.outputSize-mains;
    }
    
    
}
