// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

fun String.invoke(i: Int) {}

fun foo(i: Int) {
    <!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>i<!>(1)

    <!FUNCTION_EXPECTED!>1<!>(1)
}