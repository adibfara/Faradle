package com.snakyapps.khiardle.backend.models

data class KeyboardKeys(
    val keys: List<Key>
) {
    data class Key(
        val button: Button,
        val enabled: Boolean
    ) {
        enum class Button {
            A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
        }
    }

    companion object {
        fun default(): KeyboardKeys {
            val allKeys = Key.Button.values()

            return KeyboardKeys(allKeys.map {
                Key(it, true)
            }.toList())
        }

        fun KeyboardKeys.copy(button: Key.Button, isEnabled: Boolean): KeyboardKeys {
            val newKeys = keys.map {
                if (it.button == button) it.copy(enabled = isEnabled) else it
            }
            return copy(keys = newKeys)
        }
    }
}