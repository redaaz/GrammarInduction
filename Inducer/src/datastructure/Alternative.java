/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package datastructure;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.cursors.IntCursor;




/**
 *
 * @author reda
 */
public class Alternative {
    IntArrayList alterCode;
    int referenceIndex;
    
    public Alternative(IntArrayList AlterCode,int refIndex){
        this.alterCode=AlterCode;
        this.referenceIndex=refIndex;
    }
    
    @Override
    public String toString(){
//        String str="";
//        str = this.alterCode.stream().map((ii) -> WordsDictionary.getWord(ii) + " ").reduce(str, String::concat);
        StringBuilder str=new StringBuilder();
        for(IntCursor x:this.alterCode){
            str.append(WordsDictionary.getWord(x.value)).append(" ");
        }
        return str.toString();
    }
    
    public String toStringCode(){
//        String str="";
//        str = this.alterCode.stream().map(x->""+x+" ").reduce(str,String::concat);
        StringBuilder str=new StringBuilder();
        for(IntCursor x:this.alterCode){
            str.append(x.value).append(" ");   
        }
        return str.toString();
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
