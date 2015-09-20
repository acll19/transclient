package com.aca.translators;

import com.aca.translators.mymemory.MyMemoryTranslatorBuilder;

/**
 *
 * @author armando
 */
public class TranslatorBuilders {
    
    public static MyMemoryTranslatorBuilder createMyMemoryTranslatorBuilder() {
        return new MyMemoryTranslatorBuilder();
    }
    
}
