// !DIAGNOSTICS: -SUPERTYPES_FOR_ANNOTATION_CLASS -VIRTUAL_MEMBER_HIDDEN -FINAL_SUPERTYPE
// These errors need to be suppressed to cause light class generation
// FILE: test.kt

annotation class Ann : Target()

annotation class Ann2(vararg val allowedTargets: AnnotationTarget) : Target()

interface I : J
class C : I
annotation class Ann3 : C()
annotation class Ann4 : I

// FILE: J.java

import kotlin.annotation.Target;

public interface J extends Target {
}
