/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package heuristic;

import datastructure.FrequentPattern;
import java.util.List;

/**
 *
 * @author reda
 */
public interface Heuristics {
       
    public FrequentPattern chooseFrequentPattern(List<FrequentPattern> input);
    
}
