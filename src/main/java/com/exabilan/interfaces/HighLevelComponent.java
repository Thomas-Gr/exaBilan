package com.exabilan.interfaces;

import com.google.common.collect.ImmutableList;

import com.exabilan.types.exalang.Bilan;

/**
 * Represents high level sections for the generated document
 */
public interface HighLevelComponent {

    /**
     * Generates content associated to this component
     */
    ImmutableList<Section> generateItem(Bilan bilan);

}
