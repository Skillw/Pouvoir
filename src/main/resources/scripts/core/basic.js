/**
 * @description 通过全限定类名（完整类路径）来获取静态类
 * @author Glom
 * @date 2022/12/20
 * @param {*} className 类名
 * @returns {*} 静态类
 */
function find(className) {
    return ClassUtils.find(className);
}

/**
 * @description 通过非限定类名（类名）来获取静态类 注意！ 此类名必须唯一!
 * @author Glom
 * @date 2022/12/20
 * @param {*} className 类名
 * @returns {*} 静态类
 */
function static(className) {
    return ClassUtils.staticClass(className);
}

/**
 * @description 快速跳回同步执行，并返回结果
 * @author Glom
 * @date 2022/12/20
 * @param {*} func 执行内容
 * @return {*} any 执行结果
 */
function sync(func) {
    return Tool.sync(function () {
        func()
    });
}

/**
 * @description 同步执行(下一tick)
 * @author Glom
 * @date 2022/12/20
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function task(func) {
    Tool.task(function (task) {
        func(task)
    });
}

/**
 * @description 异步执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskAsync(func) {
    Tool.taskAsync(function (task) {
        func(task)
    });
}

/**
 * @description 同步延迟执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} delay 延迟(tick)
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskLater(delay, func) {
    Tool.taskLater(delay, function (task) {
        func(task)
    });
}

/**
 * @description 异步延迟执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} delay 延迟(tick)
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskAsyncLater(delay, func) {
    Tool.taskAsyncLater(delay, function (task) {
        func(task)
    });
}

/**
 * @description 同步定期执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} delay 延迟(tick)
 * @param {*} period 周期(tick)
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskTimer(delay, period, func) {
    Tool.taskTimer(delay, period, function (task) {
        func(task)
    });
}

/**
 * @description 异步定期执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} delay 延迟(tick)
 * @param {*} period 周期(tick)
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskAsyncTimer(delay, period, func) {
    Tool.taskAsyncTimer(delay, period, function (task) {
        func(task)
    });
}
/**
 * @description 同步定期执行,指定次数
 * @author Clok
 * @date 2024/5/4
 * @param {*} delay 延迟(tick)
 * @param {*} period 周期(tick)
 * @param {*} time 次数(int)
 * @param {*} func 执行内容 含一个参数task, time 可调用task.cancel(),times表示当前次数
 */
function taskSyncTimerTime(delay, period, time, func) {
    let times = 0
    taskTimer(delay, period, function (task) {
        times++
        func(task, times)
        if (times >= time) {
            task.cancel()
        }
    })
}
/**
 * @description 异步定期执行,指定次数
 * @author Clok
 * @date 2024/5/4
 * @param {*} delay 延迟(tick)
 * @param {*} period 周期(tick)
 * @param {*} time 次数(int)
 * @param {*} func 执行内容 含一个参数task, time 可调用task.cancel(),times表示当前次数
 */
function taskAsyncTimerTime(delay, period, time, func) {
    let times = 0
    taskAsyncTimer(delay, period, function (task) {
        times++
        func(task, times)
        if (times >= time) {
            task.cancel()
        }
    })
}

JavaArray = Java.type("java.lang.Object[]");

/**
 * @description 构建Java Array
 * @author Glom
 * @date 2023/1/5
 * @param {*} array js数组
 * @returns {*}  Java数组
 */
function arrayOf(array) {
    return Java.to(array, JavaArray)
}

/**
 * @description 构建Java List
 * @author Glom
 * @date 2022/12/20
 * @param {*} array js数组
 * @returns {*}  Java的List
 */
function listOf(array) {
    const list = new java.util.ArrayList();
    for (let i in array) {
        list.add(array[i]);
    }
    return list;
}

/**
 * @description 构建Java Map
 * @author Glom
 * @date 2022/12/20
 * @param {*} json JSON
 */
function mapOf(json) {
    const map = new java.util.HashMap();
    for (var key in json) {
        map.put(key, json[key]);
    }
    return map;
}

