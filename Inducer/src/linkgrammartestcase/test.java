/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package linkgrammartestcase;

//import linkgrammartestcase.linkgrammar.LGConfig;


import linkgrammartestcase.linkgrammar.LGConfig;
import linkgrammartestcase.linkgrammar.LGService;
import linkgrammartestcase.linkgrammar.LinkGrammar;
import linkgrammartestcase.linkgrammar.ParseResult;




/**
 *
 * @author reda
 */
public class test {
    public static void main(String[] args){
        LGConfig config=new LGConfig();
        LGService.init();
        ParseResult pr = LGService.parse(config, "my name is reda");
        int ttt=99;
        //LGService.parse(null, null)
        
        //LinkGrammar.init();
        //LinkGrammar.parse("my name is reda");
        //String str=LinkGrammar.getVersion();
        //boolean te=LinkGrammar.get
        
        //System.out.println(str);
        
        
        
    }
}
