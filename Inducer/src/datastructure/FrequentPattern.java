/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.List;
import text.General;

/**
 *
 * @author reda
 */
public class FrequentPattern {
    List<Integer> pattern;
    int sup;
    
    public FrequentPattern(){
        pattern = new ArrayList<>();
        sup=0;
    }
    
    public FrequentPattern(List<Integer> in,int supp){
        this.sup=supp;
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
}
