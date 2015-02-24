/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.FrequentPattern;
import datastructure.MainRule;
import datastructure.Rule;
import datastructure.RuleType;
import datastructure.Sentence;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import spm.spam.SPMiningAlgorithm;
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
    
    long startExecutionTime;
    long endExecutionTime;
    
    long startReadingTime;
    long endReadingTime;
    
    long startWritingTime;
    long endWritingTime;
    
    long startFreeMemory;
    long endFreeMemory;
    
    int numOfLoops;
    
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
        this.numOfLoops=0;
        this.corpusSizes=new ArrayList<>();
    }
    //@param num: number preprocessing operation
    //@param punc: punctuation preprocessing operation
    public void setTextPreprocessing(PreTextOperation num,PreTextOperation punc){
        this.numbers=num;
        this.punctuation=punc;
    }
    
    public List<FrequentPattern> runAlgorithm(List<Sentence> in){
        return this.algo.runAlgorithm(in, minSup1);
    }
    
    public List<Sentence> replaceWithNewRules(List<Sentence> input,List<Rule> newRules){
        List<MainRule> mains=new ArrayList<>();
        newRules.stream().filter((rr) -> (rr.getRuleType()==RuleType.Main)).forEach((rr) -> {
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
            mr.getReferencesIndexs().stream().forEach(x->{
                input.set(x, se);
            });
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
        Collections.sort(rule.getReferencesIndexs());
        int minIndex=rule.getReferencesIndexs().get(0);
        
        List<Integer> res=rule.getReferencesIndexs().subList(1, rule.getReferencesIndexs().size());
        
        rule.setReferencesIndexs(Arrays.asList(minIndex));
        if(rule.getRuleType()==RuleType.Main)
            ((MainRule)rule).relatedSubRules.stream().forEach(x->x.setReferencesIndexs(Arrays.asList(minIndex)));
        
        return res;
    }
    
    //to update data
    //@param input: the corpus
    //@param newRules: list of new induced rule
    //@param rules: list of all rules to adapt the references
    public List<Sentence> updateData(List<Sentence> input,List<Rule> newRules){
        List<Rule> rules=this.InducedRules;
        //apply the induced rules on the corpus, some redundunt sentence may produce after this step
        //output list size=input list size
        List<Sentence> cor=replaceWithNewRules(input,newRules);
        
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
    
    public List<Sentence> readTheCorpus(String folderPath,String fileName) throws IOException{
        List<String> inin=GI.read(folderPath+fileName);
        this.inputSize=inin.size();
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
    
    public void setNumOfLoops(int x){
        this.numOfLoops=x;
    }
    
    public int getNumOfLoops(){
        return this.numOfLoops;
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
        rep.add("# of Loops: "+this.getNumOfLoops());
        rep.add("");
        rep.add("PERFORMACE INFO");
        rep.add("---------------");
        rep.add("Reading Time (ms): "+this.getReadingTimeInMillis());
        rep.add("Execution Time (ms): "+this.getExecutionTimeInMillis());
        rep.add("Writing Time (ms): "+this.getWritingTimeInMillis());
        rep.add("Used Memory (MB): "+this.getUsedMemoryInMB());
        rep.add("");
        rep.add("DATA INFO");
        rep.add("----------");
        rep.add("Initial Corpus Size: "+this.inputSize);
        rep.add("Final Corpus Size: "+this.outputSize);
        double compressionRatio=((this.inputSize-this.outputSize)*100)/(double)this.inputSize;
        rep.add("Compression Ratio: "+ Math.round(compressionRatio*100.0)/100.0+"%");
        rep.add("Induced Rules: "+this.InducedRules.size());
        rep.add("Average Input Length (wps): "+this.totalWordsInInput/(double)this.inputSize);
        rep.add("# of Unique Input Words: "+WordsDictionary.getNumOfUniqueWords());
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
        write(rep,folderPath,"("+inputFileName+") "+date.toString());
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
    
    public void startExecution(){
        this.startExecutionTime=System.currentTimeMillis();
    }
    
    public void endExecution(){
        this.endExecutionTime=System.currentTimeMillis();
    }
    
    public long getExecutionTimeInMillis(){
        return this.endExecutionTime-this.startExecutionTime;
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
    
    public void startPointForFreeMemory(){
        this.startFreeMemory= Runtime.getRuntime().maxMemory();
    }
    
    public void endPointForFreeMemory(){
        this.endFreeMemory= Runtime.getRuntime().maxMemory();
    }
    
    public long getUsedMemoryInMB(){
        return Math.abs((this.endFreeMemory-this.startFreeMemory)/1000000);
    }
    
}
