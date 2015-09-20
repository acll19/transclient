/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aca.translators;

import java.time.LocalDateTime;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author armando
 */
public abstract class Translator {
    
    protected final Client CLIENT = ClientBuilder.newClient();
    private LocalDateTime startTime;
    
    public  abstract String translate(String source) throws TranslationLimitException;
    
     /**
     * @param startTime the startTime to set
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
}
