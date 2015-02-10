/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heuristic;

import datastructure.FrequentPattern;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author reda
 */
public class LongestMostFrequent implements Heuristics {

    @Override
    public FrequentPattern chooseFrequentPattern(List<FrequentPattern> input) {
        if(input.isEmpty())
            return null;
        if(input.size()==1)
            return input.get(0);
        
        Collections.sort(input);
        
        return input.get(input.size()-1);
    }
    
}
