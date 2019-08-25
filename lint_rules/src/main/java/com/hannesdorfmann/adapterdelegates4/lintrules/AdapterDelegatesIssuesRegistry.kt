package com.hannesdorfmann.adapterdelegates4.lintrules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.hannesdorfmann.adapterdelegates4.lintrules.valrules.ISSUE_VAL_PATTERN

/**
 * @author Paul Karpukhin
 */
class AdapterDelegatesIssuesRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(
            ISSUE_VAL_PATTERN
    )
}