/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;


import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.cursors.IntCursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spm.spam.SPMiningAlgorithm;
import text.General;


/**
 *
 * @author reda
 */
public class Rule {
    protected RuleType ruletype;
    public static int idCounter=0;
    protected int ruleID;
    protected String leftSide;
    public IntArrayList referencesIndexs;
    
    public Rule(){ 
        this.ruleID=Rule.idCounter++;
        this.leftSide="<R"+this.ruleID+">";
        WordsDictionary.addWord(leftSide);
        referencesIndexs=new IntArrayList();
    }
    
    public void setReferencesIndexs(IntArrayList ref){
        this.referencesIndexs=ref;
    }
    
    public IntArrayList getReferencesIndexs(){
        return this.referencesIndexs;
    }
    
    public String getCodedRightSide(){
         return "Rule getCodedRightSide";
    }
    
    public String toCodedString(){
        return leftSide+" -> "+getCodedRightSide();
    }

    public String getRightSide(){
        return "Rule getRightSide";
    }
    
    @Override
    public String toString(){
        return leftSide+" -> "+getRightSide();
    }
    
    public int getRuleIndex(){
        return WordsDictionary.getWordIndex(leftSide);
    }
    
    public void println(){
        System.out.println(toString());
    }
    
    public void printlnCoded(){
        System.out.println(toCodedString());
    }
    
    public static List<Rule> makeRules(List<Sentence> input,FrequentPattern fp,SPMiningAlgorithm algo,double minSup){
        List<Rule> rules=new ArrayList<>();
        //lists of n+1 slot's alternatives (n:nb of items in the frequent pattern)
        //each slot has a list of alternatives
        List<Slot> slots = new ArrayList<>();
        
        for(int i=0;i<=fp.patterns.size();i++)
            slots.add(new Slot());
        
        //HashMap<Integer,List<Integer>> referencesBesedtoSlots=new HashMap<>();
        IntObjectOpenHashMap<IntArrayList> referencesBesedtoSlots=new IntObjectOpenHashMap();
        
        fp.inputReferences.stream().forEach((refIndex) -> {
            List<IntArrayList> alters=General.split(input.get(refIndex).sentenceCode, fp.patterns);
            referencesBesedtoSlots.put(refIndex, new IntArrayList());
            for(int i=0;i<alters.size();i++){
                if(!alters.get(i).isEmpty()){
                    referencesBesedtoSlots.get(refIndex).add(i);
                    Alternative al=new Alternative(alters.get(i),refIndex);
                    slots.get(i).addAlter(al);
                }
            }
        });
        
        double minimumSupport=referencesBesedtoSlots.size()*minSup;
        CommonSlots cslots = General.findCommonReferencesSlots(referencesBesedtoSlots,algo,minimumSupport);
        
        while(cslots!=null && !cslots.slots.isEmpty()){
            List<SubRule> tempNewSubRules=new ArrayList<>();
            for(IntCursor slotID: cslots.slots){
                SubRule newSR = SubRule.makeSubRule(slots.get(slotID.value),cslots);
                tempNewSubRules.add(newSR);
            }
            //1-make main rule
            MainRule newMR = MainRule.makeMainRule(tempNewSubRules, fp, cslots);
            //2-add new rules
            tempNewSubRules.stream().forEach(x->rules.add(x));
            rules.add(newMR);
            //3-do some changes
            updateReferencesBesedtoSlotsHashMap(newMR,referencesBesedtoSlots);
            
            cslots = General.findCommonReferencesSlots(referencesBesedtoSlots,algo,minimumSupport);
        }

        return rules;
    }
    
    public static void updateReferencesBesedtoSlotsHashMap(MainRule mr,IntObjectOpenHashMap<IntArrayList> referencesBesedtoSlotsMap){
        for(IntCursor i: mr.referencesIndexs){
            if(referencesBesedtoSlotsMap.get(i.value)!=null){
                //referencesBesedtoSlotsMap.get(i.value).removeAll(mr.activeSlotIDs);
                for(IntCursor xx:mr.activeSlotIDs){
                    referencesBesedtoSlotsMap.get(i.value).remove(xx.value);
                }
                if(referencesBesedtoSlotsMap.get(i.value).isEmpty()){
                    referencesBesedtoSlotsMap.remove(i.value);
                }
            }
        }
    }
    
    public RuleType getRuleType(){
        return null;
    }
    
    public static List<Rule> convertToSecondaryRules(List<Rule> input,SubRule sourceSubRule){
        List<Rule> res=new ArrayList<>();
        IntObjectOpenHashMap<IntArrayList> mainIDToRelatedSubIDs=new IntObjectOpenHashMap();
        IntIntOpenHashMap rulesIDToResIndex=new IntIntOpenHashMap();
        
        for(Rule r: input){
            if(r.ruletype==RuleType.Main){
                SecondaryMainRule smr=new SecondaryMainRule((MainRule)r);
                //smr.setReferencesIndexs(smr.getReferencesIndexs());
                smr.setSourceSubRule(sourceSubRule);
                IntArrayList ids=new IntArrayList();
                for(SubRule sb: ((MainRule)r).relatedSubRules){
                    ids.add(sb.ruleID);
                }
                mainIDToRelatedSubIDs.put(r.ruleID, ids);
                
                res.add(smr);
                rulesIDToResIndex.put( smr.ruleID,res.size()-1);
            }else if(r.ruletype==RuleType.Sub){
                SecondarySubRule ssb=new SecondarySubRule((SubRule)r);
                //ssb.setReferencesIndexs(ssb.getReferencesIndexs());

                
                res.add(ssb);
                rulesIDToResIndex.put( ssb.ruleID,res.size()-1);
            }
        }
        
        for(Rule r: res){
            if(r.ruletype==RuleType.SecondaryMain){
                List<SubRule> list=new ArrayList<>();
                for(IntCursor i: mainIDToRelatedSubIDs.get(r.ruleID)){
                    list.add((SecondarySubRule) res.get(rulesIDToResIndex.get(i.value)));
                }
                ((SecondaryMainRule)r).relatedSubRules=list;
            }
        }
        
        return res;
    }
    
    public int getRuleID(){
        return this.ruleID;
    }
    
    public String getLeftSide(){
        return this.leftSide;
    }
}
