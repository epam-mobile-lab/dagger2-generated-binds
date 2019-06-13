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

package com.epam.daggerbinds.processor.store

import com.epam.daggerbinds.annotation.BindTo
import com.epam.daggerbinds.processor.utils.error
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.inject.Qualifier
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

/**
 * Analyzes if for the current round exist classes marked with [BindTo] annotation.
 * Such classes data stored in the [annotatedElementsData] collection to be processed.
 */
internal class BindToModuleStore(processorEnvironment: ProcessingEnvironment) {
    /**
     * Storage for classes data which should be processed by the processor.
     */
    val annotatedElementsData: MutableList<AnnotatedElementData> = mutableListOf()
    private val typesMap: MutableMap<TypeElement, Int> = mutableMapOf()

    private companion object {
        val BIND_TO_ANNOTATION_NAME: String = BindTo::class.java.canonicalName
    }

    data class AnnotatedElementData(
        val typeElement: TypeElement,
        val interfaceType: TypeElement,
        val qualifierType: TypeElement? = null
    )

    private val messager = processorEnvironment.messager
    private val elementUtils = processorEnvironment.elementUtils
    private val typeUtils = processorEnvironment.typeUtils

    /**
     * Analyze files provided for this for this round of generation. Classes that satisfy all
     * requirements stores in the AnnotatedElementsData collection for further processing.
     * @param roundEnvironment container for the information about the current round of the generation.
     * @return true - if there are classes annotated by [BindTo] for processing,
     *         false - otherwise.
     */
    fun process(roundEnvironment: RoundEnvironment): Boolean {
        roundEnvironment.rootElements.forEach { element ->
            getTypeElementAnnotatedBindTo(element)?.let { elementType ->
                if (!validateAnnotatedElementIsClass(elementType)) {
                    clear()
                    return false
                }
                val pair = getElementArgumentsPair(elementType)
                val interfaceElementType = pair.first
                val qualifierElementType = pair.second
                if (!validateAnnotatedElement(elementType, interfaceElementType, qualifierElementType)) {
                    clear()
                    return false
                }
                interfaceElementType?.run { addInterfaceTypeElement(this) }
                    ?: throw IllegalArgumentException("interfaceElementType should no be null!")
                AnnotatedElementData(elementType, interfaceElementType, qualifierElementType).apply {
                    annotatedElementsData.add(this)
                }
            }
        }
        if (!validateIfMultiImplementations()) {
            clear()
            return false
        }
        return annotatedElementsData.isNotEmpty()
    }

    /**
     * Get type of element that marked [BindTo] annotation.
     * @param element the type of class annotated.
     * @return type of annotated class.
     */
    private fun getTypeElementAnnotatedBindTo(element: Element) =
        element.takeIf {
            it.getAnnotation(BindTo::class.java) != null
        }?.let {
            it as TypeElement
        }

    /**
     * Validates if annotated element is Class.
     * @param elementType type of element annotated by [BindTo] annotation.
     * @return true - if element kind is Class,
     *         false - otherwise.
     */
    private fun validateAnnotatedElementIsClass(elementType: TypeElement) =
        if (elementType.kind != ElementKind.CLASS) {
            messager.error("Only Classes can be annotated with @BindTo", elementType)
            false
        } else true

    /**
     * Gets pair of parameters from [BindTo] annotation parameters.
     * @param elementType type of element annotated by [BindTo] annotation.
     * @return pair of parameters from [BindTo] annotation parameters.
     */
    private fun getElementArgumentsPair(elementType: TypeElement) =
        getArgumentsPair(getArguments(getMirrorForBindTo(elementType.annotationMirrors)))

    /**
     * Gets mirror of [BindTo] annotation.
     * @param annotationMirrors list of annotation mirrors which current element has.
     * @return mirror of [BindTo] annotation.
     */
    private fun getMirrorForBindTo(annotationMirrors: MutableList<out AnnotationMirror>): AnnotationMirror {
        annotationMirrors.forEach { mirror ->
            mirror.takeIf {
                isBindToMirror(it.annotationType)
            }?.let { return it }
        }
        @Suppress("TooGenericExceptionThrown")
        throw RuntimeException("Could not found annotation mirror for ${BindTo::class.java}")
    }

    /**
     * Validates if mirror for [BindTo] annotation.
     * @param type declared type of mirror.
     * @return true is declared type and [BindTo] annotation type the same,
     *         false - otherwise.
     */
    private fun isBindToMirror(type: DeclaredType): Boolean {
        val expectedType = getTypeMirrorFrom(BindTo::class.java.canonicalName)
        return typeUtils.isSameType(type, expectedType)
    }

    /**
     * Gets list of annotation arguments from annotation mirror.
     * @param mirror is the AnnotationMirror.
     * @return list of element type which is the type of arguments.
     */
    private fun getArguments(mirror: AnnotationMirror) =
        mirror.elementValues.map {
            getTypeElementFrom(it.value.value.toString())
        }

    /**
     * Gets pair of parameters from [BindTo] annotation parameters.
     * @param elements list of element types (arguments) at [BindTo] annotation.
     * @return pair of parameters from [BindTo] annotation parameters.
     */
    private fun getArgumentsPair(elements: List<TypeElement>): Pair<TypeElement?, TypeElement?> {
        val interfaces: MutableList<TypeElement?> = mutableListOf()
        val qualifiers: MutableList<TypeElement?> = mutableListOf()
        elements.forEach {
            val typeElement = getTypeElementFrom(it.toString())
            if (!hasQualifierAnnotation(typeElement)) interfaces.add(typeElement)
            if (hasQualifierAnnotation(typeElement)) qualifiers.add(typeElement)
        }
        if (interfaces.size == 2) return interfaces[0] to interfaces[1]
        if (qualifiers.size == 2) return qualifiers[0] to qualifiers[1]
        val interfaceElement = interfaces.takeIf { it.isNotEmpty() }?.get(0)
        val qualifierElement = qualifiers.takeIf { it.isNotEmpty() }?.get(0)
        return interfaceElement to qualifierElement
    }

    /**
     * Validates if parameters of [BindTo] be correct:
     * interfaceType cannot be null,
     * interfaceType cannot be qualifier,
     * interfaceType parameter have to be implemented by annotated class,
     * qualifier has to be marked [Qualifier] annotation.
     * @param elementType type of element annotated by [BindTo] annotation.
     * @param interfaceElementType type of interface argument from [BindTo] annotation.
     * @param qualifierElementType type of qualifier argument from [BindTo] annotation.
     * @return true - if all requires is satisfied,
     *         false - otherwise.
     */
    private fun validateAnnotatedElement(
        elementType: TypeElement,
        interfaceElementType: TypeElement?,
        qualifierElementType: TypeElement?
    ): Boolean {
        if (interfaceElementType == null || hasQualifierAnnotation(interfaceElementType)) {
            messager.error(
                "Class ${elementType.qualifiedName}. The \"interfaceType\" argument for annotation $BIND_TO_ANNOTATION_NAME cannot be qualifier.",
                elementType
            )
            return false
        }
        qualifierElementType?.let {
            if (!hasQualifierAnnotation(qualifierElementType)) {
                messager.error(
                    "Class ${elementType.qualifiedName}. The \"qualifier\" argument for annotation $BIND_TO_ANNOTATION_NAME have to be qualifier.",
                    elementType
                )
                return false
            }
        }
        if (!validateImplementInterfaces(elementType, interfaceElementType)) return false
        return true
    }

    /**
     * Clears all stored data.
     */
    fun clear() {
        annotatedElementsData.clear()
        typesMap.clear()
    }

    /**
     * Collects all interfaces from [BindTo] and count number this interfaces.
     * @param elementType type of interface argument from [BindTo] annotation.
     */
    private fun addInterfaceTypeElement(elementType: TypeElement) {
        typesMap.apply {
            if (contains(elementType)) get(elementType)?.plus(1)?.let { put(elementType, it) } else put(elementType, 1)
        }
    }

    /**
     * Validates that annotated class implement interface type which defined at the [BindTo] annotation.
     * @param elementType type of class annotated by [BindTo] annotation.
     * @param interfaceType type of interface argument from [BindTo] annotation.
     * @return true - if annotated class implemented interface type which defined at the [BindTo] annotation,
     *         false - otherwise.
     */
    private fun validateImplementInterfaces(elementType: TypeElement, interfaceType: TypeElement?): Boolean {
        elementType.interfaces.forEach {
            if (typeUtils.isSameType(it, interfaceType?.asType())) return true
        }
        messager.error(
            "Class ${elementType.qualifiedName} doesn't implement the $interfaceType interface " +
                    "which defined at the annotation.",
            elementType
        )
        return false
    }

    /**
     * Validates is interface which defined at [BindTo] annotation has more than one implementation.
     * @return true - if interface has multi implementation and [BindTo] annotation has any qualifier,
     *         false - if interface has multi implementation and but [BindTo] annotation  has not any qualifier.
     */
    private fun validateIfMultiImplementations(): Boolean {
        annotatedElementsData.forEach {
            val implementationsNumber = typesMap[it.interfaceType]
                ?: throw IllegalArgumentException("implementationsNumber should not be null!")
            if (implementationsNumber > 1 && it.qualifierType == null) {
                messager.error(
                    "Interface ${it.interfaceType.qualifiedName} implemented by Class ${it.typeElement.qualifiedName} " +
                            "has multi-implementation, but ${BindTo::class.java.canonicalName} has not any qualifier.",
                    it.typeElement
                )
                return false
            }
        }
        return true
    }

    /**
     * Checks if element type has [Qualifier] annotation.
     * @param elementType type element.
     * @return true - if element has [Qualifier] annotation,
     *         false = otherwise.
     */
    private fun hasQualifierAnnotation(elementType: TypeElement) =
        elementType.getAnnotation(Qualifier::class.java) != null

    /**
     * Gets element type from canonical name.
     * @param canonicalName type name.
     * @return TypeElement.
     */
    private fun getTypeElementFrom(canonicalName: String) = elementUtils.getTypeElement(canonicalName)

    /**
     * Gets mirror type from canonical name.
     * @param canonicalName type name.
     * @return TypeMirror.
     */
    private fun getTypeMirrorFrom(canonicalName: String) = getTypeElementFrom(canonicalName).asType()
}
