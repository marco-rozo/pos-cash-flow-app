import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Mascara adaptada de: https://stackoverflow.com/questions/16889502/how-to-mask-an-edittext-to-show-the-dd-mm-yyyy-date-format
class DateMaskWatcher(private val editText: EditText) : TextWatcher {
    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val cal = Calendar.getInstance()

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (p0.toString() != current) {
            var clean = p0.toString().replace("[^\\d.]|\\.".toRegex(), "")
            val cleanC = current.replace("[^\\d.]|\\.", "")

            val cl = clean.length
            var sel = cl
            var i = 2
            while (i <= cl && i < 6) {
                sel++
                i += 2
            }
            //Fix for pressing delete next to a forward slash
            if (clean == cleanC) sel--

            if (clean.length < 8) {
                clean = clean + ddmmyyyy.substring(clean.length)
            } else {
                // Esta parte garante que quando terminarmos de inserir os números
                // a data está correta, corrigindo caso não esteja
                var day = Integer.parseInt(clean.substring(0, 2))
                var mon = Integer.parseInt(clean.substring(2, 4))
                var year = Integer.parseInt(clean.substring(4, 8))

                mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
                cal.set(Calendar.MONTH, mon - 1)
                year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                cal.set(Calendar.YEAR, year)
                // ^ primeiro defina o ano para que a linha abaixo funcione corretamente
                // com anos bissextos - caso contrário, data, por exemplo. 29/02/2012
                // seria corrigido automaticamente para 28/02/2012

                day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                clean = String.format("%02d%02d%02d", day, mon, year)
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8))

            sel = if (sel < 0) 0 else sel
            current = clean
            editText.setText(current)
            editText.setSelection(if (sel < current.count()) sel else current.count())
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable) {

    }

    // Função para verificar se a data é válida
     fun isValidDate(): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false  // Isso faz com que a validação seja rigorosa

        try {
            val dateString = editText.text.toString()
            // Tenta fazer o parsing da data
            dateFormat.parse(dateString)
            return true
        } catch (e: ParseException) {
            // A data não pôde ser parseada, é inválida
            return false
        }
    }
}