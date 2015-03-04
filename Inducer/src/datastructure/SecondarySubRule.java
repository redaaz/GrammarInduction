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
public class SecondarySubRule extends SubRule{
    
    public SecondarySubRule(){
        super();
        this.ruletype=RuleType.SecondarySub;
    }
    
    public SecondarySubRule(SubRule sb){
        this.alternatives=new ArrayList<>(sb.alternatives);
        this.leftSide=sb.leftSide;
        this.referencesIndexs=new IntArrayList(sb.referencesIndexs);
        this.ruleID=sb.ruleID;
        this.ruletype=RuleType.SecondarySub;
        Rule.idCounter--;
    }
    
    
}
