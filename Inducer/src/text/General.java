/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package text;

import datastructure.CommonSlots;
import datastructure.FrequentPattern;
import datastructure.Slot;
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
        
        CommonSlots cs=intersect(in,minSup,tempIndexsToinputIndexs);
        return cs;
    }
    
    //lists: list of references (input indexs for each slot)
    //this function find the best intersection between slots to build new rules
    static CommonSlots intersect(List<List<Integer>> lists,double minSup, HashMap<Integer,Integer> tempIndexsToinputIndexs){
        if(lists==null || lists.isEmpty())
            return null;
        if(lists.size()==1){
            CommonSlots res=new CommonSlots();
            res.commonReferences= lists.get(0);
            res.solts=Arrays.asList(0);
            return res;
        }
        List<String> input=new ArrayList<>();
        lists.stream().forEach(li->input.add(integerListToCMSPAMString(li)));
        //find best slots set to build new rules
        AlgoCMSPAM algo=new AlgoCMSPAM();
        List<FrequentPattern> results= algo.runAlgorithm2(input, minSup);
        MostFrequentLongest mfl=new MostFrequentLongest();
        // pattern here: is common slots between references
        // sentences here: each reference(slots list) is an input sentence 
        // references here: is chosen reference's ids to build new rules
        FrequentPattern bestSlot=mfl.chooseFrequentPattern(results);
        if(bestSlot==null)
            return new CommonSlots();
        CommonSlots res=new CommonSlots();
        res.commonReferences=bestSlot.getReferencesList().stream().map(x->tempIndexsToinputIndexs.get(x)).collect(Collectors.toList());
        res.solts=bestSlot.getPattern();
        return res;
        
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
