package com.marcodev.cashflow.repository

import android.content.ContentValues
import android.database.Cursor
import com.marcodev.cashflow.database.DatabaseHandler
import com.marcodev.cashflow.entity.Flow

class FlowRepository(private val dbHelper: DatabaseHandler) {

    fun add(flow: Flow): Long {
        val db = this.dbHelper.writableDatabase
        val values = ContentValues()

        values.put(Flow.KEY_VALUE, flow.value)
        values.put(Flow.KEY_TYPE, flow.type)
        values.put(Flow.KEY_DETAIL, flow.detail)
        values.put(Flow.KEY_DTA_LCTO, flow.dtaLcto)

        // Insira o registro na tabela flow e obtenha o ID gerado
        val newFlowId = db.insert(Flow.TABLE_NAME, null, values)

        db.close()

        return newFlowId
    }

    fun get(id: Int): Flow? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            Flow.TABLE_NAME, null,
            "${Flow.KEY_ID}=${id}", null, null, null, null, null
        )

        var flow: Flow? = null

        if (cursor.moveToNext()) {
            flow = Flow(
                id,
                cursor.getDouble(1),
                cursor.getString(2).toString(),
                cursor.getString(3).toString(),
                cursor.getString(4).toString()
            )
        }
        db.close()

        return flow
    }

    fun update(flow: Flow) {
        val db = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(Flow.KEY_VALUE, flow.value)
        values.put(Flow.KEY_TYPE, flow.type)
        values.put(Flow.KEY_DETAIL, flow.detail)
        values.put(Flow.KEY_DTA_LCTO, flow.dtaLcto)

        db.update(Flow.TABLE_NAME, values, "${Flow.KEY_ID} = ${flow._id}", null)

        db.close()
    }

    fun delete(id: Int): Int {
        val db = dbHelper.writableDatabase

        val result = db.delete(Flow.TABLE_NAME, "${Flow.KEY_ID} = ${id}", null)
        db.close()
        return result;
    }

    fun getAll(): List<Flow> {
        val flowList = mutableListOf<Flow>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${Flow.TABLE_NAME}", null)

        if (cursor.moveToFirst()) {
            do {
                val flow = Flow(
                    cursor.getInt(0),
                    cursor.getDouble(1),
                    cursor.getString(2).toString(),
                    cursor.getString(3).toString(),
                    cursor.getString(4).toString()
                )
                flowList.add(flow)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return flowList
    }

    fun getCreditSum(): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(${Flow.KEY_VALUE}) FROM ${Flow.TABLE_NAME} WHERE ${Flow.KEY_TYPE} = 'Crédito'",
            null
        )

        var creditSum = 0.0

        if (cursor.moveToFirst()) {
            creditSum = cursor.getDouble(0)
        }

        cursor.close()
        db.close()

        return creditSum
    }

    fun getDebitSum(): Double {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM(${Flow.KEY_VALUE}) FROM ${Flow.TABLE_NAME} WHERE ${Flow.KEY_TYPE} = 'Débito'",
            null
        )

        var debitSum = 0.0

        if (cursor.moveToFirst()) {
            debitSum = cursor.getDouble(0)
        }

        cursor.close()
        db.close()

        return debitSum
    }

    fun getBalance(): Double {
        val creditSum = this.getCreditSum()
        val debitSum = this.getDebitSum()

        val balanceFinal = creditSum - debitSum

        return balanceFinal
    }

    fun listCursor(): Cursor {
        val db = dbHelper.writableDatabase

        val cursor = db.query(
            Flow.TABLE_NAME,
            null, null, null, null, null, null
        )

        return cursor
    }
}