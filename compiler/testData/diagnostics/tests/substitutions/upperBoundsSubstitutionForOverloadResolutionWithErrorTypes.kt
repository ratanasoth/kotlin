// !DIAGNOSTICS: -CONFLICTING_JVM_DECLARATIONS -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE
fun <T> f1(l: <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>List1<!><!><T>): T {throw Exception()} // ERROR type here
fun <T> f1(l: <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>List2<!><!><T>): T {throw Exception()} // ERROR type here
fun <T> f1(c: Collection<T>): T{throw Exception()}

fun <T> test(l: List<T>) {
    <!NI;OVERLOAD_RESOLUTION_AMBIGUITY!><!CANNOT_COMPLETE_RESOLVE!>f1<!><!>(l)
}