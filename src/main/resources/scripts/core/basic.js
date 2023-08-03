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
    return Tool.sync(func);
}

/**
 * @description 同步执行(下一tick)
 * @author Glom
 * @date 2022/12/20
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function task(func) {
    Tool.task(func);
}

/**
 * @description 异步执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskAsync(func) {
    Tool.taskAsync(func);
}

/**
 * @description 同步延迟执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} delay 延迟(tick)
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskLater(delay, func) {
    Tool.taskLater(delay, func);
}

/**
 * @description 异步延迟执行
 * @author Glom
 * @date 2022/12/20
 * @param {*} delay 延迟(tick)
 * @param {*} func 执行内容 含一个参数task 可调用task.cancel()
 */
function taskAsyncLater(delay, func) {
    Tool.taskAsyncLater(delay, func);
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
    Tool.taskTimer(delay, period, func);
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
    Tool.taskAsyncTimer(delay, period, func);
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
    const obj = JSON.parse(json);
    for (var key in obj) {
        map.put(key, obj[key]);
    }
}
