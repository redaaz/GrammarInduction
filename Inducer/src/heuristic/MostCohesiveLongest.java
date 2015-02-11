/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heuristic;

import datastructure.FrequentPattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.Bitmap;

/**
 *
 * @author reda
 */
public class MostCohesiveLongest  implements Heuristics {

    Map<Integer,Bitmap> VDB;
    FrequentPattern bestfp;
    double maxSim;
    
    public MostCohesiveLongest(Map<Integer,Bitmap> vdb){
        this.VDB=vdb;
        FrequentPattern bestfp=null;
        maxSim=Double.MIN_VALUE;
    }
    
    @Override
    public FrequentPattern chooseFrequentPattern(List<FrequentPattern> input) {
        
        
        Collections.sort(input, MostCohesiveLongestComparator);
        this.bestfp=input.get(input.size()-1);
        this.maxSim=bestfp.getCohesion();
        return bestfp;
    }
    
    public Comparator<FrequentPattern> MostCohesiveLongestComparator 
                      = (FrequentPattern fp1, FrequentPattern fp2) -> {
                          if(fp1.getCohesion() < fp2.getCohesion())
                              return -1;
                          if(fp1.getCohesion() > fp2.getCohesion())
                              return 1;
                          //if(calcCohesion(fp1) == calcCohesion(fp2))
                          if(fp1.getPattern().size()<fp2.getPattern().size())
                              return -1;
                          if(fp1.getPattern().size()>fp2.getPattern().size())
                              return 1;
                          //if(fp1.getPattern().size()==fp2.getPattern().size())
                          return 0;
    };
    
    // intersection(11)/union (10||01||11)
    double calcCohesion(FrequentPattern fp){
        if(fp.getPattern().size()<=1)
            return 0; //to be discussed
        if(fp.getPattern().size()==2){
            int value1=fp.getSup();
            
            List<Integer> l1= ((Bitmap)this.VDB.get(fp.getPattern().get(0))).inputReferences.stream().map(X -> X.inputSentenceID).collect(Collectors.toList());
            List<Integer> l2= ((Bitmap)this.VDB.get(fp.getPattern().get(1))).inputReferences.stream().map(X -> X.inputSentenceID).collect(Collectors.toList());
            
            int value2=union(l1,l2).size();
            //print
                fp.println();
                System.out.println(value1/(double)value2);
            return value1/(double)value2;
        }
        int value1=fp.getSup();
        
        List<Integer> Uni=new ArrayList<>();
        for(Integer pattern: fp.getPattern()){
            Uni=union(Uni, ((Bitmap)this.VDB.get(pattern)).inputReferences.stream().map(X -> X.inputSentenceID).collect(Collectors.toList()));
        }
        
        int value2=Uni.size();
        //print
            fp.println();
            System.out.println(value1/(double)value2);
        return value1/(double)value2;
    }
    
    <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new HashSet<>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<>(set);
    }
    
    public double getMaxSim(){
        return this.maxSim;
    }
    
}