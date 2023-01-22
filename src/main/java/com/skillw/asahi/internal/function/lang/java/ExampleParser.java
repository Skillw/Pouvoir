package com.skillw.asahi.internal.function.lang.java;

import com.skillw.asahi.api.member.lexer.JavaLexer;
import com.skillw.asahi.api.member.parser.prefix.type.JavaTypeParser;
import com.skillw.asahi.api.member.quest.Quester;
import org.jetbrains.annotations.NotNull;

import static com.skillw.asahi.api.ExtensionKt.quester;

//@AsahiParser
public class ExampleParser extends JavaTypeParser<String> {

    @NotNull
    @Override
    protected Quester<String> parse(@NotNull JavaLexer reader) {
        String str = reader.next();
        return quester((context) -> {
            System.out.println(str);
            return str;
        });
    }
}
