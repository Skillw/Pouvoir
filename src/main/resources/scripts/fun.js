// How to use Function System ?
// Use RPGLib.getFunctionManager().analysis(text)
// Or RPGLib.getFunctionManager().analysis(entity,text) if text contains some PAPI / RPG_PAPI
// For example:
// RPGLib.getFunctionManager().analysis(player,'If(%player_level%,>,10,Your level is bigger than 10!,Your level is lesser than 10...)')
// if your level is bigger than 10 , it will return 'Your level is bigger than 10!' else it will return 'Your level is lesser than 10...'


var symbols = ["<", "<=", "==", "!=", ">", ">=", "equals", "!equals", "contains", "!contains", "equalsIgnoreCase", "!equalsIgnoreCase"]

function hasSymbol(symbol) {
    var bool = false
    for (var index = 0; index < symbols.length; index++) {
        if (symbols[index] == symbol) {
            bool = true
            break
        }
    }
    return bool
}

var HashMap = java.util.HashMap
var functionData = Data.get('functionData');
var requiredData = Data.get('requiredData');

String.prototype.allReplace = function (f, e) {
    var reg = new RegExp(f, "g")
    return this.replace(reg, e)
}

function arrayToStr(array) {
    return "&9[ " + array.toString().allReplace(",", "&5,").allReplace("\\*", "&c*&e") + " &9]"
}

function dataToStr(key) {
    return arrayToStr(functionData.get(key))
}

var Function = Tool.static('com.skillw.pouvoir.api.function.Function')

function reg(key) {
    new Function(key, "fun.js::" + key).register()
}

function regAll() {
    reg("If")
    reg("abs")
    reg("cell")
    reg("decimals")
    reg("floor")
    reg("format")
    reg("rFormat")
    reg("max")
    reg("min")
    reg("random")
    reg("randomInt")
    reg("repeat")
    reg("round")
    reg("sum")
    reg("compute")
}

function init() {
    Data.put('functionData', new HashMap())
    Data.put('requiredData', new HashMap())
    functionData = Data.get('functionData')
    requiredData = Data.get('requiredData')
    functionData.put("If", ["*x", "*Symbol", "*y", "*True Value", "*False Value"])
    requiredData.put("If", 5)
    functionData.put("abs", ["*Value"])
    requiredData.put("abs", 1)
    functionData.put("cell", ["*Value"])
    requiredData.put("cell", 1)
    functionData.put("decimals", ["*Value", "*Decimal Places"])
    requiredData.put("decimals", 2)
    functionData.put("floor", ["*Value"])
    requiredData.put("floor", 1)
    functionData.put("format", ["*Value", "*Format"])
    requiredData.put("format", 2)
    functionData.put("rFormat", ["*Value", "&3Max Integer Places", "&3Min Integer Places", "&3Max Decimal Places", "&3Min Decimal Places"])
    requiredData.put("rFormat", 1)
    functionData.put("max", ["*Value..."])
    requiredData.put("max", 1)
    functionData.put("min", ["*Value..."])
    requiredData.put("min", 1)
    functionData.put("random", ["*Min", "*Max", "&3Max Integer Places", "&3Min Integer Places", "&3Max Decimal Places", "&3Min Decimal Places"])
    requiredData.put("random", 2)
    functionData.put("randomInt", ["*Min", "*Max", "&3Format"])
    requiredData.put("randomInt", 2)
    functionData.put("repeat", ["*Times", "*Value"])
    requiredData.put("repeat", 2)
    functionData.put("round", ["*Value"])
    requiredData.put("round", 1)
    functionData.put("sum", ["*Value..."])
    requiredData.put("sum", 2)
    functionData.put("compute", ["*Formula", "&3Variables(name=value)..."])
    requiredData.put("compute", 1)
    regAll()
}

function wrong(text) {

    try {
        MessageUtils.sendMessage(sender, RPGLib.configManager.getPrefix() + text)
        return
    } catch (e) {

    }
    MessageUtils.sendWrong(text)
}

function lessArgs(key, length) {
    var required = requiredData.get(key)
    if (length < required) {
        wrong("&cThe function &6" + key + " &crequires at least &b" + required + " &carguments " + dataToStr(key) + " &c!")
        return true
    }
    return false
}

var JMath = java.lang.Math

// If(1,==,1,yes,no)  - > return "yes"
function If() {
    var InventoryClickEvent = Packages.org.bukkit.event.inventory.InventoryClickEvent
    Tool.addListener("任意字符串(注销监听器用)", "org.bukkit.event.inventory.InventoryClickEvent", "NORMAL", false, function (event) {
        //代码
    })
    var key = "If"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var x = args[0]
    var symbol = args[1]
    var y = args[2]
    var ifTrue = args[3]
    var ifFalse = args[4]

    function handleIf(x, symbol, y) {
        if (!hasSymbol(symbol)) {
            wrong("&cThe Symbol of the function &6If &ccan only be one of " + arrayToStr(symbols) + " &c!")
            return null
        }
        switch (symbol) {
            case "<":
                return x < y
            case "<=":
                return x <= y
            case "==":
                return x == y
            case "!=":
                return x != y
            case ">":
                return x > y
            case ">=":
                return x >= y
            case "equals":
                return x.equals(y)
            case "!equals":
                return !x.equals(y)
            case "contains":
                return x.contains(y)
            case "!contains":
                return !x.contains(y)
            case "equalsIgnoreCase":
                return x.equalsIgnoreCase(y)
            case "!equalsIgnoreCase":
                return !x.equalsIgnoreCase(y)
        }
    }

    return handleIf(x, symbol, y) ? ifTrue : ifFalse
}

function checkNumber(str) {
    try {
        var num = Number(str)
        return num != "NaN"
    } catch (e) {
        return false
    }
}

// abs(-10)  - > 10
function abs() {
    var key = "abs"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    if (checkNumber(value)) {
        wrong("The Value of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    return Math.abs(value)
}

// cell(10.2)  - > 11
function cell() {
    var key = "cell"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    if (checkNumber(value)) {
        wrong("The Value of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    return Math.cell(value)
}

// decimals(10,2)  - > 10.00
function decimals() {
    var key = "decimals"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    if (checkNumber(value)) {
        wrong("The Value of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    var decimal = args[1]
    if (checkNumber(value)) {
        wrong("The Decimal Places of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    return NumberUtils.format(value, -1, 0, decimal, decimal)
}

// floor(10.9)  - > 10
function floor() {
    var key = "floor"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    if (checkNumber(value)) {
        wrong("The Value of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    return Math.floor(value)
}

// format(10.11,##.0)  - > 10.1
function format() {
    var key = "format"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    if (checkNumber(value)) {
        wrong("The Value of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    var format = args[1]
    return NumberUtils.format(value, format)
}

// rFormat(10)  - > 10.00 (default config.yml)
// rFormat(10,-1,-1,3,3) - > 10.000
function rFormat() {
    var key = "rFormat"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    for (var i = 0; i < args.length; i++) {
        if (checkNumber(args[i])) {
            wrong("The Arguments of function &6" + key + " &c can only be number!")
            return "wrong"
        }
    }
    if (args.length == 5) {
        var integerMax = args[1]
        var integerMin = args[2]
        var decimalMax = args[3]
        var decimalMin = args[4]
        return NumberUtils.format(value, integerMax, integerMin, decimalMax, decimalMin)
    } else {
        return NumberUtils.format(value)
    }
}

// max(1,2)  - > 2
// max(1,3,4,2,10) - > 10
function max() {
    var key = "max"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var index = 0;
    for (var i = index; i < args.length; i++) {
        if (checkNumber(args[i])) {
            wrong("The Arguments of function &6" + key + " &c can only be number!")
            return "wrong"
        }
    }
    var max = args[0];
    for (var i = index; i < args.length; i++) {
        if (i + 1 < args.length && max < args[i + 1]) {
            index = i + 1;
            max = args[i + 1];
        }
    }
    return max
}

// min(1,2)  - > 1
// min(1,0,2,-1) - > -1
function min() {
    var key = "min"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var index = 0;
    for (var i = index; i < args.length; i++) {
        if (checkNumber(args[i])) {
            wrong("The Arguments of function &6" + key + " &c can only be number!")
            return "wrong"
        }
    }
    var min = args[0];
    for (var i = index; i < args.length; i++) {
        if (i + 1 < args.length && min > args[i + 1]) {
            index = i + 1;
            min = args[i + 1];
        }
    }
    return min
}

// random(0,1)  - > 0.21 (default config.yml)
// random(0,1,-1,-1,-1,-1) - > 0.12398483274
function random() {
    var key = "random"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    for (var i = 0; i < args.length; i++) {
        if (checkNumber(args[i])) {
            wrong("The Arguments of function &6" + key + " &c can only be number!")
            return "wrong"
        }
    }
    var min = args[0]
    var max = args[1]
    if (args.length == 6) {
        var integerMax = args[2]
        var integerMin = args[3]
        var decimalMax = args[4]
        var decimalMin = args[5]
        return NumberUtils.random(min, max, integerMax, integerMin, decimalMax, decimalMin)
    }
    return NumberUtils.random(min, max)
}

// randomInt(1,6)  - > 2
function randomInt() {
    var key = "randomInt"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var min = args[0]
    if (checkNumber(min)) {
        wrong("The Min of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    var max = args[1]
    if (checkNumber(max)) {
        wrong("The Max of function &6" + key + " &c can only be number!")
        return "wrong"
    }

    if (args.length == 3) {
        var format = args[2]
        return NumberUtils.format(NumberUtils.randomInt(min, max), format)
    }
    return NumberUtils.randomInt(min, max)
}

// repeat(5,a)  - > aaaaa
function repeat() {
    var key = "repeat"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var times = args[0]
    if (checkNumber(times)) {
        wrong("The Times of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    var value = args[1]
    var result = null
    if (value instanceof Number)
        result = 0
    else
        result = ""
    for (var time = 0; time < times; time++) {
        result = result + value
    }
    return result
}

// round(10.4)  - > 10
function round() {
    var key = "round"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var value = args[0]
    if (checkNumber(value)) {
        wrong("The Value of function &6" + key + " &c can only be number!")
        return "wrong"
    }
    return Math.round(value)
}

// sum(1,2)  - > 3
// sum(1,23,2) - > 26
function sum() {
    var key = "sum"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var total = null
    if (args[0] instanceof Number) {
        total = 0
    } else {
        total = ""
    }
    for (var index = 0; index < args.length; index++) {
        total = total + args[index]
    }

    return total
}

var Formula = Tool.static('com.skillw.pouvoir.api.formula.Formula')

// compute(a+(b/10),a=%player_level%,b=8) - > 5.8 (if player's level = 5)
function compute() {
    var key = "compute"
    if (lessArgs(key, args.length)) {
        return "wrong"
    }
    var formulaStr = args[0]
    try {
        var formula = new Formula(formulaStr, entity)
        if (args.length > 1)
            for (var i = 1; i < args.length; i++) {
                var strs = args[i].split("=")
                formula = formula.replace(strs[0], strs[1])
            }
        return formula.result()
    } catch (e) {

    }
    if (args.length > 1)
        for (var i = 1; i < args.length; i++) {
            var strs = args[i].split("=")
            formulaStr = formulaStr.replace(strs[0], strs[1])
        }
    return CalculationUtils.getResult(formulaStr)
}


