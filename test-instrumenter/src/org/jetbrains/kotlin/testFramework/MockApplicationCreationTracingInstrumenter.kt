/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.testFramework

import org.jetbrains.org.objectweb.asm.*
import org.jetbrains.org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain


class MockApplicationCreationTracingInstrumenter : ClassFileTransformer {

    private fun loadTransformAndSerialize(classfileBuffer: ByteArray, lambda: (out: ClassVisitor) -> ClassVisitor): ByteArray {
        val reader = ClassReader(classfileBuffer)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)

        val pv = TraceClassVisitor(writer, PrintWriter(System.out.writer()))

        reader.accept(lambda(pv), 0)

        return writer.toByteArray()
    }

    override fun transform(loader: ClassLoader, className: String, classBeingRedefined: Class<*>?, protectionDomain: ProtectionDomain, classfileBuffer: ByteArray): ByteArray {

        if (className == "com/intellij/mock/MockComponentManager") {
            return loadTransformAndSerialize(classfileBuffer) { out ->
                object : ClassVisitor(Opcodes.ASM6, out) {
                    override fun visitMethod(access: Int, name: String, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
                        val originalVisitor = super.visitMethod(access, name, desc, signature, exceptions)

                        return if (name == "<init>") {
                            object : MethodVisitor(Opcodes.ASM6, originalVisitor) {
                                override fun visitInsn(opcode: Int) {
                                    if (opcode == Opcodes.RETURN) {
                                        visitVarInsn(Opcodes.ALOAD, 0)
                                        visitMethodInsn(
                                                Opcodes.INVOKESTATIC,
                                                "org/jetbrains/kotlin/testFramework/MockComponentManagerCreationTracer",
                                                "onCreate",
                                                "(Lcom/intellij/mock/MockComponentManager;)V",
                                                false
                                        )
                                    }
                                    super.visitInsn(opcode)
                                }
                            }
                        }
                        else {
                            originalVisitor
                        }
                    }
                }
            }
        }
        else if (className == "com/intellij/mock/MockComponentManager$1") {
            return loadTransformAndSerialize(classfileBuffer) { out ->
                object : ClassVisitor(Opcodes.ASM6, out) {
                    override fun visitMethod(access: Int, name: String, desc: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
                        val originalVisitor = super.visitMethod(access, name, desc, signature, exceptions)

                        return if (name == "getComponentInstance") {
                            object : MethodVisitor(Opcodes.ASM6, originalVisitor) {
                                override fun visitCode() {
                                    super.visitCode()
                                    visitLabel(Label())
                                    visitVarInsn(Opcodes.ALOAD, 0)
                                    visitFieldInsn(Opcodes.GETFIELD, "com/intellij/mock/MockComponentManager$1", "this$0", "Lcom/intellij/mock/MockComponentManager;")
                                    visitMethodInsn(
                                            Opcodes.INVOKESTATIC,
                                            "org/jetbrains/kotlin/testFramework/MockComponentManagerCreationTracer",
                                            "onGetComponentInstance",
                                            "(Lcom/intellij/mock/MockComponentManager;)V",
                                            false
                                    )
                                }
                            }
                        }
                        else {
                            originalVisitor
                        }
                    }
                }
            }
        }

        return classfileBuffer
    }
}
