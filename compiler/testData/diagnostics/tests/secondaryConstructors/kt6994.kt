// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE
class X<T> {
    constructor(t: T, i: Int): this(<!TYPE_MISMATCH!>i<!>, 1)
}
