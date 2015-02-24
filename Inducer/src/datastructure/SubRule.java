/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    
    @Override
    public RuleType getRuleType(){
        return this.ruletype;
    }
    
    public static SubRule makeSubRule(Slot slot,CommonSlots cSlots){
        if(slot==null)
            return null;
        SubRule sb=new SubRule();
        List<Integer> referencesList=new ArrayList<>(cSlots.commonReferences);
        List<Alternative> alts=slot.getAlternatives(referencesList);
        sb.setAlternatives(alts);
        sb.setReferencesIndexs(referencesList);
        return sb;
    }
}
