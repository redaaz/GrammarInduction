/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    }
    
    public void setAlternatives(List<Alternative> alters){
        this.alternatives=alters;
    }
    
    @Override
    public String getCodedRightSide(){
        if(this.alternatives.isEmpty())
            return null;
        StringBuilder str=new StringBuilder();
        alternatives.stream().forEach(alt-> {     
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
        StringBuilder str=new StringBuilder();
        alternatives.stream().forEach(alt -> {
            StringBuilder str1=new StringBuilder();
            str1.append(alt.toString());
            str.append(str1).append(" | ");
        });
        return str.substring(0, str.length()-3);
    }    
    
    public static SubRule makeSubRule(Slot slot){
        if(slot==null)
            return null;
        SubRule sb=new SubRule();
        sb.setAlternatives(slot.alternatives);
        sb.setReferencesIndexs(slot.getReferenceIndexs());
        return sb;
    }
}
