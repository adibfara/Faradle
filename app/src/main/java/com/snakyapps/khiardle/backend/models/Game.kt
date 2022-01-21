package com.snakyapps.khiardle.backend.models

data class Game(
    val originalWord: Word,
    val guesses: List<Guess>
)

fun Game.copyWithNewGuess(
    guess: Guess
) = copy(guesses = guesses + guess)