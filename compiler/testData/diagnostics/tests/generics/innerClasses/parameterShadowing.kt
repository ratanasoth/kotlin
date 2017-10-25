// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_EXPRESSION -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

class Outer<E> {
    inner class Inner<E> {
        fun foo(): E = null!!
        fun outerE() = baz()
    }

    fun baz(): E = null!!
}

fun main() {
    val inner = Outer<String>().Inner<Int>()

    inner.foo().checkType { _<Int>() }
    inner.outerE().checkType { _<String>() }
}
