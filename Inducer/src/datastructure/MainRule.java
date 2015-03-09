/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import com.carrotsearch.hppc.IntArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author reda
 */
public class MainRule extends Rule {
    
    List<Integer> elements;
    //int[] elements;
    
    public List<SubRule> relatedSubRules;
    
    IntArrayList activeSlotIDs;
    
    public MainRule(){
        super();
        this.elements=null;//new ArrayList<>();
        this.relatedSubRules=new ArrayList<>();
        this.activeSlotIDs=new IntArrayList();
        this.ruletype=RuleType.Main;
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
    
    public List<Integer> getElements(){
        return this.elements;
    }
    
    public void setSlotIDs(IntArrayList slotsIDs){
        this.activeSlotIDs=slotsIDs;
    }
    
    public static MainRule makeMainRule(List<SubRule> subRules,FrequentPattern fp,CommonSlots cslots){
        if(subRules==null || fp==null || cslots==null)
            return null;
        List<SubRule> templist=new ArrayList<>(subRules);
        IntArrayList slotsIDs=new IntArrayList(cslots.slots);
        MainRule mr=new MainRule();
        List<Integer> elems=new ArrayList<>();
        List<Integer> activeSlots=new ArrayList<>();
        IntArrayList referencesList=new IntArrayList(cslots.commonReferences);
        
        //add (slot pattern)*
        for(int i=0;i<fp.patterns.size();i++){
            if(slotsIDs.contains(i)){
                elems.add(WordsDictionary.getWordIndex(templist.get(0).leftSide));
                mr.relatedSubRules.add(templist.remove(0));
                activeSlots.add(i);
            }
            
            elems.add(fp.patterns.get(i));
        }
        //add the last slot if exist
        if(slotsIDs.contains(fp.patterns.size())){
            elems.add(WordsDictionary.getWordIndex(templist.get(0).leftSide));
            mr.relatedSubRules.add(templist.remove(0));
            activeSlots.add(fp.patterns.size());
        }
        //set elems, active slots, and referencesIndexs
        mr.setElements(elems);
        mr.setSlotIDs(slotsIDs);
        mr.setReferencesIndexs(referencesList);
        return mr;
    }
    
    @Override
    public RuleType getRuleType(){
        return this.ruletype;
    }
    
    public Sentence toSentence(){
        Sentence res=new Sentence(this.leftSide);
        return res;
    }
    
    public Long getGenerativeCount(){
        long res=1;
        try{
            for(SubRule r: this.relatedSubRules)
            {
                if(r.getRuleType()==RuleType.Sub){
                    //res*=r.getUniqueAlternatives();
                    res=java.lang.Math.multiplyExact(res, r.getUniqueAlternatives());
                }
            }
        }catch (ArithmeticException ae){
            return Long.MAX_VALUE;
        }
        return res;
    }
    
    public boolean isActiveSlot(int id){
        return this.activeSlotIDs.contains(id);
    }
}
