import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.math.BigDecimal
import java.text.NumberFormat

class DecimalDigitsTextWatcher(private val editText: EditText) : TextWatcher {
    private var current: String = ""

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

//        if (s.toString() != current) {
//            editText.removeTextChangedListener(this)
//
//            val cleanString: String = s!!.replace("""[$,.]""".toRegex(), "")
//
//            val parsed = cleanString.toDouble()
//            val formatted = NumberFormat.getCurrencyInstance().format((parsed / 100))
//
//            current = formatted
//            editText.setText(formatted)
//            editText.setSelection(formatted.length)
//
//            editText.addTextChangedListener(this)
//            editText.isCursorVisible = false
//        }

        if (s.toString() != current) {
            editText.removeTextChangedListener(this)

            val cleanString = s.toString().replace("\\D".toRegex(), "")
            val parsed = BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(BigDecimal(100), BigDecimal.ROUND_FLOOR)

            val formated = NumberFormat.getCurrencyInstance().format(parsed);

            current = formated
            editText.setText(formated)
            editText.setSelection(formated.length)

            editText.addTextChangedListener(this)
        }
    }

}

