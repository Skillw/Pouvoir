package com.skillw.pouvoir.util.read;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StrTrie<T> implements Parser<T> {
    final Char c;
    private final HashMap<Char, StrTrie<T>> children = new HashMap<>();
    T target = null;

    public StrTrie() {
        this.c = new Char(' ');
    }

    public StrTrie(Char c) {
        this.c = c;
    }

    @Override
    public int hashCode() {
        return this.c.c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StrTrie)) {
            return false;
        }
        StrTrie<?> node = (StrTrie<?>) o;
        return this.c == (node.c);
    }

    public void put(String name, T target) {
        StrTrie<T> node = this;
        int index = 0;
        while (index < name.length()) {
            char ch = name.charAt(index++);
            Char c = new Char(ch);
            node = node.children.computeIfAbsent(c, (cc) -> new StrTrie<>(c));
        }
        node.target = target;
    }

    @Override
    public Result<T> parse(String text) {
        StrTrie<T> node = this;
        int index = 0;
        while (index < text.length()) {
            Char c = new Char(text.charAt(index++));
            StrTrie<T> next = node.children.get(c);
            if (next != null) {
                node = next;
                if (node.target != null && (index == text.length() || !node.children.containsKey(new Char(text.charAt(index))))) {
                    return new Result<>(this, text).setResult(node.target).setStart(index);
                }
            }
        }
        return new Result<>(this, text);
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("char", this.c.c);
        map.put("children", Arrays.asList(this.children.values().stream().map(StrTrie::serialize).toArray()));
        map.put("target", (this.target == null) ? "null" : this.target.toString());
        return map;
    }

    @Override
    public String toString() {
        return this.serialize().toString();
    }
}

