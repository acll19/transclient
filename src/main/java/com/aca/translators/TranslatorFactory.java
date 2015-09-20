package com.aca.translators;

import com.aca.translators.mymemory.MyMemory;
import com.aca.translators.mymemory.MyMemoryTranslatorBuilder;
import com.aca.translators.yandex.Yandex;

/**
 *
 * @author armando
 */
public class TranslatorFactory {

    protected String[] args;

    public TranslatorFactory(String[] args) {
        this.args = args;
    }

    public MyMemory createMyMemory() {
        MyMemoryTranslatorBuilder builder = new MyMemoryTranslatorBuilder();
        for (int i = 2; i < args.length;) {
            if (args[i].equals("--apiKey")) {
                builder.withApiKey(args[++i]);
                continue;
            }

            if (args[i].equals("-e")) {
                builder.withEmail(args[++i]);
                continue;
            }

            if (args[i].equals("-u")) {
                builder.withUserName(args[++i]);
                continue;
            }

            if (args[i].equals("-p")) {
                builder.withPassword(args[++i]);
                continue;
            }

            if (args[i].equals("-lp")) {
                builder.withLangPair(args[++i]);
            }
            i++;
        }
        return builder.createTranslator();
    }

    public Yandex createYandex() {
        String apiKey = "";
        String lang = "";
        for (int i = 2; i < args.length;) {
            if (args[i].equals("--apiKey")) {
                apiKey = args[++i];
                continue;
            }
            if (args[i].equals("-l")) {
                lang = args[++i];
                continue;
            }
            i++;
        }
        return new Yandex(lang, apiKey);
    }

}
