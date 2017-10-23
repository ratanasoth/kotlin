/*
 * Copyright 2010-2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.backend.common

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.PropertyGetterDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorEquivalenceForOverrides
import org.jetbrains.kotlin.resolve.DescriptorUtils
import org.jetbrains.kotlin.resolve.OverridingUtil
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.types.typeUtil.supertypes

val SUSPEND_COROUTINE_OR_RETURN_NAME = Name.identifier("suspendCoroutineOrReturn")
val COROUTINE_SUSPENDED_NAME = Name.identifier("COROUTINE_SUSPENDED")

val COROUTINES_INTRINSICS_PACKAGE_FQ_NAME = DescriptorUtils.COROUTINES_PACKAGE_FQ_NAME.child(Name.identifier("intrinsics"))
val COROUTINE_CONTEXT_FQ_NAME = COROUTINES_INTRINSICS_PACKAGE_FQ_NAME.child(Name.identifier("coroutineContext"))
val COROUTINE_JVM_INTERNAL_PACKAGE_FQ_NAME = FqName("kotlin.coroutines.experimental.jvm.internal")
val COROUTINE_IMPL_NAME = Name.identifier("CoroutineImpl")
val COROUTINE_IMPL_FQ_NAME = COROUTINE_JVM_INTERNAL_PACKAGE_FQ_NAME.child(COROUTINE_IMPL_NAME)
val DO_RESUME_NAME = Name.identifier("doResume")

fun FunctionDescriptor.isBuiltInSuspendCoroutineOrReturn(): Boolean {
    if (name != SUSPEND_COROUTINE_OR_RETURN_NAME) return false

    val originalDeclaration = getBuiltInSuspendCoroutineOrReturn() ?: return false

    return DescriptorEquivalenceForOverrides.areEquivalent(
            originalDeclaration, this
    )
}

fun FunctionDescriptor.getBuiltInSuspendCoroutineOrReturn() =
        module.getPackage(COROUTINES_INTRINSICS_PACKAGE_FQ_NAME).memberScope
                .getContributedFunctions(SUSPEND_COROUTINE_OR_RETURN_NAME, NoLookupLocation.FROM_BACKEND)
                .singleOrNull()

fun FunctionDescriptor.isBuiltInCoroutineContext() =
        (this as? PropertyGetterDescriptor)?.correspondingProperty?.fqNameSafe == COROUTINE_CONTEXT_FQ_NAME

fun FunctionDescriptor.isCoroutineImplDoResume(): Boolean {
    fun coroutineImplClassDescriptor() = module.getPackage(COROUTINE_JVM_INTERNAL_PACKAGE_FQ_NAME).memberScope
            .getContributedDescriptors { it == COROUTINE_IMPL_NAME }.singleOrNull() as ClassDescriptor
    fun originalDescriptor() = coroutineImplClassDescriptor().unsubstitutedMemberScope
            .getContributedFunctions(DO_RESUME_NAME, NoLookupLocation.FROM_BACKEND).singleOrNull()
    if (this.name != DO_RESUME_NAME) return false
    if (this.containingDeclaration !is ClassDescriptor) return false
    val supertypes = (this.containingDeclaration as ClassDescriptor).defaultType.supertypes()
    if (supertypes.none { it.constructor.declarationDescriptor?.fqNameSafe == COROUTINE_IMPL_FQ_NAME }) return false
    return OverridingUtil.DEFAULT.isOverridableBy(
            originalDescriptor() as FunctionDescriptor,
            this,
            coroutineImplClassDescriptor()
    ).result == OverridingUtil.OverrideCompatibilityInfo.Result.OVERRIDABLE
}
