load("plugins/Pouvoir/scripts/core/basic.js");
/**
 * @description 获取实体视角所对的实体(可能为null)
 * @author Glom
 * @date 2022/12/20
 * @param {LivingEntity} entity 实体
 * @param {Double} distance 距离
 * @returns {LivingEntity}  实体视角所对的实体(可能为null)
 */
function getRayHit(entity, distance) {
  return EntityUtils.getEntityRayHit(entity, distance);
}
