package pl.adamzieba.bmi


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.text.DecimalFormat
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    private var teHeightUserInput : EditText? = null
    private var teWeightUserInput : EditText? = null
    private var tvBMIResult : TextView? = null
    private var tvCategoryResult : TextView? = null
    private var btnCount : Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Przypisanie elementów frontendowych
        teHeightUserInput = findViewById(R.id.heightInput)
        teWeightUserInput = findViewById(R.id.weightInput)
        tvBMIResult = findViewById(R.id.resultOutput)
        btnCount = findViewById(R.id.excecuteButton)
        var calculatedBMI= 0.0

            //Listener pola Podaj wzrost, gdy user wprowadza wzrost
        teHeightUserInput?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                teHeightUserInput?.setBackgroundResource(R.drawable.border_style)
                Toast.makeText(this, "Wprowadź wzrost", Toast.LENGTH_SHORT).show()
            }else
                teHeightUserInput?.setBackgroundColor(ContextCompat.getColor(this, R.color.textBox))
        }
        //Listener pola "Podaj wagę", gdy user wprowadza wagę
        teWeightUserInput?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus){
                teWeightUserInput?.setBackgroundResource(R.drawable.border_style)
                Toast.makeText(this, "Wprowadź wagę", Toast.LENGTH_SHORT).show()
            }else
                teWeightUserInput?.setBackgroundColor(ContextCompat.getColor(this, R.color.textBox))
        }

            //Listener przycisku "przelicz"
        btnCount?.setOnClickListener {
            val heightInput : String = teHeightUserInput?.text.toString()
            val weightInput : String = teWeightUserInput?.text.toString()
            val isHeightInputDigital = checkIfOnlyDigitals(heightInput) // Sprawdzenie czy wprowadzona wartosc jest liczba
            val isWeightInputDigital = checkIfOnlyDigitals(weightInput) // Sprawdzenie czy wprowadzona wartosc jest liczba
            var areMeasurementsInScope = false
            val convertedHeightInput: Double
            val convertedWeightInput: Double
            var isBMICalculated = false

            //Jeżeli wartości są true to:
            if (isHeightInputDigital && isWeightInputDigital){
                convertedHeightInput = userCMtoM(heightInput) // Przekonwertowanie CM na M dla wzrostu
                convertedWeightInput = weightInput.toDouble()
                calculatedBMI = calculateBMI(convertedHeightInput, convertedWeightInput)  // Obliczenie BMI
                areMeasurementsInScope = areMeasurementsInScopeFun(convertedHeightInput, convertedWeightInput) // Sprawdzenie czy waga i wzrost mieszczą się w rozsądnych granicach.
            }else{
                Toast.makeText(this, "Wartość wzrostu oraz wagi musi być liczbą", Toast.LENGTH_SHORT).show()
            }
                // Jeżeli wszystko się zgadza to:
            if (areMeasurementsInScope){
                tvBMIResult?.text = calculatedBMI.toString() // Wyświetlenie tekstu w polu "Wynik BMI"
                isBMICalculated = true //ustawienie flagi
            } // EOIF



            tvCategoryResult = findViewById(R.id.descriptionOutput)
            val categoryDisplayed : String
             // Jeżeli flaga isBMICalculated = true to:
            if (isBMICalculated){
              categoryDisplayed = displayBMICategory(tvBMIResult?.text.toString().toDouble())
                tvCategoryResult?.text = categoryDisplayed // // wyświetelenie wyniku BMI w polu "Kategoria"
            }else{
                Toast.makeText(this, "Podaj poprawną wartość wzrostu lub wagi", Toast.LENGTH_SHORT).show()
            }


        } // setOnClickListener for btnCount


    } // onCreate closing bracelet




    private fun userCMtoM(heightInput: String): Double {
        return heightInput.toDouble() / 100
    }

    private fun checkIfOnlyDigitals(userInput: String) : Boolean{
        return  userInput.toDoubleOrNull()!=null
    }

    private fun areMeasurementsInScopeFun(heightInput : Double, weightInput : Double) : Boolean{

       return !((heightInput < 0.5 || heightInput > 3.0) || (weightInput < 20.0 || weightInput > 700.0))
    }

    private fun calculateBMI(height: Double, weight: Double) : Double{
        val df = DecimalFormat("#.##")
        return df.format((weight / height.pow(2))).toDouble()
    }

    private fun displayBMICategory(BMIResult : Double) : String{

        if (BMIResult < 16.0){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.starvationAlertTextView))
            return getString(R.string.categoryStarvation)
        }else if (BMIResult in 16.0..16.99){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.emaciationAlertTextView))
            return getString(R.string.categoryEmaciation)
        }else if (BMIResult in 17.0..18.49){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.underweightAlertTextView))
        }else if (BMIResult in 18.5..24.99){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.normalAlertTextView))
            return getString(R.string.categoryNormal)
        }else if (BMIResult in 25.0..29.99){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.overweightAlertTextView))
            return getString(R.string.categoryOverweight)
        }else if (BMIResult in 30.0..34.99){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.obesityOneAlertTextView))
            return getString(R.string.categoryObesityOne)
        }else if (BMIResult in 35.0..39.99){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.obesityTwoAlertTextView))
            return getString(R.string.categoryObesityTwo)
        }else if (BMIResult >= 40.0){
            tvCategoryResult?.setBackgroundColor(ContextCompat.getColor(this, R.color.obesityThreeAlertTextView))
            return getString((R.string.categoryObesityThree))
        }
           return "BMI is not in the scope"
    } // fun displayBMICategory EOF

}
