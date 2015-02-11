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
    List<Alternative> alternatives;
    
    public Slot(){
        this.alternatives=new ArrayList<>();
    }
    
    public Slot(List<Alternative> in){
        this.alternatives=in;
    }
    
    public void addAlter(Alternative x){
        this.alternatives.add(x);
    }
    
    public int getAlternativesCount(){
        return this.alternatives.size();
    }
    
    public List<Alternative> getAlternatives(){
        return this.alternatives;
    }
    
    public List<Integer> getReferenceIndexs(){
        List<Integer> res=new ArrayList<>();
        this.alternatives.stream().forEach(x->res.add(x.referenceIndex));
        return res;
    }
    
    public void println(){
        this.alternatives.stream().forEach(x->System.out.println(x.toString()));
    }
    
    public void printlnCode(){
        this.alternatives.stream().forEach(x->System.out.println(x.toStringCode()));
    }
    
    public boolean isEmpty(){
        return this.alternatives.isEmpty();
    }
}
