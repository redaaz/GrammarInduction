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
public class MostFrequentLongest  implements Heuristic{

    @Override
    public FrequentPattern chooseFrequentPattern(List<FrequentPattern> input) {
        if(input.isEmpty())
            return null;
        if(input.size()==1)
            return input.get(0);
        
        Collections.sort(input,MostFrequentLongestComparator);
        //this code to prevent one-word frequent lists
        if(input.get(input.size()-1).getPattern().size()==1)
            return null;
        return input.get(input.size()-1);
    }
    
    public static Comparator<FrequentPattern> MostFrequentLongestComparator 
                          = (FrequentPattern fp1, FrequentPattern fp2) -> {
                              //always 1-word pattern is the smallest 
                              if(fp1.getPattern().size()==1 && fp2.getPattern().size()>1)
                                  return -1;
                              if(fp1.getPattern().size()>1 && fp2.getPattern().size()==1)
                                  return 1;
                              //if fp1 & fp2==1 || if fp1 & fp2 !=1
                              if(fp1.getSup()<fp2.getSup())
                                  return -1;
                              if(fp1.getSup()>fp2.getSup())
                                  return 1;
                              //if this.sup==o.sup
                              
                              if(fp1.getPattern().size()<fp2.getPattern().size())
                                  return -1;
                              if(fp1.getPattern().size()>fp2.getPattern().size())
                                  return 1;
                              //if this.pattern.size()==o.pattern.size()
                              
                              return 0;
    };

    @Override
    public String getHeuristicName() {
        return "MostFrequentLongest";
    }

    
    
}

