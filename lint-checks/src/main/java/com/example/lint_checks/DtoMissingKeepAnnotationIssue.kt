package com.example.lint_checks

import com.android.tools.lint.detector.api.*

object DtoMissingKeepAnnotationIssue {

    private const val ID = "DtoMissingKeepAnnotationIssue"
    private const val PRIORITY = 10
    private val CATEGORY = Category.CORRECTNESS
    private val SEVERITY = Severity.WARNING
    private const val BRIEF_DESCRIPTION = "DTO classes should have the Keep annotation."
    private const val EXPLANATION = """
        The Keep annotation avoids the obfuscation of the data classes
        which lead to issues parsing the backend responses to the DTOs.
    """
    

    val ISSUE = Issue.create(
        ID,
        BRIEF_DESCRIPTION,
        EXPLANATION,
        CATEGORY,
        PRIORITY,
        SEVERITY,
        Implementation(
            DtoMissingKeepAnnotationDetector::class.java,
            Scope.JAVA_FILE_SCOPE
        )
    )
}