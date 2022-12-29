load("plugins/Pouvoir/scripts/core/basic.js");
/**
 * @description 根据消息类型与数据构建Messager
 * @author Glom
 * @date 2022/12/20
 * player 玩家(json中的内联函数会基于此玩家解析)
 * type 消息类型(action_bar,boss_bar,chat,holo,title)
 * json 详细参数见下面注释
 * @returns {*} messager (可调用 messager.sendTo([player1,player2,...]))
 */
function buildMessager(player, type, json) {
  const map = mapOf(json);
  const messager = Pouvoir.messagerBuilderManager.build(
    type,
    player,
    function (data) {
      data.putAll(map);
    }
  );
  return messager;
}
/**
 * action_bar:
 *   text 文本          默认为""
 *   stay 持续时间(tick) 默认20tick
 * boss_bar:
 *   text 文本               默认为""
 *   color 颜色BarColor的ID  默认为PURPLE
 *   style 格式BarStyle的ID  默认为SEGMENTED_10
 *   progress 进度(0到1)     默认为1
 * chat:
 *   text 文本 默认 ""
 * holo:
 *   location 坐标    默认为玩家视角位置往上一格
 *   content 文本列表(List)  默认为空列表
 *   stay    持续时间(tick) 默认20tick
 *   time    运动次数       默认-1（关闭）
 *   finalLocation  最终坐标  默认原坐标
 * title:
 *   title    主标题         默认""
 *   subtitle 副标题         默认""
 *   fadeIn   渐入时间(tick) 默认0
 *   stay     持续时间(tick) 默认20
 *   fadeOut  渐出时间(tick) 默认0
 *
 *
 */
