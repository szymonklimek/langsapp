package com.langsapp.settings.language

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.StateManager
import com.langsapp.config.Log
import com.langsapp.model.LanguageSetting

class LanguageSettingsStateManager(
    private val languageSettingsRepository: LanguageSettingsRepository,
) : StateManager<LanguageSettingsState, LanguageSettingsAction>(
    initialState = LanguageSettingsState.Loading,
    initialSideEffects = ArrayDeque(listOf(LanguageSettingsSideEffect.FetchAvailableLanguages)),
    handleAction = { currentState, action ->
        when (action) {
            is LanguageSettingsAction.AvailableLanguagesLoaded -> when (currentState) {
                LanguageSettingsState.Loading -> ActionResult(
                    LanguageSettingsState.Input(
                        availableLanguages = action.languages,
                        learnLanguage = null,
                        baseLanguage = null,
                        supportLanguage = null,
                        isInputCorrect = false,
                    ),
                )

                is LanguageSettingsState.Input -> ActionResult()
                LanguageSettingsState.DataLoadingFailure -> ActionResult()
            }

            LanguageSettingsAction.BackTapped -> ActionResult(sideEffects = listOf(CommonSideEffect.Exit))
            is LanguageSettingsAction.BaseLanguageChanged -> when (currentState) {
                LanguageSettingsState.Loading -> ActionResult()
                is LanguageSettingsState.Input -> ActionResult(
                    currentState
                        .copy(baseLanguage = action.language)
                        .let { it.copy(isInputCorrect = it.isValid()) },
                )

                LanguageSettingsState.DataLoadingFailure -> ActionResult()
            }

            is LanguageSettingsAction.LearnLanguageChanged -> when (currentState) {
                LanguageSettingsState.Loading -> ActionResult()
                is LanguageSettingsState.Input -> ActionResult(
                    currentState
                        .copy(learnLanguage = action.language)
                        .let { it.copy(isInputCorrect = it.isValid()) },
                )

                LanguageSettingsState.DataLoadingFailure -> ActionResult()
            }

            is LanguageSettingsAction.SupportLanguageChanged -> when (currentState) {
                LanguageSettingsState.Loading -> ActionResult()
                is LanguageSettingsState.Input -> ActionResult(
                    currentState
                        .copy(supportLanguage = action.language)
                        .let { it.copy(isInputCorrect = it.isValid()) },
                )

                LanguageSettingsState.DataLoadingFailure -> ActionResult()
            }

            LanguageSettingsAction.ConfirmTapped -> when (currentState) {
                LanguageSettingsState.Loading -> ActionResult()
                is LanguageSettingsState.Input -> {
                    if (currentState.isValid()) {
                        ActionResult(
                            sideEffects =
                            listOf(
                                LanguageSettingsSideEffect.ConfirmLanguagesSelection(
                                    availableLanguages = currentState.availableLanguages,
                                    learnLanguage = currentState.learnLanguage!!,
                                    baseLanguage = currentState.baseLanguage!!,
                                    supportLanguage = currentState.supportLanguage,
                                ),
                            ),
                        )
                    } else {
                        ActionResult(
                            // TODO Replace with correctly localised text
                            sideEffects = listOf(CommonSideEffect.ShowPopUpMessage("Invalid input. TODO Replace me")),
                        )
                    }
                }

                LanguageSettingsState.DataLoadingFailure -> ActionResult()
            }

            is LanguageSettingsAction.SelectionSavingFailed -> ActionResult(
                newState = LanguageSettingsState.Input(
                    availableLanguages = action.availableLanguages,
                    learnLanguage = action.learnLanguage,
                    baseLanguage = action.baseLanguage,
                    supportLanguage = action.supportLanguage,
                    isInputCorrect = true,
                ),
                sideEffects = listOf(
                    // TODO Replace with correctly localised text
                    CommonSideEffect.ShowPopUpMessage("Something went wrong :/ "),
                ),
            )

            LanguageSettingsAction.SelectionSavingSucceeded -> ActionResult(
                sideEffects = listOf(
                    // TODO Replace with correctly localised text
                    CommonSideEffect.ShowPopUpMessage("Languages saved successfully"),
                    CommonSideEffect.Exit,
                ),
            )

            LanguageSettingsAction.AvailableLanguagesLoadFailure ->
                ActionResult()
            LanguageSettingsAction.RetryTapped ->
                ActionResult(
                    newState = LanguageSettingsState.Loading,
                    sideEffects = listOf(LanguageSettingsSideEffect.FetchAvailableLanguages),
                )
        }
    },
    handleSideEffect = { sideEffect ->
        when (sideEffect) {
            LanguageSettingsSideEffect.FetchAvailableLanguages ->
                languageSettingsRepository.fetchAvailableLanguages()
                    .fold(
                        onSuccess = {
                            LanguageSettingsAction.AvailableLanguagesLoaded(it)
                        },
                        onFailure = {
                            LanguageSettingsAction.AvailableLanguagesLoadFailure
                        },
                    )

            is LanguageSettingsSideEffect.ConfirmLanguagesSelection -> {
                languageSettingsRepository.saveSelectedLanguages(
                    LanguageSetting(
                        learnLanguage = sideEffect.learnLanguage,
                        baseLanguage = sideEffect.baseLanguage,
                        supportLanguage = sideEffect.supportLanguage,
                    ),
                ).fold(
                    onSuccess = { LanguageSettingsAction.SelectionSavingSucceeded },
                    onFailure = {
                        Log.d("Failed to update language settings.  Reason: $it")
                        LanguageSettingsAction.SelectionSavingFailed(
                            availableLanguages = sideEffect.availableLanguages,
                            learnLanguage = sideEffect.learnLanguage,
                            baseLanguage = sideEffect.baseLanguage,
                            supportLanguage = sideEffect.supportLanguage,
                        )
                    },
                )
            }

            else -> null
        }
    },
)

private fun LanguageSettingsState.Input.isValid() =
    learnLanguage != baseLanguage &&
        baseLanguage != supportLanguage &&
        learnLanguage != supportLanguage &&
        learnLanguage != null &&
        baseLanguage != null
