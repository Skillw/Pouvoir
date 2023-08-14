//@Condition()

Coerce = static("Coerce");

key = "health";

names = ["生命值需要: (?<min>\\d+)-(?<max>\\d+)", "生命值需要: (?<min>\\d+)"];

function parameters(matcher, text) {
  const min = Coerce.toDouble(matcher.group("min"));
  let max = min;
  try {
    max = Coerce.toDouble(matcher.group("max"));
  } catch (ignored) {}
  return mapOf({ min: min, max: max });
}

function condition(entity, map) {
  if (entity == null) return true;
  const min = Coerce.toDouble(map.get("min"));
  let max = min;
  try {
    max = Coerce.toDouble(map.get("max"));
  } catch (ignored) {}
  return entity.health >= min && entity.health <= max;
}
