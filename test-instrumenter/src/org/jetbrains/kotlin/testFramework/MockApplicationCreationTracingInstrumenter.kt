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

import org.jetbrains.org.objectweb.asm.ClassReader
import org.jetbrains.org.objectweb.asm.ClassWriter
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.tree.AbstractInsnNode
import org.jetbrains.org.objectweb.asm.tree.ClassNode
import org.jetbrains.org.objectweb.asm.tree.InsnList
import org.jetbrains.org.objectweb.asm.tree.MethodNode
import org.jetbrains.org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter
import java.lang.instrument.ClassFileTransformer
import java.security.ProtectionDomain
import kotlin.properties.Delegates


class MockApplicationCreationTracingInstrumenter: ClassFileTransformer {


    private fun createInstructionList(lambda: MethodNode.() -> Unit): InsnList {
        val node = MethodNode()
        lambda(node)
        return node.instructions
    }

    private fun loadTransfromAndSerialize(classfileBuffer: ByteArray, lambda: (ClassNode) -> Unit): ByteArray {
        val reader = ClassReader(classfileBuffer)

        val node = ClassNode()

        reader.accept(node, 0)

        lambda(node)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS)

        val pv = TraceClassVisitor(PrintWriter(System.out.writer()))

        node.accept(pv)

        node.accept(writer)

        return writer.toByteArray()
    }

    override fun transform(loader: ClassLoader, className: String, classBeingRedefined: Class<*>?, protectionDomain: ProtectionDomain, classfileBuffer: ByteArray): ByteArray {

        if (className == "com/intellij/mock/MockComponentManager") {
            return loadTransfromAndSerialize(classfileBuffer) { node ->

                val init = node.methods.first { it.name == "<init>" }

                val callInstructions = createInstructionList {
                    visitVarInsn(Opcodes.ALOAD, 0)
                    visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            "org/jetbrains/kotlin/testFramework/MockComponentManagerCreationTracer",
                            "onCreate",
                            "(Lcom/intellij/mock/MockComponentManager;)V",
                            false
                    )
                }


                var returnNode: AbstractInsnNode by Delegates.notNull()
                init.instructions.iterator().forEach {
                    if (it.opcode == Opcodes.RETURN) {
                        returnNode = it
                    }
                }

                init.instructions.insertBefore(returnNode, callInstructions)
            }
        } else if (className == "com/intellij/mock/MockComponentManager$1") {
            return loadTransfromAndSerialize(classfileBuffer) { node ->

                val method = node.methods.first { it.name == "getComponentInstance" }

                val callInstructions = createInstructionList {
                    visitFieldInsn(Opcodes.GETFIELD,"com/intellij/mock/MockComponentManager$1", "this$0", "Lcom/intellij/mock/MockComponentManager;")
                    visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            "org/jetbrains/kotlin/testFramework/MockComponentManagerCreationTracer",
                            "onGetComponentInstance",
                            "(Lcom/intellij/mock/MockComponentManager;)V",
                            false
                    )
                }

                method.instructions.insert(callInstructions)
            }
        }

        return classfileBuffer
    }
}
