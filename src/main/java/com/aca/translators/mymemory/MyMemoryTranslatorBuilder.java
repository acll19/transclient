/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aca.translators.mymemory;

import com.aca.translators.Translator;

/**
 *
 * @author armando
 */
public class MyMemoryTranslatorBuilder {
    
    MyMemory translator;

    public MyMemoryTranslatorBuilder() {
        this.translator = new MyMemory();
    }
    
    public void withLangPair(String lp) {
        translator.setLangpair(lp);
    }
    
    public void withUserName(String userName) {
        translator.setUserName(userName);
    }
    
    public void withPassword(String password) {
        translator.setPassword(password);
    }
    
    public void withEmail(String de) {
        translator.setDe(de);
    }
    
    public void withApiKey(String apiKey) {
        translator.setApiKey(apiKey);
    }
    
    
    public MyMemory createTranslator() {
        return translator;
    }
    
}
