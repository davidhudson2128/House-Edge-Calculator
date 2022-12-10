package com.odds.bet


import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.InputType
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.round
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    var oddsLinearLayout: LinearLayout? = null
    var oddsOfWinningLayout: LinearLayout? = null
    var listOfTextEditText: MutableList<EditText>? = null
    var listOfOddsOfWinning: MutableList<TextView>? = null
    var odds: MutableList<Int>? = null
    lateinit var enterButton: Button
    lateinit var addButton: Button
    lateinit var clearButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listOfTextEditText = mutableListOf()
        listOfOddsOfWinning = mutableListOf()

        oddsLinearLayout = findViewById(R.id.OddsLinearLayout)
        oddsOfWinningLayout = findViewById(R.id.OddsOfWinningLinearLayout)

        setupButtons()

        addButton.visibility = View.GONE
        clearButton.translationY = -30f
        enterButton.translationY = 0f

    }

    fun setupButtons() {

        enterButton = findViewById(R.id.enter_button)
        enterButton.setOnClickListener {

            // Get text
            val editTextMainInput = findViewById<EditText>(R.id.editTextNumberSigned)
            val input = editTextMainInput.text.toString()
            editTextMainInput.text = null

            if(input == ""){
                println("TRUE TEST")
            }else{
                println("FALSE TEST")
            }

            if (handleInvalidString(input)){
                clearOdds()
                addButton.visibility = View.VISIBLE
                enterButton.translationY = -30f
                clearButton.translationY = -60f


                odds = parseInputToOddsList(input)

                // Create New Views
                for (odd in odds!!) {
                    createNewOddView(odd)
                }

                val houseEdgeTextView: TextView = findViewById(R.id.HouseEdgeTextView)
                val houseEdge = getHouseEdge()
                houseEdgeTextView.setText("House edge: $houseEdge%")


            }

        }

        clearButton = findViewById(R.id.clear_button)
        clearButton.setOnClickListener {
            addButton.visibility = View.GONE
            clearButton.translationY = -30f
            enterButton.translationY = 0f
            clearOdds()
        }

        val editText: EditText = findViewById(R.id.editTextNumberSigned)
        editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_NEXT) {
                    enterButton.performClick()

                    hideSoftKeyboard()
                    editText.clearFocus()
                    editText.isCursorVisible = false
                    return true;
                }
                return false;
            }

        })

        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener {
            addNewOdds()
        }
    }

    fun addNewOdds(){

        val editTextMainInput = findViewById<EditText>(R.id.editTextNumberSigned)
        val input = editTextMainInput.text.toString()
        editTextMainInput.text = null

        if(handleInvalidString(input)) {

            val newOddsList = mutableListOf<Int>()
            for (editText in listOfTextEditText!!) {
                newOddsList.add(editText.text.toString().toInt())
            }

            val newOddsToAdd = parseInputToOddsList(input)
            for (odds in newOddsToAdd) {
                newOddsList.add(odds)
            }

            clearOdds()
            odds = newOddsList

            // Create New Views
            for (odd in odds!!) {
                createNewOddView(odd)
            }

            val houseEdgeTextView: TextView = findViewById(R.id.HouseEdgeTextView)
            val houseEdge = getHouseEdge()

            println("House edge: $houseEdge")

            houseEdgeTextView.setText("House edge: $houseEdge%")

        }

    }

    fun clearOdds(){

        oddsLinearLayout!!.removeAllViews()
        oddsOfWinningLayout!!.removeAllViews()
        listOfTextEditText!!.clear()
        listOfOddsOfWinning!!.clear()

        val houseEdgeTextView: TextView = findViewById(R.id.HouseEdgeTextView)
        houseEdgeTextView.setText("")
    }

    fun getImpliedProbability(odd: Int): Float{

        var impliedProb: Float = 0f
        if (odd <= 0){

            impliedProb = (-odd.toFloat() / (-odd.toFloat() + 100f) * 100f)
        }else{

            impliedProb = (100f / (odd.toFloat() + 100f) * 100f)
        }
        return impliedProb
    }
    fun handleInvalidString(input: String):Boolean{

        val houseEdgeTextView: TextView = findViewById(R.id.HouseEdgeTextView)

        if(input.length == 0){
            if (listOfTextEditText!!.size != 0){
                updateOdds()
            }
            return false
        }

        if (input.length == 1){
            if ((input==",") or (input=="-")){
                houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            }
            return false
        }
        if (input.length == 2){
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            return false
        }

        if (hasRepeatingCommas(input)){
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            return false
        }
        if (hasRepeatingMinus(input)){
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            return false
        }
        if (hasMinusCommaOrCommaMinus(input)){
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            return false
        }
        if (hasNumMinusFormat(input)){
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            return false
        }

        if(houseEdgeTextView.text.toString() == "House edge: -100.00%"){
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
            return false
        }
        return true
    }
    fun hasNumMinusFormat(input: String):Boolean{
        var previousChar: Char = 'x'

        for (char in input){
            if((char=='-') && ((previousChar=='0') or (previousChar=='1') or (previousChar=='2') or (previousChar=='3') or (previousChar=='4') or (previousChar=='5') or (previousChar=='6') or (previousChar=='7') or (previousChar=='8') or (previousChar=='9') )){
                return true
            }
            previousChar = char
        }

        return false

    }
    fun hasMinusCommaOrCommaMinus(input: String):Boolean{

        if ((input[0]=='-') && ((input[1]==','))){
            return true
        }
        if ((input[0]==',') && ((input[1]=='-'))){
            return true
        }
        return false
    }
    fun hasRepeatingCommas(input: String): Boolean{

        var previousChar: Char = 'x'

        for (char in input){
            if ((char==',') && (previousChar==',')){
                return true
            }
            previousChar = char
        }
        return false

    }
    fun hasRepeatingMinus(input: String): Boolean{

        var previousChar: Char = 'x'

        for (char in input){
            if ((char=='-') && (previousChar=='-')){
                return true
            }
            previousChar = char
        }
        return false

    }

    fun parseInputToOddsList(input: String): MutableList<Int>{
        var oddsList = mutableListOf<Int>()



        try {
            var builderString = ""
            for ((i, char) in input.withIndex()){

                if(char == ' '){
                    continue
                }

                if ((char == ',') or (i == input.length - 1)){
                    if (i == input.length-1) {
                        builderString += char.toString()
                    }
                    oddsList.add(builderString.toInt())
                    builderString = ""
                }else{
                    builderString += char.toString()
                }
            }
        }
        catch (e: NumberFormatException){
            val houseEdgeTextView: TextView = findViewById(R.id.HouseEdgeTextView)
            houseEdgeTextView.setText(getString(R.string.invalid_input_string))
        }

        return oddsList

    }

    fun updateOdds(){

        val newOddsList = mutableListOf<Int>()
        for (editText in listOfTextEditText!!){
            if (editText.text.toString() != "") {
                newOddsList.add(editText.text.toString().toInt())
            }
        }

        clearOdds()
        odds = newOddsList

        // Create New Views
        for (odd in odds!!) {
            createNewOddView(odd)
        }

        val houseEdgeTextView: TextView = findViewById(R.id.HouseEdgeTextView)
        val houseEdge = getHouseEdge()
        houseEdgeTextView.setText("House edge: $houseEdge%")


    }

    fun getHouseEdge(): String{
        var houseEdge = 0f

        var totalPayout = 0f

        var hashMapOddsToBetAmount: HashMap<Int, Float> = hashMapOf()



        for (i in odds!!.indices){
            if (i == 0){
                hashMapOddsToBetAmount[odds!![i]] = 100f
                totalPayout = getPayoutOfOdds(odds!![i]) + 100f
                continue
            }
            hashMapOddsToBetAmount[odds!![i]] = getBetAmount(odds!![i], totalPayout)
        }


        var totalMoneyBet = 0f
        for (odds in odds!!){
            totalMoneyBet += hashMapOddsToBetAmount[odds]!!
        }
        houseEdge = ((totalMoneyBet - totalPayout)/totalMoneyBet) *100


        var houseEdgeStr = (((houseEdge*100).roundToInt().toFloat())/100f).toString()
        return houseEdgeStr
    }

    fun getPayoutOfOdds(odds: Int): Float{
        var payout = 0f

        var betSize = 100f

        if (odds <= 0){
            payout = ((betSize)/((-1 * odds.toFloat())/100f))
        }else{
            payout = ((odds.toFloat())/100f)*betSize
        }

        return payout
    }

    fun getBetAmount(odds: Int, totalPayout: Float): Float{

        var betAmount = 0f


        if (odds <= 0){
            betAmount = totalPayout*(-1*odds.toFloat()) / (100 + (-1*odds.toFloat()))
        }else{
            betAmount = totalPayout/((odds.toFloat()/100f)+1)
        }

        return betAmount
    }

    fun createNewOddView(odd: Int){


        val dpSize = round(360 / this.resources.displayMetrics.density).toInt()

        // Create new views
        val newTextContainer: TextInputLayout = TextInputLayout(ContextThemeWrapper(this, R.style.text_input_greeting))
        var param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        param.setMargins(10,0,10,0)
        newTextContainer.layoutParams = param

        val newEditText: EditText = EditText(this)
        var param2: LinearLayout.LayoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpSize)
        newEditText.layoutParams = param2
        newEditText.setText(odd.toString())
        newEditText.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED)

        newEditText.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // if the event is a key down event on the enter button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    updateOdds()
                    return true
                }
                return false
            }
        })

        newTextContainer.addView(newEditText)
        oddsLinearLayout!!.addView(newTextContainer)

        listOfTextEditText!!.add(newEditText)


        // ODDS OF WINNING


        val newTextView: TextView = TextView(this)
        var param3: LinearLayout.LayoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,dpSize)
        newTextView.layoutParams = param3
        newTextView.gravity = Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        newTextView.setText(getWinPercent(odd))


        oddsOfWinningLayout!!.addView(newTextView)





    }
    fun getWinPercent(oddToCheck: Int):String{

        var sumOfImpliedProbabilities: Float = 0f
        for (odd in odds!!){
            sumOfImpliedProbabilities += getImpliedProbability(odd)
        }
        var winPercent = getImpliedProbability(oddToCheck) / sumOfImpliedProbabilities
        winPercent = (((winPercent * 10000).roundToInt()).toFloat()/100f)


        return "$winPercent%"
    }
}
fun Activity.hideSoftKeyboard(){
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}