// !DIAGNOSTICS: -UNUSED_PARAMETER, -UNUSED_VARIABLE
// !WITH_NEW_INFERENCE

object TestClass {
    inline operator fun <T> invoke(task: () -> T) = task()
}

fun test(s: String): String {
    val a = TestClass { TestClass { TestClass } }
    a checkType { _<TestClass>() }

    <!UNREACHABLE_CODE!>val b =<!> TestClass { return s }
    <!UNREACHABLE_CODE!>b checkType { _<Nothing>() }<!>
}