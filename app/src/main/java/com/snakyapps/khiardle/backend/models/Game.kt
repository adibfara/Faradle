package com.snakyapps.khiardle.backend.models

data class Game(
    val originalWord: Word,
    val guesses: List<Guess>,
    val wordLength: Int = 5,
)

fun Game.copyWithNewGuess(
    guess: Guess
) = copy(guesses = guesses + guess)