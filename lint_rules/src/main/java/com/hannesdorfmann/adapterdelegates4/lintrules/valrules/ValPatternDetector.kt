package com.hannesdorfmann.adapterdelegates4.lintrules.valrules

import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.*

/**
 * @author Paul Karpukhin
 */
class ValPatternDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes() = listOf(UVariable::class.java)

    override fun createUastHandler(context: JavaContext) = ValPatternHandler(context)
}