/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntContainer;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import text.General;

/**
 *
 * @author reda
 */
public class SubRule extends Rule {
    List<Alternative> alternatives;
    
    public SubRule(){
        super();
        this.alternatives=new ArrayList<>();
        this.ruletype=RuleType.Sub;
    }
    
    public void setAlternatives(List<Alternative> alters){
        this.alternatives=alters;
    }
    
    @Override
    public String getCodedRightSide(){
        if(this.alternatives.isEmpty())
            return null;       
        Set<Alternative> set=new HashSet<>(this.alternatives);
        List<Alternative> uniqueList=new ArrayList<>(set);
        StringBuilder str=new StringBuilder();
        uniqueList.stream().forEach(alt-> {     
            StringBuilder subStr=new StringBuilder();
            subStr.append(alt.toStringCode());
            str.append(subStr).append(" | ");
        } );
        return str.substring(0, str.length()-3);
    }
    
    @Override
    public String getRightSide(){
        if(this.alternatives.isEmpty())
            return null;
        Set<Alternative> set=new HashSet<>(this.alternatives);
        List<Alternative> uniqueList=new ArrayList<>(set);
        StringBuilder str=new StringBuilder();
        uniqueList.stream().forEach(alt -> {
            StringBuilder str1=new StringBuilder();
            str1.append(alt.toString());
            str.append(str1).append(" | ");
        });
        return str.substring(0, str.length()-3);
    }    
    
    public long getUniqueAlternatives(){
        if(this.alternatives.isEmpty())
            return 0;
        int uniqueCount=0;
        long SecMainRules=1;
        ObjectOpenHashSet<Alternative> set=new ObjectOpenHashSet();
        try{
            for(Alternative al:this.alternatives)
            {
                if(al.referenceIndex!=-1){
                    set.add(al);
                }
                else{
                    SecMainRules=java.lang.Math.multiplyExact(SecMainRules,  al.getRelatedSecondaryMainRule().getGenerativeCount());
                }
            }
        }catch(ArithmeticException ae){
            return Long.MAX_VALUE;
        }
        
        uniqueCount=set.size();
        SecMainRules=SecMainRules==1?0:SecMainRules;
        return uniqueCount+SecMainRules;
    }
    
    @Override
    public RuleType getRuleType(){
        return this.ruletype;
    }
    
    public static SubRule makeSubRule(Slot slot,CommonSlots cSlots){
        if(slot==null)
            return null;
        SubRule sb=new SubRule();
        IntArrayList referencesList=new IntArrayList(cSlots.commonReferences);
        List<Alternative> alts=slot.getAlternatives(referencesList);
        sb.setAlternatives(alts);
        sb.setReferencesIndexs(referencesList);
        return sb;
    }
    
    public List<Sentence> getAlternatives(){
        List<Sentence> res=new ArrayList<>();
        
        for(Alternative alt: this.alternatives){
            Sentence sen=new Sentence();
            sen.setSentenceCode(alt.alterCode);
            sen.setOriginalReference(alt.getReferenceIndex());
            res.add(sen);
        }
        
        return res;
    }
    
    public void updateAlternatives(List<Sentence> input,List<Rule> newSecRules){
        List<Alternative> alter=new ArrayList<>();
        
        for(Sentence se:input){
            IntArrayList code=new IntArrayList();
            for(Integer i: se.sentenceCode)
                code.add((int)i);
            Alternative al=new Alternative( code,se.getOriginalReference());
            if(se.getOriginalReference()==-1)
            {
                for(Rule r:newSecRules){
                    if(r.ruletype==RuleType.SecondaryMain && (Objects.equals(WordsDictionary.getWordIndex(r.leftSide), se.sentenceCode.get(0)))){
                        al.setRelatedSecondaryMainRule((SecondaryMainRule) r);
                    }
                }
                
            }
            if(al.getRelatedSecondaryMainRule()==null && al.getReferenceIndex()==-1){
                String uuu=WordsDictionary.getWord(al.alterCode.get(0));
                int u=9+3;
            }
            alter.add(al);
        }
        this.alternatives=alter;
    }
    
    public IntIntOpenHashMap getOrderedIndexToOriginalIndexMap(){
        IntIntOpenHashMap res=new IntIntOpenHashMap();
        
        for(int i=0;i<this.referencesIndexs.size();i++){
            res.put(i, this.referencesIndexs.get(i));
        }
        
        return res;
    }
    
    
}
