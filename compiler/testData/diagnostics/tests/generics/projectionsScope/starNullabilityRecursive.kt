// !DIAGNOSTICS: -UNUSED_PARAMETER
// !CHECK_TYPE
// !WITH_NEW_INFERENCE
interface A<T : A<T?>?> {
    fun foo(): T?
}
fun testA(a: A<*>) {
    a.foo() checkType { _<A<*>?>() }
}
