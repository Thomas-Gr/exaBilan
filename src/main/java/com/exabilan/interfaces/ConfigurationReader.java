package com.exabilan.interfaces;

import java.util.List;

import com.exabilan.types.exalang.Orthophoniste;

/**
 * This reader enable users to provide themselves the configuration they want to be used
 * when generating their document.
 */
public interface ConfigurationReader {

    /**
     * Returns the {@class Orthophoniste} that did the Bilan
     */
    Orthophoniste getOrthophoniste();

    /**
     * Returns the document's header
     */
    List<String> getHeaderLines();

}
