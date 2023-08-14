//@Condition()

Coerce = static("Coerce");

Player = find("org.bukkit.entity.Player");
key = "weather";

names = ["需要天气: (?<weather>.*)", "需要不是天气: (?<weather>.*)"];

function parameters(matcher, text) {
  var weather = matcher.group("weather");
  if (weather.contains("晴")) {
    weather = "CLEAR";
  } else {
    weather = "DOWNFALL";
  }
  const isIn = !matcher.pattern().toString().contains("不");
  return mapOf({ weather: weather, status: isIn });
}

function condition(entity, map) {
  if (entity == null || !(entity instanceof Player)) return true;
  const weather = map.get("weather");
  const isIn = map.get("status");
  return (
    (isIn && entity.getPlayerWeather().name().equalsIgnoreCase(weather)) ||
    (!isIn && !entity.getPlayerWeather().name().equalsIgnoreCase(weather))
  );
}
