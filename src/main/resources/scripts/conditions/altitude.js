//@Condition()

Coerce = static("Coerce");

key = "altitude";

names = ["高度: (?<min>\\d+)-(/<max>\\d+)", "高度: (?<min>\\d+)"];

function parameters(matcher, text) {
  const min = Coerce.toDouble(matcher.group("min"));
  let max = 10000;
  try {
    max = Coerce.toDouble(matcher.group("max"));
  } catch (ignored) {}
  return mapOf({ min: min, max: max });
}

function condition(entity, map) {
  if (entity == null) return true;
  const min = Coerce.toDouble(map.get("min"));
  let max = 10000;
  try {
    max = Coerce.toDouble(map.get("max"));
  } catch (ignored) {}
  return entity.location.y >= min && entity.location.y <= max;
}
