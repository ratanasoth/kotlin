// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE
open class Super<T> {
    inner open class Inner {
    }
}

class Sub : Super<String>() {
    inner class SubInner : Super<String>.Inner {
        constructor()
        constructor(x: Int) : super() {}
    }
}
