lookup = {M: 1000, CM: 900, D: 500, CD: 400, C: 100, XC: 90, L: 50, XL: 40, X: 10, IX: 9, V: 5, IV: 4, I: 1}

function toInt(roman) {
    let i, num = 0;
    for (i in lookup) {
        while (roman.indexOf(i) === 0) {
            num += lookup[i];
            roman = roman.replace(i, '');
        }
    }
    return num;
}

function toRoman(num) {
    let roman = '';
    for (let i in lookup) {
        while (num >= lookup[i]) {
            roman += i;
            num -= lookup[i];
        }
    }
    return roman;
}

//@StringOperation(roman_num)
function romanNum(a, b) {
    return toNum(toInt(a) + toInt(b));
}
