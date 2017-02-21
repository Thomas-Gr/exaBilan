package com.exabilan.types.exalang;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Value;

@Value
public class ExaLang {
    String name;
    String folderName;
    List<Module> modules;

    @JsonCreator
    public ExaLang(
            @JsonProperty("name") String name,
            @JsonProperty("folderName") String folderName,
            @JsonProperty("module") List<Module> modules) {
        this.name = name;
        this.folderName = folderName;
        this.modules = modules;
    }
}
