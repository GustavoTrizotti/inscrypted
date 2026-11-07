package br.edu.ifsp.scl.core;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TableSlot {
    FIRST,
    SECOND,
    THIRD,
    FOURTH;

    static Set<TableSlot> set() {
        return Arrays.stream(values()).collect(Collectors.toSet());
    }
}
