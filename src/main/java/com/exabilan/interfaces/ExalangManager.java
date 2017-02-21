package com.exabilan.interfaces;

import java.io.IOException;
import java.util.Set;

import com.exabilan.types.exalang.ExaLang;

public interface ExalangManager {

    /**
     * Returns the complete set of registered versions of Exalang
     */
    Set<ExaLang> getAllExalangs() throws IOException;

}
