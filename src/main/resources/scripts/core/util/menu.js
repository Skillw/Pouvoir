load("plugins/Pouvoir/scripts/core/basic.js");
function Basic() {
  /**
   * @description 用抽象符号的排列设置形状
   * @author Glom
   * @date 2022/12/21
   * @param {Array<String>} chars 抽象符号串数组
   */
  function maps(chars) {}

  /**
   * @description 设置行数
   * @author Glom
   * @date 2022/12/21
   * @param {Int} lines 行数
   */
  function rows(lines) {}

  /**
   * @description 设置界面中抽象符号所对应的位置的物品
   * @author Glom
   * @date 2022/12/21
   * @param {Char} slot 抽象符号
   * @param {ItemStack} item 物品/材质(XMaterial)
   */
  function setItem(slot, item) {}

  /**
   * @description 构建界面
   * @author Glom
   * @date 2022/12/21
   * @returns {Inventory} 界面
   */
  function build() {}

  /**
   * @description 构建InventoryHolder
   * @author Glom
   * @date 2022/12/21
   * @param {(Basic) -> MenuHolder} callback 回调函数
   */
  function holder(callback) {}

  /**
   * @description 获取抽象符号所对应的槽位列表(List)
   * @author Glom
   * @date 2022/12/21
   * @param {Char} char 抽象符号
   * @returns {List<Int>} 槽位列表
   */
  function getSlot(char) {}

  /**
   * @description 设置是否锁定玩家手部动作 设置为 true 则将阻止玩家在使用菜单时进行包括但不限于 丢弃物品，拿出菜单物品等行为等行为
   * @author Glom
   * @date 2022/12/21
   * @param {Boolean} bool
   */
  function handLocked(bool) {}

  /**
   * @description 构建时执行回调函数
   * @author Glom
   * @date 2022/12/21
   * @param {Boolean} async 是否异步
   * @param {(Player,Inventory) -> Unit} callback 回调函数
   */
  function onBuild(async, callback) {}

  /**
   * @description 点击事件回调 仅在特定位置下触发
   * @author Glom
   * @date 2022/12/21
   * @param {*} bind 抽象符号
   * @param {function} callback 回调
   */
  function onClick(bind, callback) {}

  /**
   * @description 点击事件回调
   * @author Glom
   * @date 2022/12/21
   * @param {function} callback 回调
   */
  function onClickAll(callback) {}

  /**
   * @description 关闭事件回调
   * @author Glom
   * @date 2022/12/21
   * @param {function} callback 回调函数
   */
  function onClose(callback) {}
}

const BasicMenuBuilder = find(">taboolib.module.ui.type.Basic");
const XMaterial = find(">taboolib.library.xseries.XMaterial");

/**
 * @description 构建基础界面
 * @author Glom
 * @date 2022/12/21
 * @returns {Basic}  Basic对象 (基础界面构建器)
 */
function buildBasic(title) {
  const basic = new Basic();
  if (title == "undefined") title = "title";
  basic.builder = new BasicMenuBuilder(title);
  basic.maps = function (chars) {
    basic.builder.map(chars);
    return basic;
  };
  basic.rows = function (lines) {
    basic.builder.rows(lines);
    return basic;
  };
  basic.setItem = function (slot, item) {
    basic.builder.set(charOf(slot), item);
    return basic;
  };

  basic.build = function () {
    return basic.builder.build();
  };

  basic.holder = function (callback) {
    basic.builder.holder(callback);
    return basic;
  };

  basic.getSlot = function (char) {
    basic.builder.getSlot(char);
    return basic;
  };

  basic.handLocked = function (bool) {
    basic.builder.handLocked(bool);
    return basic;
  };

  basic.onBuild = function (async, callback) {
    basic.builder.onBuild(async, callback);
    return basic;
  };

  basic.onClick = function (bind, callback) {
    basic.builder.onClick(bind, callback);
    return basic;
  };

  basic.onClickAll = function (callback) {
    basic.builder.onClick(callback);
    return basic;
  };

  basic.onClose = function (callback) {
    basic.builder.onClose(callback);
    return basic;
  };

  return basic;
}

ItemStack = find("org.bukkit.inventory.ItemStack");
Material = find("org.bukkit.Material");

//@Awake(Reload)
function example() {
  const menu = buildBasic("我是标题");
  const stone = new ItemStack(Material.valueOf("STONE"));
  const sword = new ItemStack(Material.valueOf("DIAMOND_SWORD"));
  const inv = menu
    .maps(["#########", "#*******#", "#***O***#", "#*******#", "#########"])
    .setItem("#", stone)
    .onClick("#", function (event) {
      event.isCancelled = true;
      event.clicker.sendMessage("你点了石头!");
    })
    .setItem("O", sword)
    .onClick("O", function (event) {
      event.isCancelled = true;
      event.clicker.sendMessage("你点了剑!");
    })
    .build();
  const player = Bukkit.getPlayer("Glom_");
  Tool.task(function (t) {
    player.openInventory(inv);
  });
}
