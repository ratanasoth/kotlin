// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

class A
fun A.fn(b: Int): Nothing = TODO()

fun A.run() {
    "".apply { fn(<!TYPE_MISMATCH!>""<!>) }
}