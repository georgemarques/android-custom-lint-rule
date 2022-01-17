package com.example.lint_checks

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.TextFormat
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement
import org.jetbrains.uast.java.JavaUClass
import org.jetbrains.uast.kotlin.KotlinUClass

private const val KEEP_ANNOTATION = "androidx.annotation.Keep"

class DtoMissingKeepAnnotationDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UClass::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return KeepAnnotationElementHandler(context)
    }

    private class KeepAnnotationElementHandler(private val context: JavaContext) :
        UElementHandler() {

        override fun visitClass(node: UClass) {
            if (node is JavaUClass || node is KotlinUClass) {
                val hasDtoSuffix = node.name?.endsWith("DTO")
                if (hasDtoSuffix == true) {
                    val hasKeepAnnotation =
                        node.findAnnotation(KEEP_ANNOTATION) != null

                    if (!hasKeepAnnotation) {
                        val issue = DtoMissingKeepAnnotationIssue.ISSUE
                        context.report(
                            issue = issue,
                            scopeClass = node,
                            location = context.getLocation(node.javaPsi),
                            message = issue.getBriefDescription(TextFormat.TEXT),
                            quickfixData = addKeepAnnotationFix()
                        )
                    }
                }
            }
        }

        private fun addKeepAnnotationFix(): LintFix =
            LintFix.create()
                .annotate(KEEP_ANNOTATION)
                .build()
    }
}
