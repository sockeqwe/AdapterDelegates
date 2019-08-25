package com.hannesdorfmann.adapterdelegates4.lintrules.valrules

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.JavaContext
import org.jetbrains.uast.*

/**
 * @author Paul Karpukhin
 */

class ValPatternHandler(private val context: JavaContext) : UElementHandler() {
    private lateinit var node: UVariable
    private var isAdapterDelegateLibrary = false
    private var isVal = false

    override fun visitVariable(node: UVariable) {
        this.node = node
        checkVal()
        checkAdapterDelegateLibrary()
        if (isVal && isAdapterDelegateLibrary) {
            reportMessage()
        }
    }

    private fun checkVal() {
        isVal = node.isFinal && node.isStatic
    }

    private fun checkAdapterDelegateLibrary() {
        val isAdapterDelegateLayoutContainerInlineMethod = node.text.contains("adapterDelegateLayoutContainer<")
        val isAdapterDelegateInlineMethod = node.text.contains("adapterDelegate<")
        isAdapterDelegateLibrary = isAdapterDelegateLayoutContainerInlineMethod || isAdapterDelegateInlineMethod
    }

    private fun reportMessage() {
        context.report(
                ISSUE_VAL_PATTERN,
                node.javaPsi,
                context.getNameLocation(node),
                "val as adapter delegate causes memory leak. Use fun instead of val"
        )
    }
}