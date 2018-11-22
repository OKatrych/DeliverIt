package eu.warble.deliverit.presentation.util

sealed class Result {
    class Success<T>(val value: T): Result()
    class Failure(val error: Throwable): Result()
}