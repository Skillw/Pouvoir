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
object Abs : PouFunction<Double>("abs") {
    override fun execute(reader: IReader, context: Context): Double? {
        val number = reader.parseDouble(context) ?: return null
        return abs(number)
    }
}
```

亦或者

```javascript
var Coerce = static("Coerce");

//@Function(abs)
function example(reader, context) {
    var number = reader.parseDouble(context)
    if (number == null) return null;
    return abs(number)
}
```

使用:

```kotlin
 println("abs(-1)".parse())
```

打印: 1

## Links

WIKI [http://blog.skillw.com/#sort=pouvoir&doc=README.md](http://blog.skillw.com/#sort=pouvoir&doc=README.md)

JavaDoc [http://doc.skillw.com/pouvoir/](http://doc.skillw.com/pouvoir/)

MCBBS [https://www.mcbbs.net/thread-1221977-1-1.html](https://www.mcbbs.net/thread-1221977-1-1.html)

爱发电 [https://afdian.net/@glom\_](https://afdian.net/@glom_)
