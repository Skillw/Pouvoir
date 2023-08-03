/**
 * 懒人式数据
 * @param key
 * @param supplier
 * @returns {*}
 */
function lazy(key, supplier) {
    return Tool.lazy(key, supplier)
}

/**
 * 正则
 * @param text
 * @returns {*}
 */
function pattern(text) {
    return Tool.pattern(text)
}

function forEach(iterable, consumer) {
    Tool.forEach(iterable, consumer)
}
