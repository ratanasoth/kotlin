// !DIAGNOSTICS: -UNUSED_VARIABLE -UNUSED_PARAMETER
// !CHECK_TYPE
// !WITH_NEW_INFERENCE

fun <T: Any> bar(a: Array<T>): Array<T?> =  null!!

fun test1(a: Array<out Int>) {
    val r: Array<out Int?> = <!NI;TYPE_MISMATCH!><!NI;UNSUPPORTED!>bar<!>(a)<!>
    val t = <!NI;UNSUPPORTED!>bar<!>(a)
    <!NI;UNSUPPORTED!>t<!> checkType { <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>_<!><!><Array<out Int?>>() }
}

fun <T: Any> foo(l: Array<T>): Array<Array<T?>> = null!!

fun test2(a: Array<out Int>) {
    val r: Array<out Array<out Int?>> = <!NI;TYPE_MISMATCH!><!NI;UNSUPPORTED!>foo<!>(a)<!>
    val t = <!NI;UNSUPPORTED!>foo<!>(a)
    <!NI;UNSUPPORTED!>t<!> checkType { <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>_<!><!><Array<out Array<out Int?>>>() }
}