/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package inducer;

import datastructure.MainRule;
import datastructure.Rule;
import datastructure.RuleType;
import datastructure.Sentence;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author reda
 */
public class GI {
    
    public List<Sentence> updateTheCorpus(List<Sentence> input,List<Rule> rules){
        List<MainRule> mains=new ArrayList<>();
        rules.stream().filter((rr) -> (rr.getRuleType()==RuleType.Main)).forEach((rr) -> {
            mains.add((MainRule)rr);
        });
        
        List<Sentence> toDelete=new ArrayList<>();
        
        mains.stream().forEach((mr) -> {
            Sentence se=mr.toSentence();
            if (mr.getReferencesIndexs().size()>=1) {
                input.set(mr.getReferencesIndexs().get(0), se);
                mr.getReferencesIndexs().subList(1, mr.getReferencesIndexs().size()).stream().forEach((ii) -> {
                    toDelete.add(input.get(ii));
                });
            }
        });
        
        return input.stream().filter(x->!toDelete.contains(x)).collect(Collectors.toList());
    }
    
}
