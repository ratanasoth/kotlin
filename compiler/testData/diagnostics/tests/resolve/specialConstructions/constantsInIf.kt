// !DIAGNOSTICS: -USELESS_ELVIS
// !WITH_NEW_INFERENCE

fun test() {
    bar(if (true) {
        <!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>
    } else {
        <!CONSTANT_EXPECTED_TYPE_MISMATCH!>2<!>
    })

    bar(<!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!> ?: <!CONSTANT_EXPECTED_TYPE_MISMATCH!>2<!>)
}

fun bar(s: String) = s