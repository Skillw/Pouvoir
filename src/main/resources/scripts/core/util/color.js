load("plugins/Pouvoir/scripts/core/basic.js");
/**
 * @description 给文本上色 "&6test" -> "§6test"
 * @author Glom
 * @date 2022/12/20
 * @param {*} str 含颜色代码的文本
 * @returns {*} 上色后的文本
 */
function color(str) {
  return ColorUtils.color(str);
}

/**
 * @description 给文本去色 "&6test"/"§6test" -> "test"
 * @author Glom
 * @date 2022/12/20
 * @param {*} str 含颜色代码的文本
 * @returns {*} 去色后的文本
 */
function uncolor(str) {
  return ColorUtils.unColor(str);
}

/**
 * @description 给文本去色，还原未上色的文本 "§6test" -> "&6test"
 * @author Glom
 * @date 2022/12/20
 * @param {*} str 含颜色代码的文本
 * @returns {*} 未上色的文本
 */
function decolor(str) {
  return ColorUtils.decolored(str);
}
