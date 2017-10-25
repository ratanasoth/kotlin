// !CHECK_TYPE
// !WITH_NEW_INFERENCE
fun test() {
    val a = if (true) {
        val x = 1
        ({ x })
    } else {
        { 2 }
    }
    a checkType {  _<() -> Int>() }
}