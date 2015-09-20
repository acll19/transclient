package com.aca.translators.yandex;

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
public class Yandex extends Translator {

    private String lang;
    private String apiKey;
    private int limit;

    private final String CLIENT_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    public Yandex(String lang, String apiKey) {
        this.lang = lang;
        this.apiKey = apiKey;
    }

    @Override
    public String translate(String source) throws TranslationLimitException {
        if (isCountOk(source)) {
            try {
                String response = CLIENT.target(CLIENT_URL)
                        .queryParam("key", this.getApiKey())
                        .queryParam("lang", this.lang)
                        .queryParam("text", source)
                        .request()
                        .get(String.class);
                JsonObject jsonObject = JsonTools.readObject(response);

                String traslation = jsonObject.getJsonArray("text").getString(0);
                return traslation;
            } catch (Exception e) {
                java.util.logging.Logger.getLogger(Yandex.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        throw new TranslationLimitException("Yandex limit has been reached. Please try again in 24 hrs.");
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String ak) {
        this.apiKey = ak;
    }

    public void setLang(String newLang) {
        this.lang = newLang;
    }

    /**
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    private boolean isTimeOk() {
        if (getStartTime() == null) {
            setStartTime(LocalDateTime.now(ZoneId.systemDefault()));
            return true;
        }
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime nowMinus24 = now.minusHours(24);
        return !nowMinus24.isAfter(getStartTime());
    }

    private boolean isCountOk(String source) {
        int count = 10000;
        if (this.limit == 0) {
            limit = 1000;
        }

        if (count - source.length() < this.limit) {
            this.limit -= source.length();
            return true;
        }

        if (isTimeOk()) {
            this.limit = count;
            return true;
        }
        return false;
    }
}
