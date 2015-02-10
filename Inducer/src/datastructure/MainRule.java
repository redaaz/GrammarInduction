/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.List;

/**
 *
 * @author reda
 */
public class MainRule extends Rule {
    
    List<Integer> elements;
    
    public MainRule(){
        super();
    }
    
    @Override
    public String getCodedRightSide(){
        StringBuilder str=new StringBuilder();
        elements.stream().forEach(i->str.append(i).append(" "));
        return str.toString();
    }
    
    @Override
    public String getRightSide(){
        StringBuilder str=new StringBuilder();
        elements.stream().forEach(i-> str.append(WordsDictionary.getWord(i)).append(" "));
        return str.toString();
    }
    
    public void setElements(List<Integer> elem){
        this.elements=elem;
    }
    
    
}
