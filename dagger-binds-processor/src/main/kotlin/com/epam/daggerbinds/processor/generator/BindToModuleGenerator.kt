/*
 * Copyright 2019 EPAM Systems, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.epam.daggerbinds.processor.generator

import com.epam.daggerbinds.processor.DaggerBindsProcessor.Companion.generateBaseComment
import com.epam.daggerbinds.processor.generator.BindToModuleGenerator.Companion.name
import com.epam.daggerbinds.processor.store.BindToModuleStore.AnnotatedElementData
import com.epam.daggerbinds.processor.utils.note
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ParameterSpec
import dagger.Binds
import dagger.Module
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Named
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * Generates dagger [Module] with provide methods that you pass in the [generate] method.
 * Generated class will be named as [name] const.
 */
internal class BindToModuleGenerator(pe: ProcessingEnvironment) {

    private companion object {
        const val name = "Generated_BindsModule"
    }

    private val messager = pe.messager

    /**
     * Generates [TypeSpec] dagger module with List of annotated elements.
     * @param elementsData list of annotated elements for which should be generated binds methods.
     * @return [TypeSpec] that represent dagger module with binds methods with possibility of multi bindings.
     */
    fun generate(elementsData: List<AnnotatedElementData>): TypeSpec = TypeSpec.interfaceBuilder(name)
        .addJavadoc(generateBaseComment())
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(Module::class.java)
        .addMethods(generateBindsMethods(elementsData))
        .build()

    /**
     * Defines list of binds methods.
     * @param elementsData the list of AnnotatedElementData for which should be created binds methods.
     * @return list of provide [MethodSpec].
     */
    private fun generateBindsMethods(elementsData: List<AnnotatedElementData>) =
        elementsData.onEach { messager.note(" -> ${it.typeElement.qualifiedName} added to the binds module") }
            .map { generateBindsMethod(it) }

    /**
     * Generates binds method for elementData.
     * @param elementData the AnnotatedElementData.
     * @return [MethodSpec] that represents binds method.
     */
    private fun generateBindsMethod(elementData: AnnotatedElementData): MethodSpec {
        val methodName = "binds_${elementData.typeElement.qualifiedName.toString().replace(".", "_")}"
        val paramPair = getMethodParamPair(elementData.typeElement)
        val paramSpec = generateParamSpec(paramPair)
        val interfaceName = ClassName.get(elementData.interfaceType)

        val builder = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
            .addParameter(paramSpec)
            .addAnnotation(Binds::class.java)
            .returns(interfaceName)
        if (elementData.qualifierType != null) {
            val annotationSpec = generateAnnotationSpec(elementData.qualifierType, elementData.typeElement)
            builder.addAnnotation(annotationSpec)
        }
        return builder.build()
    }

    /**
     * Creates parameters pair of which would be created from TypeElement.
     * @param elementType the source of parameters.
     * @return pair of [TypeName] and [String].
     */
    private fun getMethodParamPair(elementType: TypeElement?) =
        TypeName.get(elementType?.asType()) to elementType?.qualifiedName.toString().replace(".", "_").decapitalize()

    /**
     * Generates parameter spec which would be created from parameters pair.
     * @param paramPair the pair of parameters.
     * @return [ParameterSpec].
     */
    private fun generateParamSpec(paramPair: Pair<TypeName, String>) =
        ParameterSpec.builder(paramPair.first, paramPair.second).build()

    /**
     * Generates [Qualifier] annotation spec.
     * @param qualifierType the source of qualifier.
     * @param typeElement the source of key.
     * @return annotation spec [Qualifier] for binds module.
     */
    private fun generateAnnotationSpec(qualifierType: TypeElement?, typeElement: TypeElement): AnnotationSpec {
        val namedQualifierName = Named::class.java.canonicalName
        val anotherQualifierName = qualifierType?.qualifiedName.toString()
        val builder = AnnotationSpec.builder(ClassName.get(qualifierType))

        if (namedQualifierName == anotherQualifierName) {
            builder.addMember("value", "\"${typeElement.qualifiedName}\"")
        }
        return builder.build()
    }
}
