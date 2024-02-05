# Pouvoir

插件永久免费

---

## 插件

| 说明   | 内容                        |
|------|---------------------------|
| 兼容版本 | 1.9+                      |
| 软依赖  | PlaceholderAPI Mythicmobs |

![GitHub Release](https://img.shields.io/github/v/release/Skillw/Pouvoir)

## 介绍

**Pouvoir** 是基于 **TabooLib VI** 编写的一款类库插件
其包括但不限于以下模块/功能:

- [Asahi](https://github.com/Glom-c/Asahi) - 高速编译执行的动态脚本语言
- [ScriptManager](https://github.com/Glom-c/Pouvoir/tree/master/src/main/kotlin/com/skillw/pouvoir/api/manager/sub/script) -
  拥有健全API的脚本引擎管理器
- [DatabaseManager](https://github.com/Glom-c/Pouvoir/blob/master/src/main/kotlin/com/skillw/pouvoir/api/manager/sub/DatabaseManager.kt) -
  数据库容器管理器
- [TriggerManager](https://github.com/Glom-c/Pouvoir/blob/master/src/main/kotlin/com/skillw/pouvoir/api/manager/sub/TriggerManager.kt) -
  触发器管理器
- [TriggerHandlerManager](https://github.com/Glom-c/Pouvoir/blob/master/src/main/kotlin/com/skillw/pouvoir/api/manager/sub/TriggerHandlerManager.kt) -
  触发器处理器管理器
- [SelectorManager](https://github.com/Glom-c/Pouvoir/blob/master/src/main/kotlin/com/skillw/pouvoir/api/manager/sub/SelectorManager.kt) -
  目标选择器管理器
- [PouPlaceholderAPI](https://github.com/Glom-c/Pouvoir/blob/master/src/main/kotlin/com/skillw/pouvoir/api/manager/sub/PouPlaceholderManager.kt) -
  面向实体的占位符管理器
- [ParticleLib](https://github.com/602723113/ParticleLib) - 由莫老开发，一款强大有趣的特效类库
- [SubPouvoir](https://github.com/Glom-c/Pouvoir/blob/master/src/main/kotlin/com/skillw/pouvoir/api/plugin) -
  解耦的SubPou附属开发框架
  你可以通过编写**代码**/**脚本**来拓展本插件的诸多内容.

---

对于一些可扩展API，**Pouvoir** 提供了脚本拓展
并使用脚本注解进行自动注册注销

详细请见WIKI

#### Asahi 前缀解释器拓展 (Asahi Prefix Parser Extension)

```javascript

//@AsahiPrefix(-name example)
function example(lexer) {
    var numberQuester = lexer.questDouble()
    return result(function (context) {
        var number = numberQuester.get(context)
        print(number)
        return number
    })
}
```

亦或者

```kotlin
@AsahiPrefix(["print", "info"], "lang")
fun info() = prefixParser {
        //开始此函数的"编译"(parse)
        val content = quest<Any>()  //寻求一个任意类型对象
        // result里是执行函数时，要干的事情
        result {
            content.get().also {
                //打印它
                println(it)
            }
        }
    }
```

使用:

```kotlin
 println("example 114.514".asahi())
```

打印: 114.514

## Links

WIKI [http://blog.skillw.com/#sort=pouvoir&doc=README.md](http://blog.skillw.com/#sort=pouvoir&doc=README.md)

JavaDoc [http://doc.skillw.com/pouvoir/](http://doc.skillw.com/pouvoir/)

MCBBS [https://www.mcbbs.net/thread-1221977-1-1.html](https://www.mcbbs.net/thread-1221977-1-1.html)

爱发电 [https://afdian.net/@glom\_](https://afdian.net/@glom_)
