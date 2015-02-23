/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spm.spam;

import datastructure.FrequentPattern;
import datastructure.Sentence;
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
    
    public List<FrequentPattern> runAlgorithm2(List<String> input, double minsupRel){
        return null;
    }
    
    public List<FrequentPattern> runAlgorithm(List<Sentence> input, double minsupRel){
        return null;
    }
    
    public String getAlgoName(){
        return "No name has inserted";
    }
}
