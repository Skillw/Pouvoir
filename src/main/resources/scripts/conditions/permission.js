//@Condition()

Player = find("org.bukkit.entity.Player");

key = "gm";

names = ["权限(:|：)?\\s?(?<permission>.*)"];

function parameters(matcher, text) {
  const permission = matcher.group("permission");
  return mapOf({ permission: permission });
}

function condition(entity, map) {
  if (entity == null || !(entity instanceof Player)) return true;
  const permission = map.get("permission").toString();
  return entity.hasPermission(permission);
}
