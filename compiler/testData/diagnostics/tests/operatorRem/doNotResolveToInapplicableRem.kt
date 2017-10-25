// !DIAGNOSTICS: -UNUSED_PARAMETER
// !WITH_NEW_INFERENCE

object OldMod {
    <!DEPRECATED_BINARY_MOD!>operator<!> fun mod(x: Int) {}
}

object RemExtension
operator fun RemExtension.rem(x: Int) {}

fun foo() {
    OldMod <!DEPRECATED_BINARY_MOD_AS_REM!>%<!> 123
}