/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import datastructure.CommonSlots;
import datastructure.FrequentPattern;
import datastructure.Rule;
import datastructure.Sentence;
import datastructure.Slot;
import heuristic.Heuristic;
import heuristic.LongestMostFrequent;
import heuristic.MostFrequentLongest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import spm.spam.AlgoCMSPAM;
import spm.spam.SPMiningAlgorithm;


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
    
    public static CommonSlots findCommonReferencesSlots(HashMap<Integer,List<Integer>> referenceToSlots,double minSup){
        List<List<Integer>> in=new ArrayList<>();
        HashMap<Integer,Integer> tempIndexsToinputIndexs=new HashMap<>();
        Iterator it = referenceToSlots.entrySet().iterator();
        while(it.hasNext()){
             HashMap.Entry pairs = (HashMap.Entry)it.next();
             in.add((List<Integer>)pairs.getValue());
             tempIndexsToinputIndexs.put(in.size()-1, (Integer)pairs.getKey());
        }
        AlgoCMSPAM algo=new AlgoCMSPAM();
        Heuristic heu=new LongestMostFrequent();
        //CommonSlots cs=intersect(in,algo,heu,minSup,tempIndexsToinputIndexs);
        CommonSlots cs=intersect(in,tempIndexsToinputIndexs);
        //minSup condition
        if(cs!=null && cs.commonReferences.size()>=minSup)
            return cs;
        return null;
    }
    
    //lists: list of slots for each input indexs
    //tempIndexsToinputIndexs: to convert from 0-based indexs sentences to global indexs
    //this function find the best intersection between slots to build new rules (longest - most frequent)
    @SuppressWarnings("empty-statement")
    static CommonSlots intersect(List<List<Integer>> lists,HashMap<Integer,Integer> tempIndexsToinputIndexs){
        if(lists==null || lists.isEmpty())
            return null;
        
        List<String> slotsStringforEachInput= new ArrayList<>();
        lists.stream().forEach(li->{
           StringBuilder str=new StringBuilder();
           li.stream().forEach(elem-> str.append(elem));
           slotsStringforEachInput.add(str.toString());
        });
        
        HashMap<String,Integer> Counter=new HashMap<>();
        int MaxCount=0;
        List<String> MaxString=new ArrayList<>();
        HashMap<String,List<Integer>> refIds=new HashMap<>();
        int IdCounter=0;
        for(String str:slotsStringforEachInput) {
            if(Counter.get(str)==null){
                Counter.put(str,1);
            }
            else{
                Counter.put(str, Counter.get(str)+1);
            }
            
            if(Counter.get(str)>MaxCount){
                MaxString.clear();
                MaxString.add(str);
                MaxCount=Counter.get(str);
                
            }else if(Counter.get(str)==MaxCount){
                if(str.length()>MaxString.get(0).length())
                    MaxString.add(0, str);
                else
                    MaxString.add(str);
                
            }
            if(refIds.get(str)==null)
                refIds.put(str, new ArrayList<>());
            refIds.get(str).add(IdCounter);
            IdCounter++;
        };
        
        if(MaxString.isEmpty())
            return null;
        
        //if MaxString.size()>1
        CommonSlots res=new CommonSlots();
        res.commonReferences=refIds.get(MaxString.get(0)).stream().map(x->tempIndexsToinputIndexs.get(x)).collect(Collectors.toList());
        
        res.slots=new ArrayList<>();
        for(Character cc:MaxString.get(0).toCharArray()){
            res.slots.add(Integer.parseInt(""+cc));
        }
        return res;
        //else if ==0
        
    }
    
    public static String integerListToCMSPAMString(List<Integer> in){
        StringBuilder str=new StringBuilder();
        if(in==null || in.isEmpty())
            return "-2";
        in.stream().forEach(x->str.append(x).append(" -1 "));
        str.append("-2");
        return str.toString();
    }
}
