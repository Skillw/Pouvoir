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


EntityFlag = find("com.skillw.pouvoir.api.feature.selector.EntityFlag").INSTANCE
/**
 * @description 为实体添加一个flag(可能为null)
 * @author Clok
 * @date 2024/5/4
 * @param {Object{entity, string, tick?}} entity 实体
 * @returns unit
 */
function addFlag(object) {
  const entity = object["entity"]
  const key = object["key"]
  const tick = object["tick"] || -1
  EntityFlag.addFlag(entity, key, tick)
}

/**
 * @description 为实体删除一个flag
 * @author Clok
 * @date 2024/5/4
 * @param {Entity} entity 实体
 * @param {string} key flag
 * @returns unit
 */
function removeFlag(entity, key) {
  EntityFlag.removeFlag(entity, key)
}

/**
 * @description 获取一个Flag拥有者的实体表
 * @author Clok
 * @date 2024/5/4
 * @param {string} key flag
 * @returns Set<Entity>
 */
function getEntities(key) {
  return EntityFlag.getEntities(key)
}