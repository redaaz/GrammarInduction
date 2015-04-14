/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spm.spam;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import datastructure.FrequentPattern;
import datastructure.Repetition;
import datastructure.Sentence;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reda
 */
public class SPMiningAlgorithm {
    // for statistics
    public long startTime;
    public long endTime;
    public int patternCount;
    
    // Vertical database
    public Map<Integer, Bitmap> verticalDB = new HashMap<>();
    //public IntObjectOpenHashMap<Bitmap> verticalDB=new IntObjectOpenHashMap();
    public List<FrequentPattern> runAlgorithm2(List<String> input, double minsupRel){
        return null;
    }
    
    public List<FrequentPattern> runAlgorithm(List<Sentence> input, double minsupRel){
        return null;
    }
    
    public String getAlgoName(){
        return "No name has inserted";
    }
    
    public List<FrequentPattern> stringArrayToFrequentPatternList_CMSPAM(List<String> input,List<Sentence> corpus){
        List<FrequentPattern> res=new ArrayList<>();
        for(String str:input){
            ///*PERFORMANCE TEST*/long third1=System.currentTimeMillis();
            FrequentPattern x=new FrequentPattern(str);
            ///*PERFORMANCE TEST*/long third2=System.currentTimeMillis();
            ///*PERFORMANCE TEST*/acc1+=third2-third1;
            List<List<Repetition>> ref=new ArrayList<>();
            ///*PERFORMANCE TEST*/long third3=System.currentTimeMillis();
            x.getPattern().stream().forEach((i) -> {
                ref.add(this.verticalDB.get(i).inputReferences);
            });
            ///*PERFORMANCE TEST*/long third4=System.currentTimeMillis();
            ///*PERFORMANCE TEST*/acc2+=third4-third3;
            x.setInputReferences(ref,corpus);
            ///*PERFORMANCE TEST*/long third5=System.currentTimeMillis();
            ///*PERFORMANCE TEST*/acc3+=third5-third4;
            x.setCohesion(verticalDB);
            ///*PERFORMANCE TEST*/long third6=System.currentTimeMillis();
            ///*PERFORMANCE TEST*/acc4+=third6-third5;
            res.add(x);
        }
        return res;
    }
    
    
    
}
