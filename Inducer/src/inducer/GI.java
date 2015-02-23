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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.SPMiningAlgorithm;
import text.Preprocessing;

/**
 *
 * @author reda
 */
public class GI {
    
    SPMiningAlgorithm algo;
    
    double minSup1;
    double minSup2;
    
    public GI(SPMiningAlgorithm alg,double min1,double min2){
        this.algo=alg;
        this.minSup1=min1;
        this.minSup2=min2;
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
    public List<Sentence> updateData(List<Sentence> input,List<Rule> newRules,List<Rule> rules){
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
    
    public static List<Sentence> readTheCorpus(String folderPath,String fileName) throws IOException{
        List<String> inin=GI.read(folderPath+fileName);
        return GI.buildSentencesCorpus(inin);
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

    public static void writeTheCorpus(List<Sentence> input,String folderPath, String fileName) throws IOException{
        List<String> in=input.stream().map(x-> x.toString()).collect(Collectors.toList());
        write(in,folderPath,fileName);
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
    
    private static List<Sentence> buildSentencesCorpus(List<String> input){
        List<Sentence> res=new ArrayList<>();
        Preprocessing.initialization();
        
        input.stream().forEach(x->{
            String str=Preprocessing.toLowerCase(x);
            //str=Preprocessing.replaceNumbers(str);
            str=Preprocessing.removePunctuations(str);
            str=Preprocessing.removeLongPrePostWhiteSpaces(str);
            res.add(new Sentence(str));
        });
        
        return res;
    }
    
    public static void writeRules(List<Rule> rules,String outputPath, String filename) throws IOException{
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
}
