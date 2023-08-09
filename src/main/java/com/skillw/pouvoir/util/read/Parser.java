package com.skillw.pouvoir.util.read;

import org.jetbrains.annotations.NotNull;

public interface Parser<T> {
    @NotNull
    Result<T> parse(String text);
}
