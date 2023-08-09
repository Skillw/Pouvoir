package com.skillw.pouvoir.util.read;

public interface Parser<T> {
    Result<T> parse(String text);
}
