package es.iessaladillo.pedrojoya.exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.exchange.SoftInputUtils.hideSoftKeyboard
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding
import java.lang.Exception
import kotlin.Throws

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var txtAmountWatcher: TextWatcher
    private var getTagFromRadioButton = Currency.EURO
    private var getTagToRadioButton = Currency.DOLLAR


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialStage()
        setupViews()


    }

    private fun initialStage() {
        binding.rdbDollar.tag = Currency.DOLLAR
        binding.rdbDollar2.tag = Currency.DOLLAR
        binding.rdbPound.tag = Currency.POUND

        binding.rdbPound2.tag = Currency.POUND
        binding.rdbEuro.tag = Currency.EURO
        binding.rdbEuro2.tag = Currency.EURO

        binding.txtAmount.selectAll()
        binding.rdbEuro2.isEnabled = false
        binding.rdbDollar.isEnabled = false
    }


    fun setupViews() {
        binding.rdgActualCurrency.setOnCheckedChangeListener { rdg, selectButton ->
            radiobuttonFromCurrentChangeChecked(rdg, selectButton, binding.imageCurrency)
        }
        binding.rdgToCurrency.setOnCheckedChangeListener { rdg, selectButton ->
            radiobuttonToCurrentChangeChecked(rdg, selectButton, binding.imageCurrency2)
        }
        binding.btnExchange.setOnClickListener { conversionOnClick() }


        txtAmountWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isBlank()) {
                    binding.txtAmount.text = Editable.Factory.getInstance().newEditable("0")
                    binding.txtAmount.selectAll()
                }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        }
        binding.txtAmount.addTextChangedListener(txtAmountWatcher)
        binding.txtAmount.setOnEditorActionListener(TextView.OnEditorActionListener{_,_,_ -> conversionOnClick()})
    }

    private fun conversionOnClick():Boolean {
        hideSoftKeyboard(binding.root)
        Toast.makeText(
            this,
            getString(
                R.string.result,
                binding.txtAmount.text.toString(),
                getTagFromRadioButton.symbol,
                conversionAmount(), getTagToRadioButton.symbol
            ),
            Toast.LENGTH_SHORT
        ).show()
        return true
    }

    private fun conversionAmount(): Double {
        var amount: Double = binding.txtAmount.text.toString().toDouble()
        when {
            binding.rdbDollar.isChecked -> {
                when {
                    binding.rdbPound2.isChecked -> amount = Currency.POUND.fromDollar(amount)
                    binding.rdbEuro2.isChecked -> amount = Currency.EURO.fromDollar(amount)
                }
            }
            binding.rdbEuro.isChecked -> {
                amount = Currency.POUND.toDollar(amount)
                when {
                    binding.rdbPound2.isChecked -> amount = Currency.EURO.fromDollar(amount)
                }
            }
            binding.rdbPound.isChecked -> {
                amount = Currency.POUND.toDollar(amount)
                when {
                    binding.rdbEuro2.isChecked -> amount = Currency.EURO.fromDollar(amount)
                }
            }
        }
        return amount
    }

    private fun getSelectedRadioButton(rdg: RadioGroup, selectedButton: Int): RadioButton {
        val selectedRadioButton = when (selectedButton) {
            rdg.getChildAt(0).id -> rdg.getChildAt(0) as RadioButton
            rdg.getChildAt(1).id -> rdg.getChildAt(1) as RadioButton
            rdg.getChildAt(2).id -> rdg.getChildAt(2) as RadioButton
            else -> throw Exception("Wrong id")
        }
        return selectedRadioButton
    }

    private fun radiobuttonFromCurrentChangeChecked(
        rdg: RadioGroup,
        selectButton: Int,
        imageCurrency: ImageView
    ) {
        getTagFromRadioButton = getSelectedRadioButton(rdg, selectButton).tag as Currency
        imageCurrency.setImageResource(getTagFromRadioButton.drawableResId)
        for (i in 0 until binding.rdgToCurrency.childCount) {
            binding.rdgToCurrency.getChildAt(i).isEnabled =
                binding.rdgToCurrency.getChildAt(i).tag != getTagFromRadioButton
        }
    }

    private fun radiobuttonToCurrentChangeChecked(
        rdg: RadioGroup,
        selectButton: Int,
        imageCurrency: ImageView
    ) {
        getTagToRadioButton = getSelectedRadioButton(rdg, selectButton).tag as Currency
        imageCurrency.setImageResource(getTagToRadioButton.drawableResId)
        for (i in 0 until binding.rdgActualCurrency.childCount) {
            binding.rdgActualCurrency.getChildAt(i).isEnabled =
                binding.rdgActualCurrency.getChildAt(i).tag != getTagToRadioButton
        }


    }

}


