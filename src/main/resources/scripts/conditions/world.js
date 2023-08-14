//@Condition()

Coerce = static("Coerce");

key = "world";

names = ["要求世界: (?<world>.*)", "需要不是世界: (?<world>.*)"];

function parameters(matcher, text) {
  const world = matcher.group("world");
  const isIn = !matcher.pattern().toString().contains("不");
  return mapOf({ world: world, status: isIn });
}

function condition(entity, map) {
  if (entity == null) return true;
  const world = map.get("world");
  const isIn = map.get("status");
  return (
    (isIn && entity.location.world.name.equalsIgnoreCase(world)) ||
    (!isIn && !entity.location.world.name.equalsIgnoreCase(world))
  );
}
