load("plugins/Pouvoir/scripts/core/basic.js");

/**
 * @description 发送标题
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 * @param {*} title 标题
 * @param {*} subtitle 副标题
 * @param {*} fadeIn 渐入时间(tick)
 * @param {*} stay 持续时间(tick)
 * @param {*} fadeOut 渐出时间(tick)
 */
function sendTitle(player, title, subtitle, fadeIn, stay, fadeOut) {
  PlayerUtils.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
}

/**
 * @description 发送ActionBar
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function sendActionBar(player, text) {
  PlayerUtils.sendActionBar(player, text);
}

/**
 * @description 重置ActionBar
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function resetActionBar(player) {
  PlayerUtils.resetActionBar(player);
}

/**
 * @description 发送BossBar
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 * @param {*} text 文本
 * @param {BarColor} color 颜色
 * @param {BarStyele} style 格式
 * @param {*} progress 进度 (0~1)
 */
function sendBossBar(player, text, color, style, progress) {
  PlayerUtils.sendBossBar(player, text, color, style, progress);
}

/**
 * @description 发送声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 * @param {XSound} sound 音效ID
 */
function playSound(player, sound) {
  PlayerUtils.playSound(player, sound);
}

/**
 * @description 发送点击按钮声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function soundClick(player) {
  PlayerUtils.soundClick(player);
}

/**
 * @description 发送升级声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function soundLevelUp(player) {
  PlayerUtils.soundLevelUp(player);
}

/**
 * @description 发送成就完成声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function soundChallenge(player) {
  PlayerUtils.soundChallenge(player);
}

/**
 * @description 发送成功声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function soundSuccess(player) {
  PlayerUtils.soundSuccess(player);
}

/**
 * @description 发送失败声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function soundFail(player) {
  PlayerUtils.soundFail(player);
}

/**
 * @description 发送(铁砧)完成声音
 * @author Glom
 * @date 2022/12/20
 * @param {*} player 玩家
 */
function soundFinish(player) {
  PlayerUtils.soundFinish(player);
}
