//@Condition()

Coerce = static("Coerce");

key = "ground";

names = ["要求在地面", "要求不在地面"];

function parameters(matcher, text) {
  const isIn = !matcher.pattern().toString().contains("不");
  return mapOf({ status: isIn });
}

function condition(entity, map) {
  if (entity == null) return true;
  const isIn = Coerce.toBoolean(map.get("status"));
  return (isIn && entity.isOnGround()) || (!isIn && !entity.isOnGround());
}
