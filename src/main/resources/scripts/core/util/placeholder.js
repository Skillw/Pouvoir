load("plugins/Pouvoir/scripts/core/basic.js");
PouvoirAPI = static("PouvoirAPI");
/**
 * @description 解析占位符(PouPAPI & PAPI)
 * @author Glom
 * @date 2022/12/20
 * @param {String} string 待解析的文本
 * @param {LivingEntity} entity 实体
 * @returns {String} 解析后的文本
 */
function placeholder(string, entity) {
  return  PouvoirAPI.placeholder(string, entity,true);
}
