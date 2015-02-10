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
public class SubRule extends Rule {
    List<List<Integer>> alternatives;
    
    public SubRule(){
        super();
    }
    
    public void setAlternatives(List<List<Integer>> alters){
        this.alternatives=alters;
    }
    
    @Override
    public String getCodedRightSide(){
        if(this.alternatives.isEmpty())
            return null;
        StringBuilder str=new StringBuilder();
        alternatives.stream().forEach(li-> {     
            StringBuilder subStr=new StringBuilder();
            li.stream().forEach(i-> subStr.append(i).append(" "));
            str.append(subStr).append(" | ");
        } );
        return str.substring(0, str.length()-3);
    }
    
    @Override
    public String getRightSide(){
        if(this.alternatives.isEmpty())
            return null;
        StringBuilder str=new StringBuilder();
        alternatives.stream().forEach(li -> {
            StringBuilder str1=new StringBuilder();
            li.stream().forEach(i-> str1.append(WordsDictionary.getWord(i)).append(" "));
            str.append(str1).append(" | ");
        });
        return str.substring(0, str.length()-3);
    }
}
