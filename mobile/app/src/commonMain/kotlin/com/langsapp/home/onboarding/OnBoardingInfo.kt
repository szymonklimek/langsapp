package com.langsapp.home.onboarding

class OnBoardingInfo private constructor(
    val sections: List<OnBoardingSection>,
) {
    val isFinished: Boolean =
        sections.none {
            (it.rootStep.required && !it.rootStep.done) ||
                it.childSteps.any { stepInfo ->
                    (stepInfo.required && !stepInfo.done)
                }
        }

    enum class OnBoardingStep {
        SELECT_LANGUAGES,
        SIGN_UP,
        FILL_PROFILE,
        DOWNLOAD_CONTENT,
    }

    /**
     * @param required Indicates if the step is required to do before using the app
     * @param done Indicates if the step is done already
     * @param enabled Indicates if in current state the step can be performed.
     * For example user can't add profile info if they didn't sign up before
     */
    data class StepInfo(
        val step: OnBoardingStep,
        val required: Boolean = false,
        val done: Boolean = false,
        val enabled: Boolean = true,
    )

    data class OnBoardingSection(val rootStep: StepInfo, val childSteps: List<StepInfo> = emptyList())

    companion object {
        fun createForFreshUser(): OnBoardingInfo = OnBoardingInfo(
            sections = listOf(
                OnBoardingSection(
                    rootStep = StepInfo(OnBoardingStep.SELECT_LANGUAGES, required = true),
                    childSteps = listOf(
                        StepInfo(step = OnBoardingStep.DOWNLOAD_CONTENT, required = true, enabled = false),
                    ),
                ),
                OnBoardingSection(
                    rootStep = StepInfo(step = OnBoardingStep.SIGN_UP),
                    childSteps = listOf(StepInfo(OnBoardingStep.FILL_PROFILE, enabled = false)),
                ),
            ),
        )

        fun OnBoardingInfo.finishStep(step: OnBoardingStep): OnBoardingInfo =
            markStepDone(step)
                .let {
                    when (step) {
                        OnBoardingStep.SELECT_LANGUAGES ->
                            it
                                .markStepEnabled(OnBoardingStep.DOWNLOAD_CONTENT)

                        OnBoardingStep.DOWNLOAD_CONTENT -> it
                        OnBoardingStep.SIGN_UP ->
                            it
                                .markStepEnabled(OnBoardingStep.FILL_PROFILE)
                                .markStepRequired(OnBoardingStep.FILL_PROFILE)

                        OnBoardingStep.FILL_PROFILE -> it
                    }
                }

        private fun OnBoardingInfo.markStepDone(step: OnBoardingStep) = OnBoardingInfo(
            sections = sections.map { section ->
                when {
                    section.rootStep.step == step -> section.copy(rootStep = section.rootStep.copy(done = true))
                    else -> section.copy(
                        childSteps = section.childSteps.map {
                            if (it.step == step) {
                                it.copy(done = true)
                            } else {
                                it
                            }
                        },
                    )
                }
            },
        )

        private fun OnBoardingInfo.markStepRequired(step: OnBoardingStep) = OnBoardingInfo(
            sections = sections.map { section ->
                when {
                    section.rootStep.step == step -> section.copy(rootStep = section.rootStep.copy(required = true))
                    else -> section.copy(
                        childSteps = section.childSteps.map {
                            if (it.step == step) {
                                it.copy(required = true)
                            } else {
                                it
                            }
                        },
                    )
                }
            },
        )

        private fun OnBoardingInfo.markStepEnabled(step: OnBoardingStep) = OnBoardingInfo(
            sections = sections.map { section ->
                when {
                    section.rootStep.step == step -> section.copy(rootStep = section.rootStep.copy(enabled = true))
                    else -> section.copy(
                        childSteps = section.childSteps.map {
                            if (it.step == step) {
                                it.copy(enabled = true)
                            } else {
                                it
                            }
                        },
                    )
                }
            },
        )
    }
}
