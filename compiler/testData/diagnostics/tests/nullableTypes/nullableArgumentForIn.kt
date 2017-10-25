// !WITH_NEW_INFERENCE

fun test(x: Int?) {
     <!TYPE_MISMATCH!>x<!> in 1..2
}