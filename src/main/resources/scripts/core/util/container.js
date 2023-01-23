load("plugins/Pouvoir/scripts/core/basic.js");
/** @type {Container} 持久化容器 */
const container = PouvoirContainer.container;

/**
 * 用法:
 *
 * 用户名，键，值 都可自定义，为字符串即可
 * (用户名建议为uuid或玩家名)
 *
 *               用户名 键
 *  database.get(user,key)  - 从容器中获取值
 *                   用户名 键
 *  database.delete(user,key) - 删除容器中的值
 *               用户名 键 值
 *  database.set(user,key,value) - 设置容器中的值
 *
 */
