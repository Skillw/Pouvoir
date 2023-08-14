//@Condition()

Player = find("org.bukkit.entity.Player");
Coerce = static("Coerce");

key = "food";

names = ["要求饱食度: (?<value>\\d+)"];

function parameters(matcher, text) {
  const value = Coerce.toInteger(matcher.group("value"));
  return mapOf({ value: value });
}

function condition(entity, map) {
  if (entity == null || !(entity instanceof Player)) return true;
  const level = Coerce.toInteger(map.get("value"));
  return entity.foodLevel >= level;
}
