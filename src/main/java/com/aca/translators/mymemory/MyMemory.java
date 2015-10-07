/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aca.translators.mymemory;

import com.aca.jsontools.JsonTools;
import com.aca.translators.TranslationLimitException;
import com.aca.translators.Translator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Level;
import javax.json.JsonObject;

/**
 *
 * @author armando
 */
public class MyMemory extends Translator {

    private String langpair;
    private final String CLIENT_URL = "http://api.mymemory.translated.net";
    private String apiKey;
    private int currentWordCount;
    private String userName;
    private String password;
    private String de;

    public MyMemory(String langpair, String apiKey) {
        this.langpair = langpair;
        this.apiKey = apiKey;
        this.currentWordCount = 1000;
    }

    public MyMemory() {
    }

    public MyMemory(String langpair, String apiKey, String email) {
        this.langpair = langpair;
        this.apiKey = apiKey;
        this.currentWordCount = 1000;
        this.de = email;
    }

    public MyMemory(String langpair, String apiKey, String userName, String passwd) {
        this.langpair = langpair;
        this.apiKey = apiKey;
        this.currentWordCount = 1000;
        this.userName = userName;
        this.password = passwd;
    }

    public void authenticate(String uName, String passwd) {
        String autUrl = CLIENT_URL + "/keygen";

        this.setUserName(uName);
        this.setPassword(passwd);

        String response = CLIENT.target(autUrl)
                .queryParam("user", uName)
                .queryParam("pass", passwd)
                .request().get(String.class);
        JsonObject jsonObject = JsonTools.readObject(response);
        this.setApiKey(jsonObject.getString("key"));
    }

    public void authenticate() {
        String autUrl = CLIENT_URL + "/keygen";

        try {
            String response = CLIENT.target(autUrl)
                    .queryParam("user", this.userName)
                    .queryParam("pass", this.password)
                    .request().get(String.class);
            JsonObject jsonObject = JsonTools.readObject(response);
            this.setApiKey(jsonObject.getString("key"));
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(MyMemory.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public String translate(String source) throws TranslationLimitException {
        if (apiKey == null || apiKey.length() == 0) {
            authenticate();
        }
        if (isWordCountOk(source)) {
            try {
                String transUrl = CLIENT_URL + "/get";
                String response = CLIENT.target(transUrl)
                        .queryParam("q", source)
                        .queryParam("de", this.de)
                        .queryParam("langpair", this.langpair)
                        .queryParam("apiKey", this.apiKey)
                        .request().get(String.class);
                JsonObject jsonObject = JsonTools.readObject(response);

                String traslation = jsonObject.getJsonObject("responseData")
                        .getString("translatedText");
                return traslation;
            } catch (Exception e) {
                throw e;
            }
        }
        this.setLimitReached(true);
        throw new TranslationLimitException("MyMemory API word limit has been reached. Please try again in 24 hrs.");
    }

    private boolean isTimeOk() {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        if (getStartTime() == null) {
            setStartTime(now);
            return true;
        }
        
        LocalDateTime nowMinus24 = now.minusHours(24);
        return !nowMinus24.isAfter(getStartTime());
    }

    private boolean isWordCountOk(String source) {
        boolean hasEmail = this.de != null && de.length() > 0;
        int limit = 10000;
        if (!hasEmail) {
            limit = 1000;
        }

        String[] sourceWords = source.split(" ");
        int sourceWordCount = sourceWords.length;

        if (this.currentWordCount + sourceWordCount <= limit) {
            this.currentWordCount += sourceWordCount;
            return true;
        }

        if (isTimeOk()) {
            this.currentWordCount = limit;
            return true;
        }
        return false;
    }

    /**
     * @param langpair the langpair to set
     */
    public void setLangpair(String langpair) {
        this.langpair = langpair;
    }

    /**
     * @param apiKey the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param de the de to set
     */
    public void setDe(String de) {
        this.de = de;
    }
}
