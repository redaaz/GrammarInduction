/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 *
 * @author reda
 */
public class General {
    public static boolean tryParseInt(String value)  
    {  
        try  
        {  
            Integer.parseInt(value);  
            return true;  
        } catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
    }
    
    
    public static boolean ContainsSequence( List<Integer> pattern, List<Integer> toTest)
    {
        if(pattern.isEmpty())
            return true;
        if(pattern.size()==1)
            return toTest.contains(pattern.get(0));
        
        
        List<Boolean> test=new ArrayList<>(Collections.nCopies(pattern.size(), false));
        
        int currentPosition=0;
        for(int i=0;i<pattern.size();i++){
            
            for(int j=currentPosition;j<toTest.size();j++,currentPosition++){
                if(Objects.equals(pattern.get(i), toTest.get(j))){
                    test.set(i, Boolean.TRUE);
                    break;
                }
                
            }
            
            
        }
        
        if (test.contains(Boolean.FALSE)) return false;

        return true;
    }
    
    //convert from CMSPAM coded string to list of integer
    public static List<Integer> toIntegerList(String x){
        List<Integer> res=new ArrayList<>();
        String str=x.replace("-2", "");
        String replaced = str.replace(" -1 "," ");
        String[] tokens= replaced.split(" ");
        
        for(String s:tokens){
            if(tryParseInt(s)){
                res.add(Integer.parseInt(s));
            }
            else{
                return null;
            }
        }
        
        return res;
    }
    
    //to siplit an integer list by splitters -returning n+1 lists of slots between splitters (sometimes empty lists added)
    public static List<List<Integer>> split(List<Integer> toSplit,List<Integer> splitters){
        List<List<Integer>> res=new ArrayList<>();
        if(toSplit.isEmpty() || splitters.isEmpty())
            return null;
        int lastIndex=0;
        int index=0;
        
        for(Integer sp:splitters){    
            while(!Objects.equals(toSplit.get(index), sp)){
                index++;
            }
            if(lastIndex==index){
                res.add(new ArrayList<>());
            }
            else{
                res.add(toSplit.subList(lastIndex, index));    
            }   
            index++;
            lastIndex=index;
        }
        //the last list
        if(index==toSplit.size()){
            res.add(new ArrayList<>());
        }
        else{
           res.add(toSplit.subList(index, toSplit.size()));     
        }
        return res;
    }
}
