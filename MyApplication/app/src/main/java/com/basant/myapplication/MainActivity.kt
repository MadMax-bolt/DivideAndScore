package com.basant.myapplication

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.commonScore
import kotlinx.android.synthetic.main.activity_main.computer
import kotlinx.android.synthetic.main.activity_main.currentNumber
import kotlinx.android.synthetic.main.activity_main.enterButton
import kotlinx.android.synthetic.main.activity_main.linearlayout1
import kotlinx.android.synthetic.main.activity_main.number2
import kotlinx.android.synthetic.main.activity_main.number3
import kotlinx.android.synthetic.main.activity_main.player
import kotlinx.android.synthetic.main.activity_main.start_button
import kotlinx.android.synthetic.main.activity_main.winner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var common_score = 0
        var current_number = 324.0
        var number_selected: Int = 2

        var PlayerNow = false

        text_update(common_score, current_number)

        number2.setOnClickListener {
            number2.setBackgroundColor(Color.RED)
            number3.setBackgroundColor(Color.TRANSPARENT)
            number_selected = 2
        }

        number3.setOnClickListener {
            number2.setBackgroundColor(Color.TRANSPARENT)
            number3.setBackgroundColor(Color.RED)
            number_selected = 3
        }

        start_button.setOnClickListener {
            linearlayout1.visibility = View.VISIBLE
        }

        player.setOnClickListener {
            player.setTypeface(null, Typeface.BOLD)
            computer.setTypeface(null, Typeface.NORMAL)
            PlayerNow = true
        }

        computer.setOnClickListener {
            player.setTypeface(null, Typeface.NORMAL)
            computer.setTypeface(null, Typeface.BOLD)
            PlayerNow = false
        }

        enterButton.setOnClickListener {
            if (PlayerNow) {
                if (!winner_check(current_number, common_score)) {
                    if (current_number % number_selected == 0.0) {
                        current_number /= number_selected
                        if (current_number % 2 == 0.0) {
                            common_score += 1
                        } else {
                            common_score -= 1
                        }
                        text_update(common_score, current_number)
                        val move = computerMove(
                            current_number.toInt(),
                            Integer.MIN_VALUE,
                            Integer.MAX_VALUE
                        )
                        current_number /= move
                        text_update(common_score, current_number)
                        winner_check(current_number, common_score)
                    }
                }
            } else {
                val move =
                    computerMove(current_number.toInt(), Integer.MIN_VALUE, Integer.MAX_VALUE)
                current_number /= move
                text_update(common_score, current_number)
                winner_check(current_number, common_score)
            }
        }
    }

    fun computerMove(number: Int, alpha: Int, beta: Int): Int {
        if (number == 2 || number == 3) {
            return if (beta >= 0) 1 else -1
        }
        var bestScore = Integer.MIN_VALUE
        var bestMove = -1
        var newAlpha = alpha
        for (move in listOf(2, 3)) {
            if (number % move == 0) {
                val score = if (number / move % 2 == 0) 1 else -1
                val newNumber = number / move
                val newScore = -computerMove(newNumber, -beta, -newAlpha)
                if (newScore + score > bestScore) {
                    bestScore = newScore + score
                    bestMove = move
                }
                newAlpha = maxOf(newAlpha, newScore + score)
                if (newAlpha >= beta) {
                    break
                }
            }
        }
        return bestMove
    }


    fun text_update(score: Int, number: Double) {
        currentNumber.text = number.toInt().toString()
        commonScore.text = score.toInt().toString()

    }

    fun winner_check(x: Double, y: Int): Boolean {
        if (x == 2.0 || x == 3.0) {
            winner.visibility = View.VISIBLE
            if (y <= 0) {
                winner.text = "Player Wins"
            } else {
                winner.text = "Computer Wins"
            }
            return true
        }
        return false
    }
}