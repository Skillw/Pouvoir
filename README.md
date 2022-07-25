# Pouvoir

插件永久免费

---

## 插件

| 说明     | 内容                      |
| -------- | ------------------------- |
| 兼容版本 | 1.12.2+               |
| 软依赖   | PlaceholderAPI Mythicmobs |

## 介绍

**Pouvoir** 是基于 **TabooLib VI** 编写的一款多线程脚本引擎插件

你可以通过编写**代码**/**脚本**来拓展本插件的诸多内容.

### 包括但不限于

#### 脚本顶级成员 (Script Top Level Function)

```kotlin
    @ScriptTopLevel
@JvmStatic
fun String.placeholder(entity: LivingEntity): String {
    return Pouvoir.pouPlaceHolderAPI.replace(entity, this)
}
```

(支持Class,Static Field,Static Method)   
使用:

```javascript
placeholder(["%as_att:PhysicalDamage_value%", entity])
```

#### "abs function" 字符串内联函数拓展 (Inline String Function Extension)

```kotlin
@AutoRegister
object Abs : PouFunction("abs") {
    override fun predicate(args: Array<String>): Boolean {
        return args.size == 1 && Coerce.asDouble(args[0]).isPresent
    }

    override fun function(args: Array<String>): Any? {
        val number = Coerce.toDouble(args[0])
        return abs(number)
    }
}
```

亦或者

```javascript
var Coerce = static("Coerce");

//@Function(plus)
function example(args) {
    if (args.length < 2) return "wrong-arguments";
    var a = Coerce.toDouble(args[0]);
    var b = Coerce.toDouble(args[1]);
    return a + b;
}
```

使用:

```kotlin
 println("plus(1,2)".analysis())
```

打印: 3

## Links

WIKI [http://blog.skillw.com/#sort=pouvoir&doc=README.md](http://blog.skillw.com/#sort=pouvoir&doc=README.md)

JavaDoc [http://doc.skillw.com/pouvoir/](http://doc.skillw.com/pouvoir/)

MCBBS [https://www.mcbbs.net/thread-1221977-1-1.html](https://www.mcbbs.net/thread-1221977-1-1.html)

爱发电 [https://afdian.net/@glom\_](https://afdian.net/@glom_)
