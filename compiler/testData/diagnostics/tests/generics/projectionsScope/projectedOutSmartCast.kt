// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE
class Inv<E>
class C<R> {
    fun bindTo(property: Inv<R>) {}
}

fun foo(x: Any?, y: C<*>) {
    y.<!MEMBER_PROJECTED_OUT!>bindTo<!>(<!NI;TYPE_MISMATCH!>""<!>)

    if (x is C<*>) {
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.<!MEMBER_PROJECTED_OUT!>bindTo<!>(<!NI;TYPE_MISMATCH!>""<!>)
        with(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>) {
            <!MEMBER_PROJECTED_OUT!>bindTo<!>(<!NI;TYPE_MISMATCH!>""<!>)
        }
    }

    with(x) {
        if (this is C<*>) {
            <!NI;DEBUG_INFO_IMPLICIT_RECEIVER_SMARTCAST!><!MEMBER_PROJECTED_OUT!><!DEBUG_INFO_IMPLICIT_RECEIVER_SMARTCAST!>bindTo<!><!><!>(<!NI;TYPE_MISMATCH!>""<!>)
        }
    }
}
