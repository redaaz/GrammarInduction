/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import text.General;

/**
 *
 * @author reda
 */
public class FrequentPattern {
    List<Integer> pattern;
    int sup;
    List<Integer> inputReferences;
    
    public FrequentPattern(){
        pattern = new ArrayList<>();
        inputReferences=new ArrayList<>();
        sup=0;
    }
    
    public FrequentPattern(List<Integer> in,int supp){
        this.sup=supp;
        inputReferences=new ArrayList<>();
        pattern = new ArrayList<>();
        pattern = in;
    }
    //take string from CMSPAM format "number -1 number -1 .... -1 -2"
    public FrequentPattern(String x){
        //check for null or empty string
        if(x==null || x.isEmpty()){
            pattern = null;
            sup=-1;
            return;
        }   
        String[] parts=x.split("SUP: ");
        //check for parsing error
        if(parts.length!=2){
            pattern = null;
            sup=-1;
            return;
        }
        
        //set sup value
        if(!General.tryParseInt(parts[1])){
            pattern=null;
            sup=-1;
            return;
        }
        else{
            sup=Integer.parseInt(parts[1]);
        }
        
        pattern = new ArrayList<>();
        String[] Indexes= parts[0].split(" -1 ");
        
        for(String str: Indexes){
            boolean valid=General.tryParseInt(str);
            if(!str.isEmpty() && valid){
                pattern.add(Integer.parseInt(str));        
            }
            if(!valid){
                pattern=null;
                sup=-1;
                return;
            }
                
        }
        
    }
    
    @Override
    public String toString(){
        if(pattern==null)
            return null;
        if(pattern.isEmpty())
            return "";
        
        String res="";
        
        res = pattern.stream().filter((index) -> (WordsDictionary.isExist((int)index))).map((index) -> WordsDictionary.getWord(index)+" ").reduce(res, String::concat);
        
        return res;
    }
    
    public String toStringWithSup(){
        return toString() + "SUP: "+sup;
    }
    
    public String toStringCode(){
        String res="";
        if(pattern==null)
            return null;
        if(pattern.isEmpty())
            return "";
        res= pattern.stream().map((i) -> ""+i+" ").reduce(res, String::concat);
        return res;
    }
    
    public String toStringCodeWithSup(){
        return toStringCode()+ "SUP: "+sup;
    }
    
    //@param input is a list of repetitions for each token in the frequent pattern - repetition means: [inputSentenceID & tokenPosition in such input sentence ]
    public void setInputReferences(List<List<Repetition>> input,List<String> in ){
        this.inputReferences=intersect(input,in);
    }
    
    public List<Integer> getPattern(){
        return this.pattern;
    }
    
    public void printReferencesList(){
        this.inputReferences.stream().forEach((i) -> {
            System.out.print(i+" ");
        });
         System.out.println();
    }
    //check that all pattern items appear in the refreces sentences in the correst order
    //this is not efficient solution for this issue 
    public List<Integer> patternOrderCorrection(List<Integer> references){
        List<Integer> res=new ArrayList<>();
        
        
                
        return res;
    }
    
    
    
    public  List<Integer> intersect(List<List<Repetition>> input,List<String> in){
        
        
        //choose smallest set to optimaize the intersection
        int minListSizeIndex=-1;
        int minListSize=Integer.MAX_VALUE;
        for(int i=0;i<input.size();i++){
            if(!input.get(i).isEmpty()&& input.get(i).size()<minListSize){
                minListSizeIndex=i;
                minListSize=input.get(i).size();
            }        
        }
        
        Set<Integer> uniqueNums = new HashSet<>(input.get(minListSizeIndex).stream()
        .map(x->x.inputSentenceID).collect(Collectors.toList()));
        
        for(int i=0;i<input.size();i++){
            if(i!=minListSizeIndex)
                uniqueNums.retainAll(new HashSet<>(input.get(i).stream()
        .map(x->x.inputSentenceID).collect(Collectors.toList())));
        }
        
        
        List<Integer> res1=new ArrayList<>(uniqueNums);
        List<Integer> res=new ArrayList<>();
        
        for(Integer i: res1){
            
            if (General.ContainsSequence(this.pattern,General.toIntegerList(in.get(i))))
               res.add(i);
                
        }
        
        
        
        return res;
    }
}
