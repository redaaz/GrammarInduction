/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author reda
 */
public class Rule {
    
    protected static Integer idCounter=0;
    protected Integer ruleID;
    protected String leftSide;
    
    
    public Rule(){
        
        this.ruleID=Rule.idCounter++;
        this.leftSide="<R"+this.ruleID+">";
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
    
}
