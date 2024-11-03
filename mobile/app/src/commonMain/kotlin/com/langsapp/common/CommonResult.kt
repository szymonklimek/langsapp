package com.langsapp.common

/**
 * Class similar to [Result] that looks more elegant when used from iOS codebase and provide possibility to define
 * custom [FailureType] for better compile-type safety when used.
 */
sealed class CommonResult<SuccessType, FailureType> {
    data class Success<SuccessType, FailureType>(val value: SuccessType) : CommonResult<SuccessType, FailureType>()
    data class Failure<SuccessType, FailureType>(val error: FailureType) : CommonResult<SuccessType, FailureType>()

    fun fold(
        onSuccess: (SuccessType) -> (Unit),
        onFailure: (FailureType) -> (Unit),
    ) {
        when (this) {
            is Success -> onSuccess(value)
            is Failure -> onFailure(error)
        }
    }

    fun getOrNull(): SuccessType? =
        when (this) {
            is Success -> value
            is Failure -> null
        }

    fun errorOrNull(): FailureType? =
        when (this) {
            is Success -> null
            is Failure -> error
        }
}
