load("plugins/Pouvoir/scripts/core/basic.js");
/**
 * @description 格式化数字
 * @author Glom
 * @date 2022/12/20
 * @param {*} number 数字
 * @param {*} format 格式
 * @returns {*}  格式化后的数字文本
 */
function format(number, format) {
  return NumberUtils.format(number, format);
}

/**
 * @description 计算公式并返回结果
 * @author Glom
 * @date 2022/12/20
 * @param {*} formula 公式
 * @param {*} entity 实体
 * @returns {BigDecimal} 计算结果
 */
function calculate(formula, entity) {
  return CalculationUtils.calculate(formula, entity, null);
}

/**
 * @description 计算公式并返回结果
 * @author Glom
 * @date 2022/12/20
 * @param {*} formula 公式
 * @param {*} entity 实体
 * @returns {Double} 计算结果
 */
function calculateDouble(formula, entity) {
  return CalculationUtils.calculateDouble(formula, entity, null);
}
