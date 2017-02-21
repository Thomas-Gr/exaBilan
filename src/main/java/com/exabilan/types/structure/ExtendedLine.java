package com.exabilan.types.structure;

import static java.util.Optional.empty;

import java.util.Optional;

import com.exabilan.interfaces.Component;
import com.exabilan.interfaces.FileGenerator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
public final class ExtendedLine implements Component {
    private final Optional<String> line;
    private final boolean isNewLine;
    private final TextStyle style;

    private ExtendedLine(Optional<String> line, boolean isNewLine, TextStyle style) {
        this.line = line;
        this.isNewLine = isNewLine;
        this.style = style;
    }

    public static ExtendedLine createLine(String line) {
        return new ExtendedLine(Optional.of(line), false, null);
    }

    public static ExtendedLine createLine(String line, TextStyle style) {
        return new ExtendedLine(Optional.of(line), false, style);
    }

    public static ExtendedLine createNewLine() {
        return new ExtendedLine(empty(), true, null);
    }

    @Override
    public void accept(FileGenerator fileGenerator) {
        fileGenerator.visit(this);
        fileGenerator.leave(this);
    }
}
