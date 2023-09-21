package com.marcodev.cashflow.utils
import java.text.NumberFormat
import java.util.Locale

object NumberFormatUtils {
    fun formatDoubleToCurrency(doubleValue: Double): String {
        val brazilianCurrencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return brazilianCurrencyFormat.format(doubleValue)
    }

}