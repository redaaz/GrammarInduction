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
public class CommonSlots {
    public IntArrayList commonReferences;
    public IntArrayList slots;
    
    public CommonSlots(){
     this.commonReferences=new IntArrayList();
     this.slots=new IntArrayList();
    }
    
}
