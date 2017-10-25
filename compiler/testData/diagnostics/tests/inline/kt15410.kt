// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

open class Foo protected constructor()

inline fun foo(f: () -> Unit) = object: Foo() {}

class A : Foo() {
    inline fun foo(f: () -> Unit) = <!PROTECTED_CONSTRUCTOR_NOT_IN_SUPER_CALL!>Foo<!>()
}