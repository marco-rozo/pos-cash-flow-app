package com.marcodev.cashflow.entity

class Flow(
    var _id: Int,
    var value: Double,
    var type: String,
    var detail: String,
    var dtaLcto: String
) {
    // Nomes das colunas e da tabela flow
    companion object {
        const val TABLE_NAME = "cash_flow"
        const val KEY_ID = "_id"
        const val KEY_VALUE = "value"
        const val KEY_TYPE = "type"
        const val KEY_DETAIL = "detail"
        const val KEY_DTA_LCTO = "dta_Lcto"

        const val INDEX_KEY_ID = 0
        const val INDEX_KEY_VALUE = 1
        const val INDEX_KEY_TYPE = 2
        const val INDEX_KEY_DETAIL = 3
        const val INDEX_KEY_DTA_LCTO = 4

    }
}