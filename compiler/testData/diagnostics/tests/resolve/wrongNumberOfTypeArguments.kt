// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

fun <T> foo(t: T) = t

fun test1() {
    foo<!WRONG_NUMBER_OF_TYPE_ARGUMENTS!><Int, String><!>(<!TYPE_MISMATCH!>""<!>)
}


fun <T, R> bar(t: T, r: R) {}

fun test2() {
    bar<!WRONG_NUMBER_OF_TYPE_ARGUMENTS!><Int><!>(<!TYPE_MISMATCH!>""<!>, "")
}