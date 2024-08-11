package com.langsapp.common

fun <SuccessType, FailureType> Result<SuccessType>.mapToCommonResult(
    errorMapping: (Throwable) -> FailureType
): CommonResult<SuccessType, FailureType> =
    fold(
        onSuccess = { CommonResult.Success(it) },
        onFailure = { CommonResult.Failure(errorMapping(it)) }
    )
