package com.snakyapps.khiardle.backend.models

sealed class WordStatus {
    object NotExists : WordStatus()
    object Correct : WordStatus()
    data class Incorrect(
        val equalityStatuses: Array<EqualityStatus>
    ) : WordStatus() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Incorrect

            if (!equalityStatuses.contentEquals(other.equalityStatuses)) return false

            return true
        }

        override fun hashCode(): Int {
            return equalityStatuses.contentHashCode()
        }
    }
}

enum class EqualityStatus {
    WrongPosition,
    Correct,
    Incorrect;
}

