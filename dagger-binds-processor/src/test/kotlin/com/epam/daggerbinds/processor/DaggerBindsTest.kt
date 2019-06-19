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

package com.epam.daggerbinds.processor

import com.epam.daggerbinds.processor.SourceCode.MultiSubtypes.NamedQualifier
import com.epam.daggerbinds.processor.SourceCode.MultiSubtypes.CustomQualifier
import com.epam.daggerbinds.processor.SourceCode.SingleSubtype
import com.epam.daggerbinds.processor.SourceCode.WithError.NoQualifier
import com.epam.daggerbinds.processor.SourceCode.WithError.NoInterface
import com.epam.daggerbinds.processor.SourceCode.WithError.QualifierIsNotQualifier
import com.epam.daggerbinds.processor.SourceCode.WithError.InterfaceTypeIsQualifier
import com.epam.daggerbinds.processor.SourceCode.generatedClassQualifierName
import com.google.testing.compile.CompilationSubject
import com.google.testing.compile.JavaFileObjects
import com.google.testing.compile.CompilationSubject.assertThat
import com.google.testing.compile.Compiler.javac
import org.junit.Before
import org.junit.Test
import javax.tools.JavaFileObject

class DaggerBindsTest {

    private lateinit var processor: DaggerBindsProcessor

    @Before
    fun setUp() {
        processor = DaggerBindsProcessor()
    }

    @Test
    fun `single subtype`() {
        val generatedClass = JavaFileObjects.forSourceString(
            generatedClassQualifierName,
            SingleSubtype.generatedClass
        )

        val superClass = JavaFileObjects.forSourceString(
            SingleSubtype.superClassQualifierName,
            SingleSubtype.superClass
        )

        val implementedClass = JavaFileObjects.forSourceString(
            SingleSubtype.impClassQualifierName,
            SingleSubtype.impClass
        )

        compileAndAssertSuccess(generatedClass, implementedClass, superClass)
    }

    @Test
    fun `multi subtype with Named qualifier`() {
        val generatedClass = JavaFileObjects.forSourceString(
            generatedClassQualifierName,
            NamedQualifier.generatedClass
        )

        val superClass = JavaFileObjects.forSourceString(
            NamedQualifier.superClassQualifierName,
            NamedQualifier.superClass
        )

        val implementedClassFirst = JavaFileObjects.forSourceString(
            NamedQualifier.impClassFirstQualifierName,
            NamedQualifier.impClassFirst
        )

        val implementedClassSecond = JavaFileObjects.forSourceString(
            NamedQualifier.impClassSecondQualifierName,
            NamedQualifier.impClassSecond
        )

        compileAndAssertSuccess(generatedClass, implementedClassFirst, implementedClassSecond, superClass)
    }

    @Test
    fun `multi subtype with Custom qualifier`() {
        val generatedClass = JavaFileObjects.forSourceString(
            generatedClassQualifierName,
            CustomQualifier.generatedClass
        )

        val superClass = JavaFileObjects.forSourceString(
            CustomQualifier.superClassQualifierName,
            CustomQualifier.superClass
        )

        val implementedClassFirst = JavaFileObjects.forSourceString(
            CustomQualifier.impClassFirstQualifierName,
            CustomQualifier.impClassFirst
        )

        val implementedClassSecond = JavaFileObjects.forSourceString(
            CustomQualifier.impClassSecondQualifierName,
            CustomQualifier.impClassSecond
        )

        val customAnnotationClassFirst = JavaFileObjects.forSourceString(
            CustomQualifier.customQualifierClassFirstQualifierName,
            CustomQualifier.customQualifierClassFirst
        )

        val customAnnotationClassSecond = JavaFileObjects.forSourceString(
            CustomQualifier.customQualifierClassSecondQualifierName,
            CustomQualifier.customQualifierClassSecond
        )

        compileAndAssertSuccess(
            generatedClass,
            implementedClassFirst,
            implementedClassSecond,
            superClass,
            customAnnotationClassFirst,
            customAnnotationClassSecond
        )
    }

    @Test
    fun `annotation has not qualifier in case Interface has multi implementation`() {
        val superClass = JavaFileObjects.forSourceString(
            NoQualifier.superClassQualifierName,
            NoQualifier.superClass
        )

        val implementedClassFirst = JavaFileObjects.forSourceString(
            NoQualifier.impClassFirstQualifierName,
            NoQualifier.impClassFirst
        )

        val implementedClassSecond = JavaFileObjects.forSourceString(
            NoQualifier.impClassSecondQualifierName,
            NoQualifier.impClassSecond
        )

        compileAndAssertFailed(
            "Interface com.epam.subtypes.severalfirst.Repository implemented by Class com.epam.subtypes.severalfirst.RepositoryImpFirst " +
                    "has multi-implementation, but com.epam.daggerbinds.annotation.BindTo has not any qualifier.",
            superClass,
            implementedClassFirst,
            implementedClassSecond
        )
    }

    @Test
    fun `class does not implement interface which defined at the annotation`() {
        val superClass = JavaFileObjects.forSourceString(
            NoInterface.superClassQualifierName,
            NoInterface.superClass
        )

        val implementedClass = JavaFileObjects.forSourceString(
            NoInterface.impClassQualifierName,
            NoInterface.impClass
        )

        compileAndAssertFailed(
            "Class com.epam.subtypes.severalfirst.RepositoryImp doesn't " +
                    "implement the com.epam.subtypes.severalfirst.Repository interface " +
                    "which defined at the annotation.",
            superClass,
            implementedClass
        )
    }

    @Test
    fun `interface type is qualifier`() {
        val superClass = JavaFileObjects.forSourceString(
            InterfaceTypeIsQualifier.superClassQualifierName,
            InterfaceTypeIsQualifier.superClass
        )

        val implementedClass = JavaFileObjects.forSourceString(
            InterfaceTypeIsQualifier.impClassQualifierName,
            InterfaceTypeIsQualifier.impClass
        )

        compileAndAssertFailed(
            "Class com.epam.subtypes.severalfirst.RepositoryImp. The \"interfaceType\" argument for " +
                    "annotation com.epam.daggerbinds.annotation.BindTo cannot be qualifier.",
            superClass,
            implementedClass
        )
    }

    @Test
    fun `qualifier is not qualifier`() {
        val superClass = JavaFileObjects.forSourceString(
            QualifierIsNotQualifier.superClassQualifierName,
            QualifierIsNotQualifier.superClass
        )

        val implementedClass = JavaFileObjects.forSourceString(
            QualifierIsNotQualifier.impClassQualifierName,
            QualifierIsNotQualifier.impClass
        )

        compileAndAssertFailed(
            "Class com.epam.subtypes.severalfirst.RepositoryImp. The \"qualifier\" argument for " +
                    "annotation com.epam.daggerbinds.annotation.BindTo have to be qualifier.",
            superClass,
            implementedClass
        )
    }

    private fun compileAndAssertSuccess(expectedModule: JavaFileObject, vararg files: JavaFileObject) {
        val compilation = javac().withProcessors(processor).compile(*files)
        CompilationSubject.assertThat(compilation)
            .succeeded()
        CompilationSubject.assertThat(compilation)
            .generatedSourceFile(generatedClassQualifierName)
            .hasSourceEquivalentTo(expectedModule)
    }

    private fun compileAndAssertFailed(expectedError: String, vararg files: JavaFileObject) {
        val compilation = javac().withProcessors(processor).compile(*files)
        assertThat(compilation).failed()
        assertThat(compilation).hadErrorContaining(expectedError)
    }
}