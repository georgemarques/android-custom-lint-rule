package com.example.lint_checks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import org.junit.Test

internal class DtoMissingKeepAnnotationDetectorTest {

    @Test
    fun `given kotlin dto class missing Keep annotation should return warning message`() {
        TestLintTask.lint().files(
            TestFiles.kotlin(
                """
                        package foo
                        class TestSuffixDTO
                """
            ).indented()
        )
            .issues(DtoMissingKeepAnnotationIssue.ISSUE)
            .run()
            .expect(
                """
                    src/foo/TestSuffixDTO.kt: Warning: DTO classes should have the Keep annotation. [DtoMissingKeepAnnotationIssue]
                    0 errors, 1 warnings
                    """

            )
    }

    @Test
    fun `given kotlin dto class with Keep annotation should return no warnings`() {
        val stubAnnotation = kotlin(
            """
                package androidx.annotation
                annotation class Keep  
            """.trimIndent()
        )
        val stubFile = kotlin(
            """
            package foo
            @androidx.annotation.Keep
            class TestSuffixDTO
            """
        ).indented()

        TestLintTask.lint()
            .files(stubAnnotation, stubFile)
            .issues(DtoMissingKeepAnnotationIssue.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun `given java DTO class missing Keep annotation should return DtoMissingKeepAnnotationIssue`() {
        val stubFile = java(
            """
            package com.example.lintcustomrules;
            public class TestJavaDTO {
            }
            """
        ).indented()

        TestLintTask.lint()
            .files(stubFile)
            .issues(DtoMissingKeepAnnotationIssue.ISSUE)
            .run()
            .expect(
                """
                    src/com/example/lintcustomrules/TestJavaDTO.java:2: Warning: DTO classes should have the Keep annotation. [DtoMissingKeepAnnotationIssue]
                    public class TestJavaDTO {
                    ^
                    0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun `given java DTO class with Keep annotation should not return DtoMissingKeepAnnotationIssue`() {
        val stubFile = java(
            """
            package com.example.lintcustomrules;
            @androidx.annotation.Keep
            public class TestJavaDTO {
            }
            """
        ).indented()

        TestLintTask.lint()
            .files(stubFile)
            .issues(DtoMissingKeepAnnotationIssue.ISSUE)
            .run()
            .expectClean()
    }
}
