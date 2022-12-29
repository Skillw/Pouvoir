load("plugins/Pouvoir/scripts/core/basic.js");

TextHandler = find("com.skillw.pouvoir.internal.core.function.TextHandler");
SimpleContext = find(
  "com.skillw.pouvoir.internal.core.function.context.SimpleContext"
);
/**
 * 解析文本中的内联函数 例如:
 * "测试内联函数: {random 0 to 1}"
 * @author Glom
 * @date 2022/12/20
 * @param string 含内联函数的文本
 * @return 解析后的文本
 */

function analysis(string) {
  return TextHandler.analysis(string);
}

/**
 * 解析文本中的内联函数 例如:
 * - "测试内联函数: {random 0 to 1}"
 * @author Glom
 * @date 2022/12/20
 * @param string 含内联函数的文本
 * @param context 上下文
 * @return String 解析后的文本
 */

function analysis(string, json) {
  const context = new SimpleContext(mapOf(json));
  return TextHandler.analysis(string, context);
}

/**
 * 执行一段内联函数
 *
 * @author Glom
 * @date 2022/12/20
 * @param str 内联函数段
 * @param context 上下文
 * @return 结果(可能为null)
 */
function evalInline(str) {
  const context = new SimpleContext(mapOf(json));
  return Pouvoir.pouFunctionManager.eval(str, context);
}
