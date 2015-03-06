/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import com.carrotsearch.hppc.IntArrayList;
import java.util.ArrayList;

/**
 *
 * @author reda
 */
public class SecondaryMainRule extends MainRule{
    
    private SubRule sourceSubRule;
    
    public SecondaryMainRule(){
        super();
        this.ruletype=RuleType.SecondaryMain;
    }
    
    public SecondaryMainRule(MainRule mr){
        this.activeSlotIDs = new IntArrayList(mr.activeSlotIDs);
        this.elements =new ArrayList<>( mr.elements);
        this.leftSide=mr.leftSide;
        this.referencesIndexs=new IntArrayList(mr.referencesIndexs);
        this.ruleID=mr.ruleID;
        this.relatedSubRules=new ArrayList<>();
        this.ruletype=RuleType.SecondaryMain;
        Rule.idCounter--;
    }
    
    public void setSourceSubRule(SubRule source){
        this.sourceSubRule=source;
    }
    
    public SubRule getSourceSubRule(){
        return this.sourceSubRule;
    }
    
    @Override
    public Long getGenerativeCount(){
        long res=1;
        for(SubRule r: this.relatedSubRules)
        {
            if(r.getRuleType()==RuleType.SecondarySub)
                res*=r.getUniqueAlternatives();
        }
        return res;
    }
}
