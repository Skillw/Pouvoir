package com.skillw.pouvoir.util.read;

public class Char {
    final char c;

    public Char(char c) {
        this.c = c;
    }

    @Override
    public int hashCode() {
        return this.c;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Char) && ((Char) o).c == this.c;
    }
}
