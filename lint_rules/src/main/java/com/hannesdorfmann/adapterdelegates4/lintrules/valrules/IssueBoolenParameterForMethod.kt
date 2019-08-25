package com.hannesdorfmann.adapterdelegates4.lintrules.valrules

import com.android.tools.lint.detector.api.*

/**
 * @author Paul Karpukhin
 */
val ISSUE_VAL_PATTERN = Issue.create(
    "top level val ",
    "Top level val for adapter delegates is not good idea",
    "The new kotlin DSL is nice but could lead to memory leaks if you use a top level val " +
            "in a kotlin file as such a top level val will internally result in a static field that " +
            "will be kept forever. Thus, the context of the adapter delegate might be kept forever " +
            "around (leaking activity).",
    Category.CORRECTNESS,
    5,
    Severity.WARNING,
    Implementation(
        ValPatternDetector::class.java,
        Scope.JAVA_FILE_SCOPE
    )
)