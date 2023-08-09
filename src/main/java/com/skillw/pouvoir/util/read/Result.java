package com.skillw.pouvoir.util.read;

import org.jetbrains.annotations.NotNull;

public class Result<T> {
    public T result = null;
    public int start;
    public int end;
    public String all = null;
    private Parser<T> root = null;

    public Result() {
    }

    public Result(Parser<T> root) {
        this.root = root;
    }

    public Result(Parser<T> root, String all) {
        this.root = root;
        this.all = all;
    }

    public boolean matched() {
        return this.result != null;
    }


    public Result<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public Result<T> setRange(int start, int end) {
        this.start = start;
        this.end = end;
        return this;
    }

    @NotNull
    public Result<T> parse() {
        if (this.root == null || this.all == null) {
            return new Result<>();
        }
        return this.root.parse(this.all.substring(this.end));
    }


}
