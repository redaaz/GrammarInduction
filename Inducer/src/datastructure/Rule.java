/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import text.General;


/**
 *
 * @author reda
 */
public class Rule {
    
    protected static Integer idCounter=0;
    protected Integer ruleID;
    protected String leftSide;
    protected List<Integer> referencesIndexs;
    
    public Rule(){ 
        this.ruleID=Rule.idCounter++;
        this.leftSide="<R"+this.ruleID+">";
        WordsDictionary.addWord(leftSide);
        referencesIndexs=new ArrayList<>();
    }
    
    public void setReferencesIndexs(List<Integer> ref){
        this.referencesIndexs=ref;
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
    
    public Integer getRuleIndex(){
        return WordsDictionary.getWordIndex(leftSide);
    }
    
    public void println(){
        System.out.println(toString());
    }
    
    public void printlnCoded(){
        System.out.println(toCodedString());
    }
    
    public static List<Rule> makeRules(List<Sentence> input,FrequentPattern fp,double minSup){
        List<Rule> rules=new ArrayList<>();
        //lists of n+1 slot's alternatives (n:nb of items in the frequent pattern)
        //each slot has a list of alternatives
        List<Slot> slots = new ArrayList<>();
        
        for(int i=0;i<=fp.patterns.size();i++)
            slots.add(new Slot());
        
        HashMap<Integer,List<Integer>> referencesBesedtoSlots=new HashMap<>();
        
        fp.inputReferences.stream().forEach((refIndex) -> {
            List<List<Integer>> alters=General.split(input.get(refIndex).sentenceCode, fp.patterns);    
            referencesBesedtoSlots.put(refIndex, new ArrayList<>());
            for(int i=0;i<alters.size();i++){
                if(!alters.get(i).isEmpty()){
                    referencesBesedtoSlots.get(refIndex).add(i);
                    Alternative al=new Alternative(alters.get(i),refIndex);
                    slots.get(i).addAlter(al);
                }
            }
        });
        
        CommonSlots cslots = General.findCommonReferencesSlots(referencesBesedtoSlots,minSup);
        
        while(cslots!=null && !cslots.solts.isEmpty()){
            List<SubRule> tempNewSubRules=new ArrayList<>();
            for(Integer slotID: cslots.solts){
                SubRule newSR = SubRule.makeSubRule(slots.get(slotID),cslots.commonReferences);
                tempNewSubRules.add(newSR);
                newSR.println();
            }
            //TODO: complete the impelentation
            cslots = General.findCommonReferencesSlots(referencesBesedtoSlots,minSup);
        }
        
        
        return null;
    }
    
}
