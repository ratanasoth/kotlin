// !WITH_NEW_INFERENCE

// FILE: KotlinFile.kt
fun foo(javaClass: JavaClass) {
    javaClass.doSomething(<!NI;NAMED_ARGUMENTS_NOT_ALLOWED!><!NAMED_ARGUMENTS_NOT_ALLOWED!>p<!><!> = 1) <!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>{
        bar()
    }<!><!>
}

fun bar(){}

// FILE: JavaClass.java
public class JavaClass {
    public void doSomething(int p, Runnable runnable) { runnable.run(); }
}