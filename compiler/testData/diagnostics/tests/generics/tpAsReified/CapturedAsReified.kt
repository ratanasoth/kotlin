// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE
class A<F>

inline fun <reified T> foo(x: A<T>) {}

fun test(x: A<out CharSequence>) {
    <!REIFIED_TYPE_FORBIDDEN_SUBSTITUTION!>foo<!>(x)
}
