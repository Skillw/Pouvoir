load("plugins/Pouvoir/scripts/core/basic.js");

/**
 * 解析文本中的Asahi 例如:
 * "测试Asahi: {random 0 to 1}"
 * @author Glom
 * @date 2022/12/20
 * @param string 含Asahi的文本
 * @return 解析后的文本
 */

function analysis(string) {
    return analysis(string, "{}");
}

AsahiContext = static("AsahiContext");

/**
 * 解析文本中的Asahi 例如:
 * - "测试Asahi: {random 0 to 1}"
 * @author Glom
 * @date 2022/12/20
 * @param string 含Asahi的文本
 * @param context 上下文
 * @return String 解析后的文本
 */

function analysis(string, json) {
    const context = new AsahiContext();
    context.putAll(mapOf(json));
    return AsahiAPI.analysis(string, context);
}

/**
 * 执行一段Asahi
 *
 * @author Glom
 * @date 2022/12/20
 * @param str Asahi段
 * @param namespaces 命名空间
 * @param json JSON, 上下文
 * @return 结果(可能为null)
 */
function evalAsahi(str, namespaces, json) {
    const context = new AsahiContext();
    context.putAll(mapOf(json));
    return AsahiAPI.asahi(str, arrayOf(namespaces), context);
}

function asahi(str) {
    return evalAsahi(str, ["lang", "common"], {});
}
