/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.Bitmap;
import text.General;

/**
 *
 * @author reda
 */
public class FrequentPattern {
    List<Integer> patterns;
    int sup;
    List<Integer> inputReferences;
    double cohesion; // calculated by Jaccard index (law)
    
    public FrequentPattern(){
        patterns = new ArrayList<>();
        inputReferences=new ArrayList<>();
        sup=0;
        this.cohesion=-1;
    }
    
    public FrequentPattern(List<Integer> in,int supp){
        this.sup=supp;
        inputReferences=new ArrayList<>();
        patterns = new ArrayList<>();
        patterns = in;
        this.cohesion=-1;
    }
    //take string from CMSPAM format "number -1 number -1 .... -1 -2"
    public FrequentPattern(String x){
        //check for null or empty string
        if(x==null || x.isEmpty()){
            patterns = null;
            sup=-1;
            return;
        }   
        String[] parts=x.split("SUP: ");
        //check for parsing error
        if(parts.length!=2){
            patterns = null;
            sup=-1;
            return;
        }
        
        //set sup value
        if(!General.tryParseInt(parts[1])){
            patterns=null;
            sup=-1;
            return;
        }
        else{
            sup=Integer.parseInt(parts[1]);
        }
        this.cohesion=-1;
        patterns = new ArrayList<>();
        String[] Indexes= parts[0].split(" -1 ");
        
        for(String str: Indexes){
            boolean valid=General.tryParseInt(str);
            if(!str.isEmpty() && valid){
                patterns.add(Integer.parseInt(str));        
            }
            if(!valid){
                patterns=null;
                sup=-1;
                return;
            }
                
        }
        
    }
    
    @Override
    public String toString(){
        if(patterns==null)
            return null;
        if(patterns.isEmpty())
            return "";
        
        String res="";
        
        res = patterns.stream().filter((index) -> (WordsDictionary.isExist((int)index))).map((index) -> WordsDictionary.getWord(index)+" ").reduce(res, String::concat);
        
        return res;
    }
    
    public String toStringWithSup(){
        return toString() + "SUP: "+sup;
    }
    
    public String toStringCode(){
        String res="";
        if(patterns==null)
            return null;
        if(patterns.isEmpty())
            return "";
        res= patterns.stream().map((i) -> ""+i+" ").reduce(res, String::concat);
        return res;
    }
    
    public String toStringCodeWithSup(){
        return toStringCode()+ "SUP: "+sup;
    }
    
    //@param input is a list of repetitions for each token in the frequent pattern - repetition means: [inputSentenceID & tokenPosition in such input sentence ]
    public void setInputReferences(List<List<Repetition>> input,List<Sentence> in ){
        this.inputReferences=intersect(input,in);
    }
    //to calc cohesion by Jaccard index law i.e.: intersection(11)/union (10||01||11)
    //@param vdb is the vertical data base used in CMSPAM the algorithm
    //required: sup value 
    public void setCohesion(Map<Integer,Bitmap> vdb){
        if (this.patterns.size()<=1){
            this.cohesion=0;//to be discussed
            return;
        }
        Set<Integer> set = new HashSet<>();
        for(Integer pp:this.patterns){
            set.addAll(((Bitmap)vdb.get(pp)).inputReferences.stream().map(X -> X.inputSentenceID).collect(Collectors.toList()));
        }
        if(set.isEmpty())
            this.cohesion=-1;
        this.cohesion=this.sup/(double)set.size();
    }
    
    public double getCohesion(){
        return this.cohesion;
    }
    
    public List<Integer> getPattern(){
        return this.patterns;
    }
    
    public void printReferencesList(){
        this.inputReferences.stream().forEach((i) -> {
            System.out.print(i+" ");
        });
         System.out.println();
    }
    
    public String getReferencesList(){
        String res="";
        res = this.inputReferences.stream().map((i) -> (""+i+" ")).reduce(res, String::concat);
        return res;
    }
    //check that all pattern items appear in the refreces sentences in the correst order
    //this is not efficient solution for this issue 
    public List<Integer> patternOrderCorrection(List<Integer> references){
        List<Integer> res=new ArrayList<>();
        
        
                
        return res;
    }
    
    public  List<Integer> intersect(List<List<Repetition>> input,List<Sentence> in){
        
        
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
            if (General.ContainsSequence(this.patterns,General.toIntegerList(in.get(i).toStringCM_SPAM())))
               res.add(i);       
        }
        return res;
    }
    
    public void println(){
        System.out.println(this.toString()+" ("+this.sup+") "+this.getReferencesList());
    }

    public int getSup(){
        return this.sup;
    }
}
