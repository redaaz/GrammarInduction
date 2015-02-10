/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heuristic;

import datastructure.FrequentPattern;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author reda
 */
public class LongestMostFrequent implements Heuristics{

    @Override
    public FrequentPattern chooseFrequentPattern(List<FrequentPattern> input) {
        if(input.isEmpty())
            return null;
        if(input.size()==1)
            return input.get(0);
        
        Collections.sort(input,FruitNameComparator);
        
        return input.get(input.size()-1);
    }

   

    public static Comparator<FrequentPattern> FruitNameComparator 
                          = new Comparator<FrequentPattern>() {
 
	    public int compare(FrequentPattern fp1, FrequentPattern fp2) {
 
	        // compareTo should return < 0 if this is supposed to be
                // less than other, > 0 if this is supposed to be greater than 
                // other and 0 if they are supposed to be equal

                if(fp1.getPattern().size()<fp2.getPattern().size())
                    return -1;
                if(fp1.getPattern().size()>fp2.getPattern().size())
                    return 1;
                //if this.pattern.size()==o.pattern.size()
                if(fp1.getSup()<fp2.getSup())
                    return -1;
                if(fp1.getSup()>fp2.getSup())
                    return 1;
                //if this.sup==o.sup
                return 0;
	    }
 
	};

    
    
}
