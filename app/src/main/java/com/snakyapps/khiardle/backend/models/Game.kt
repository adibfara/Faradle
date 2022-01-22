package com.snakyapps.khiardle.backend.models

data class Game(
    val originalWord: Word,
    val guesses: List<Guess>,
    val wordLength: Int = 5,
    private val keyboardKeys: KeyboardKeys = KeyboardKeys.English(),
) {
    val availableKeyboard: KeyboardKeys = keyboardKeys.updateWithGuesses(guesses)
    val isWon = guesses.any { it.wordStatus == WordStatus.Correct }
}

private fun KeyboardKeys.updateWithGuesses(guesses: List<Guess>): KeyboardKeys {
    val allWords: Map<Char, List<Pair<Char, EqualityStatus?>>> =
        guesses.flatMap { guess ->
            guess.word.word.mapIndexed { index, character ->
                when (guess.wordStatus) {
                    WordStatus.Correct -> Pair(character, EqualityStatus.Correct)
                    is WordStatus.Incorrect -> Pair(character,
                        guess.wordStatus.equalityStatuses[index])
                    WordStatus.NotExists -> Pair(character, null)
                }
            }
        }.toSet().groupBy { it.first.uppercaseChar() }

    val keys = keys.map {

        val isIncorrect =
            allWords[it.button.uppercaseChar()]?.all { it.second == EqualityStatus.Incorrect }
        it.copy(enabled = isIncorrect != true)
    }

    return withUpdatedButton(keys)
}

fun Game.copyWithNewGuess(
    guess: Guess,
) = copy(guesses = guesses + guess)