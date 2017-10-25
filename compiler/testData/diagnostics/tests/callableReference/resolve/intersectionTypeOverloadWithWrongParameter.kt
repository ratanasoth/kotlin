// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

import kotlin.reflect.KCallable

class Foo {
    fun <T> installRoute(handler: T) where T : (String) -> Any?, T : KCallable<*> {
    }

    fun <T> installRoute(handler: T) where T : () -> Any?, T : KCallable<*> {
    }

    fun foo() {
        <!OVERLOAD_RESOLUTION_AMBIGUITY!>installRoute<!><Any>(::route)
    }

}

fun route(s: String): Any? = null