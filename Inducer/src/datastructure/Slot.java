/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reda
 */
public class Slot {
    List<String> Alternatives;
    
    public Slot(){
        this.Alternatives=new ArrayList<>();
    }
    
    public Slot(List<String> in){
        this.Alternatives=in;
    }
    
    public void addAlter(String x){
        this.Alternatives.add(x);
    }
    
    public int getAlternativesCount(){
        return this.Alternatives.size();
    }
    
    public List<String> getAlternatives(){
        return this.Alternatives;
    }
    
    
}
