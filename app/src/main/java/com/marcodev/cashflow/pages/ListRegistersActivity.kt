package com.marcodev.cashflow.pages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.marcodev.cashflow.MainActivity
import com.marcodev.cashflow.R
import com.marcodev.cashflow.adapter.FlowAdapter
import com.marcodev.cashflow.database.DatabaseHandler
import com.marcodev.cashflow.repository.FlowRepository

class ListRegistersActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHandler
    private lateinit var lvRegisters: ListView;
    private lateinit var flowRepository: FlowRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_registers)

        lvRegisters = findViewById(R.id.lvRegisters)

        dbHelper = DatabaseHandler(this)
        flowRepository = FlowRepository(dbHelper)
    }

    override fun onStart() {
        super.onStart()
        val cursor = flowRepository.listCursor()
        val adapter = FlowAdapter(this, cursor)
        lvRegisters.adapter = adapter
    }

    fun btAddOnClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}