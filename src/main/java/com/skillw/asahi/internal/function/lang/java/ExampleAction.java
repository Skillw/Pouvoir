package com.skillw.asahi.internal.function.lang.java;

import com.skillw.asahi.api.annotation.AsahiInfix;
import com.skillw.asahi.api.member.parser.infix.namespacing.BaseInfix;

@AsahiInfix
public class ExampleAction extends BaseInfix<String> {
    /**
     * 使用方法:
     * set str to "123"
     * str printStr
     */
    public ExampleAction() {
        super(String.class, "common");
        this.infix(new String[]{"printStr"}, (context, str) -> {
            System.out.println(str);
            return str;
        });
    }
}
