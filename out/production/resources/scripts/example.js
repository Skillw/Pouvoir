// //@Awake(Reload)
// function example() {
//     print("Beep! Pouvoir is reloading!")
//     print("             -- example.js")
// }

var Coerce = com.skillw.pouvoir.taboolib.common5.Coerce

//@Function(abs)
function abs() {
    var number = Coerce.toDouble(args[0])
    return Math.abs(number)
}