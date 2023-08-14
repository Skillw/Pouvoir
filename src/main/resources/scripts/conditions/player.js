//@Condition()

Coerce = static("Coerce");

key = "player";

names = ["持有者: (?<player>.*)"];

function parameters(matcher, text) {
  const player = matcher.group("player");
  return mapOf({ player: player });
}

function condition(entity, map) {
  if (entity == null) return true;
  const player = map.get("player");
  return entity.name == player;
}
