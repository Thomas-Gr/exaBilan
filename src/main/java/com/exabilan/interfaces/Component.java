package com.exabilan.interfaces;

/**
 * Represents a displayable component for the generated word document
 */
public interface Component {

    /**
     * Defines behavior when being visited by a {@class FileGenerator}
     */
    void accept(FileGenerator fileGenerator);

}
