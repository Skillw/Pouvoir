//@Condition()

Coerce = static("Coerce");

key = "water";

names = ["需要在水里", "需要不在水里"];

function parameters(matcher, text) {
  const isIn = !matcher.pattern().toString().contains("不");
  return mapOf({ status: isIn });
}

function condition(entity, map) {
  if (entity == null) return true;
  const isIn = Coerce.toBoolean(map.get("status"));
  var result = true;
  try {
    result =
      (isIn && entity.isInWaterOrRain()) ||
      (!isIn && !entity.isInWaterOrRain());
  } catch (e) {
    print(color("&c您的服务端不兼容 water(需要在水里) 条件! 已自动注销!"));
    AttributeSystem.conditionManager.remove(key);
  }
  return result;
}
