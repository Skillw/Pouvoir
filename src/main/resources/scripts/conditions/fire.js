//@Condition()

Player = find("org.bukkit.entity.Player");
Coerce = static("Coerce");

key = "fire";

names = ["需要在燃烧", "需要不在燃烧"];

function parameters(matcher, text) {
  const isIn = !matcher.pattern().toString().contains("不");
  return mapOf({ status: isIn });
}

function condition(entity, map) {
  if (entity == null) return true;
  const isIn = Coerce.toBoolean(map.get("status"));
  return (isIn && entity.fireTicks > 0) || (!isIn && entity.fireTicks <= 0);
}
