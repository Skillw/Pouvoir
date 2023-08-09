package com.skillw.pouvoir.util.read;

public class Result<T> {
    private final Parser<T> root;
    private final String all;
    public T result = null;
    int start;

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

    public Result<T> setStart(int start) {
        this.start = start;
        return this;
    }

    public Result<T> parse() {
        return this.root.parse(this.all.substring(this.start));
    }


}
