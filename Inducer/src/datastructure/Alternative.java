/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import java.util.List;

/**
 *
 * @author reda
 */
public class Alternative {
    List<Integer> alterCode;
    int referenceIndex;
    
    public Alternative(List<Integer> AlterCode,int refIndex){
        this.alterCode=AlterCode;
        this.referenceIndex=refIndex;
    }
    
    @Override
    public String toString(){
        String str="";
        str = this.alterCode.stream().map((ii) -> WordsDictionary.getWord(ii) + " ").reduce(str, String::concat);
        return str;
    }
    
    public String toStringCode(){
        String str="";
        str = this.alterCode.stream().map(x->""+x+" ").reduce(str,String::concat);
        return str;
    }
    
    public int getReferenceIndex(){
        return this.referenceIndex;
    }

    @Override
    public boolean equals(Object aThat){
        Alternative that=(Alternative)aThat;
        if(that.alterCode.size()!=this.alterCode.size())
            return false;
        return this.alterCode.equals(that.alterCode);
        //ignoring reference index
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
}
