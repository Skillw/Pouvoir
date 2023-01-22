package com.skillw.asahi.internal.function.lang.java;

import com.skillw.asahi.api.annotation.AsahiPrefix;
import com.skillw.asahi.api.member.parser.prefix.namespacing.BaseJavaPrefix;
import com.skillw.asahi.api.member.lexer.JavaLexer;
import com.skillw.asahi.api.member.quest.Quester;
import org.jetbrains.annotations.NotNull;

@AsahiPrefix(names = {"wuhu"}, namespace = "lang")
public class ExampleFunction extends BaseJavaPrefix<Object> {
    @NotNull
    @Override
    protected Quester<Object> parse(@NotNull JavaLexer reader) {
        //编译时
        // 寻求下一个Object
        Quester<Object> quester1 = reader.questAs();
        // 寻求下一个Object
        Quester<Object> quester2 = reader.questObj();
        //返回结果
        return reader.result(context /* 上下文 */ -> {
            //执行时
            //从上下文中取得刚才寻求的Object
            Object obj1 = quester1.get(context);
            Object obj2 = quester2.get(context);
            System.out.println("Invoking wuhu! Obj: " + obj1.toString() + "  " + obj2.toString());
            return obj1;
        });
    }
}
