package com.marcodev.cashflow

import DateMaskWatcher
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.marcodev.cashflow.database.DatabaseHandler
import com.marcodev.cashflow.entity.Flow
import com.marcodev.cashflow.pages.ListRegistersActivity
import com.marcodev.cashflow.repository.FlowRepository

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHandler
    private lateinit var flowRepository: FlowRepository

    private lateinit var etValue: EditText
    private lateinit var etDateLctos: EditText
    private lateinit var btSave: Button
    private lateinit var btShowLctos: Button
    private lateinit var btShowBalance: Button
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerDetail: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etValue = findViewById(R.id.etValue)
        etDateLctos = findViewById(R.id.etDateLctos)
        btSave = findViewById(R.id.btSave)
        btShowLctos = findViewById(R.id.btShowAll)
        btShowBalance = findViewById(R.id.btShowBalance)
        spinnerType = findViewById(R.id.spType)
        spinnerDetail = findViewById(R.id.spDetail)

        etDateLctos.addTextChangedListener(DateMaskWatcher(etDateLctos))


        val typesArray = resources.getStringArray(R.array.types)
        val detailsArray = resources.getStringArray(R.array.details_credit)

        // Criando um adapter para o Spinner
        val detailAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, detailsArray)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typesArray)

        // Defina o layout para exibição das opções
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        detailAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Associando o adapter ao Spinner
        spinnerType.adapter = adapter
        spinnerDetail.adapter = detailAdapter

        // Definindo um listner de seleção para o Spinner
        spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                //Identificando o item selecionado
                val selectedItem = typesArray[position]

                val detailsArray = when (selectedItem) {
                    "Crédito" -> resources.getStringArray(R.array.details_credit)
                    "Débito" -> resources.getStringArray(R.array.details_debit)
                    else -> emptyArray()
                }

                // Criando um novo adaptador com os valores correspondentes
                val detailsAdapter = ArrayAdapter(
                    this@MainActivity,
                    android.R.layout.simple_spinner_item,
                    detailsArray
                )
                detailsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDetail.adapter = detailsAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Este método é chamado quando nada é selecionado
            }
        }

        dbHelper = DatabaseHandler(this)
        flowRepository = FlowRepository(dbHelper)

    }


    fun btSaveOnClick(view: View) {
        if (etValue.getText().toString() == "") {
            etValue.setError("Campo não pode ser vazio")
            etValue.requestFocus()
            return
        }

        if (etDateLctos.getText().toString() == "") {
            etDateLctos.setError("Campo não pode ser vazi")
            etDateLctos.requestFocus()
            return
        }

        if (!DateMaskWatcher(etDateLctos).isValidDate()) {
            etDateLctos.setError("Data inválida")
            etDateLctos.requestFocus()
            return
        }

        val selectedTypeValue = spinnerType.selectedItem.toString()
        val selectedDetailValue = spinnerDetail.selectedItem.toString()

        val newFlow = Flow(
            0,
            etValue.text.toString().toDouble(),
            selectedTypeValue,
            selectedDetailValue,
            etDateLctos.getText().toString()
        )

        val newFlowId = flowRepository.add(newFlow)

        Toast.makeText(this, "Inclusão efetuada com sucesso", Toast.LENGTH_LONG).show()

    }

    fun btShowBalanceOnClick(view: View) {

        val balanceValue: Double = flowRepository.getBalance()
        val creditSumValue: Double = flowRepository.getCreditSum()
        val debitSumValue: Double = flowRepository.getDebitSum()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Saldo Atual")
        val message: CharSequence =
            "Seu saldo atual é: $balanceValue \nSoma dos créditos: $creditSumValue\nSoma dos débitos: $debitSumValue"
        builder.setMessage(message)

        // Configura o botão "OK" no AlertDialog
        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        // Crie e exiba o AlertDialog
        val alertDialog = builder.create()
        alertDialog.show()

    }

    fun btShowLctosOnClick(view: View) {
        val intent = Intent( this, ListRegistersActivity::class.java )
        startActivity( intent )
    }
}