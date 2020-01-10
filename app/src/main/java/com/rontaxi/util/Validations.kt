package com.rontaxi.util


import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.rontaxi.R
import es.dmoral.toasty.Toasty
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Validations {

    /**
     * check if EditText is Emptyor not
     */
    fun isEmpty(context: Context,et: TextInputEditText): Boolean {
        val data = et.text.toString().trim { it <= ' ' }

        if (data.isEmpty()) {

            var hint = ""

            if (et is TextInputEditText) {
                val parent = et.parent.parent as TextInputLayout
                hint = parent.hint!!.toString()
            } else {
                hint = et.hint!!.toString()
            }


            if (hint.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1) {
                setError(context,et, context.getString(R.string.pleaseEnter) + " " + hint.toLowerCase())
            } else {
                setError(context,et, context.getString(R.string.pleaseEnter) + " " + hint.toLowerCase())
            }

            return false
        }
        return true
    }

    /**
     * check if EditText is Emptyor not
     */
    fun isEmpty(context:Context,et: TextInputEditText, string: String): Boolean {
        val data = et.text.toString().trim { it <= ' ' }

        if (data.isEmpty()) {

            var hint = ""

            if (et is TextInputEditText) {
                val parent = et.parent.parent as TextInputLayout
                hint = parent.hint!!.toString()
            } else {
                hint = et.hint!!.toString()
            }


            if (hint.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1) {
                setError(context,et, string)
            } else {
                setError(context,et, string)
            }

            return false
        }
        return true
    }


    /**
     * check if TextView is Emptyor not
     */
    fun isEmpty(context: Context,et: TextView): Boolean {
        val data = et.text.toString().trim { it <= ' ' }

        if (data.length == 0) {
            val parent = et.parent.parent as TextInputLayout
            val hint = parent.hint!!.toString()
            if (hint.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1) {
                setError(context,et, context.getString(R.string.pleaseEnter) + " " + hint)
            } else {
                setError(context,et, context.getString(R.string.pleaseEnter) + " " + hint)
            }



            return false
        }
        return true
    }



    /**
     * Check if Email Address is valid or not
     */
    fun isValidEmail(context: Context,et: TextInputEditText): Boolean {
        if (!isEmpty(context,et)) {
            return false
        }

        val email = et.text.toString().trim { it <= ' ' }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || isValidNumber2(context,et)) {

            setError(context,et, "" + context.getString(R.string.validEmail))
            return false
        }

        return true
    }


    /**
     * Check if Password Entered is valid or not
     * checks Password must contain alphanumeric Characters
     */
    fun isValidPassword(context: Context,et: TextInputEditText): Boolean {
        if (!isEmpty(context,et)) {
            return false
        }
        val password = et.text.toString().trim { it <= ' ' }

        if (password.length > 5) {
            return true
        } else {
            setError(context,et, context.getString(R.string.validPass))
            return false
        }
    }

    fun isAlphaNumeric(context: Context,et: TextInputEditText): Boolean {

        var containsAlphabet=false
        var containsNumber=false

        containsAlphabet= containsAlphabets(et.text.toString().trim())
        containsNumber= Validations.containsNumber(et.text.toString().trim())

        if(containsAlphabet && containsNumber){

            return true
        }
        Toasty.error(context,context.getString(R.string.pass_alpha_numeric)).show()

        return false



       /* var s= et.text.toString()

        for (i in 0 until s.length) {
            val c = s[i]
            if (c.toInt() < 0x30 || c.toInt() >= 0x3a && c.toInt() <= 0x40 || c.toInt() > 0x5a && c.toInt() <= 0x60 || c.toInt() > 0x7a)
                Toasty.error(context!!,context.getString(R.string.pass_alpha_numeric)).show()
                return false
        }*/
       // return true


        /*if(s.matches(Regex("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$"))){


            return true
        }
        Toasty.error(context!!,context.getString(R.string.pass_alpha_numeric)).show()
        return false*/


        /*for (i in 0 until s.length) {
            val c = s[i]
            if (!Character.isDigit(c) && !Character.isLetter(c)){
                Toasty.error(context!!,context.getString(R.string.pass_alpha_numeric))

                return false
            }

        }

        return true*/



        /*var bool=s != null && s.matches("^[a-zA-Z0-9]*$".toRegex())

        if(!bool){

            Toasty.error(context!!,context.getString(R.string.pass_alpha_numeric))
        }

        return bool*/
    }

    /**
     * Check if Password Entered is valid or not
     * checks Password must contain alphanumeric Characters
     */
    fun isValidConfirmPassword(context: Context,et: TextInputEditText, message: String): Boolean {
        if (!isEmptyPass(context,et, message)) {
            return false
        }
        return true
    }


    /**
     * check if EditText is Emptyor not
     */
    fun isEmptyPass(context: Context,et: TextInputEditText, message: String): Boolean {
        val data = et.text.toString().trim { it <= ' ' }

        if (data.isEmpty()) {

            var hint = ""
            var parent: TextInputLayout? = null
            if (et is TextInputEditText) {
                parent = et.parent.parent as TextInputLayout
                hint = parent.hint!!.toString()
            } else {
                hint = et.hint!!.toString()
            }

            if (hint.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1) {
                if (parent == null)
                    setError(context,et, context.getString(R.string.please) + " " + message)
                else
                    setError(context,parent, context.getString(R.string.please) + " " + message)
            } else {
                setError(context,et, message)
            }

            return false
        }
        return true
    }

    /**
     * check if Username Entered is Valid or not
     */
    fun isValidUsername(context: Context,et: TextInputEditText): Boolean {

        if (!isEmpty(context,et)) {
            return false
        }
        val userName = et.text.toString().trim { it <= ' ' }

        if (userName.matches("[a-zA-Z\\s]+".toRegex())) {
            if (Character.isUpperCase(userName[0])) {
                //                if (userName.length() >= 3) {
                var count = 0
                var flagusSuccess = true
                for (i in 1 until userName.length) {
                    if (userName[i] == ' ' && userName[i + 1] == ' ') {
                        setError(context,et, context.getString(R.string.onlyOneSpace))

                        flagusSuccess = false
                    }

                    if (userName[i] == ' ') {
                        count++
                        if (count > 2) {
                            setError(context,et, context.getString(R.string.validUserName))
                            flagusSuccess = false
                        }
                    }
                }
                if (flagusSuccess) {
                    return true
                }
                //                }
            }
            //            setError(et,"" + context.getString(R.string.firstLetterCap));
            return true
        } else {
            setError(context,et, context.getString(R.string.validUserName))
            return false
        }
    }

    /**
     * check if Username Entered is Valid or not
     */
    fun isValidUsername(context: Context,et: TextInputEditText, string: String): Boolean {

        if (!isEmpty(context,et, string)) {
            return false
        }
        val userName = et.text.toString().trim { it <= ' ' }

        if (userName.matches("[a-zA-Z\\s]+".toRegex())) {
            if (Character.isUpperCase(userName[0])) {
                //                if (userName.length() >= 3) {
                var count = 0
                var flagusSuccess = true
                for (i in 1..userName.length - 1) {
                    if (userName[i] == ' ' && userName[i + 1] == ' ') {
                        setError(context,et, context.getString(R.string.onlyOneSpace))

                        flagusSuccess = false
                    }

                    if (userName[i] == ' ') {
                        count++
                        if (count > 2) {
                            setError(context,et, context.getString(R.string.validUserName))
                            flagusSuccess = false
                        }
                    }
                }
                if (flagusSuccess) {
                    return true
                }
                //                }
            }
            //            setError(et,"" + context.getString(R.string.firstLetterCap));
            return true
        } else {
            setError(context,et, context.getString(R.string.validUserName))
            return false
        }
    }
 

    fun isTermsAccepted(checkBox: CheckBox, context: Context): Boolean {

        if (!checkBox.isChecked) {

            Toasty.error(context,context.getString(R.string.termCondition)).show()

         //   showAlert(context,context.getString(R.string.termCondition),context.getString(R.string.ok),{})

            //checkBox.error = "Please accept the Terms & Conditions by clicking on checkbox."
            return false
        }
        //checkBox.error=null

        return true

    }

    fun isEmptyCustom(context: Context,et: TextInputEditText, hint: String): Boolean {
        val data = et.text.toString().trim { it <= ' ' }

        if (data.isEmpty()) {
            setError(context,et, hint + " " + context.getString(R.string.contactNumber).toLowerCase())
            return false
        }
        return true
    }


    /**
     * check if PhoneNumber Entered is Valid or not and
     */
    fun isValidNumber2(context: Context,et: TextInputEditText): Boolean {
        if (!isEmptyCustom(context,et, "Please")) {
            return false
        }
        val number = et.text.toString().trim { it <= ' ' }

        try {
            if (!number.matches("[0]*[1-9][0-9]*".toRegex())) {
                // et.error = "" + context.getString(R.string.validPhone)
                return false
            } else if (number.length < 6 || number.length > 10) {
                // setError(et, "" + context.getString(R.string.phoneLength))
                return false
            }
            return true
        } catch (ex: NumberFormatException) {
            //  setError(et, "" + context.getString(R.string.validPhone))
            return false
        }

    }


    /**
     * check if PhoneNumber Entered is Valid or not and
     */
    fun isValidPhoneNumber(context: Context,et: TextInputEditText): Boolean {
        if (!isEmptyCustom(context,et, "Please")) {
            return false
        }
        val number = et.text.toString().trim { it <= ' ' }

        try {
            if (!number.matches("[0]*[1-9][0-9]*".toRegex())) {
                et.error = "" + context.getString(R.string.validPhone)
                return false
            } else if (number.length < 8 || number.length > 16) {
                setError(context,et, "" + context.getString(R.string.validPhone))
                return false
            }
            return true
        } catch (ex: NumberFormatException) {
            setError(context,et, "" + context.getString(R.string.validPhone))
            return false
        }

    }


    /**
     * check if CardNumber Entered is valid or not
     */
    fun isValidCardNumber(context: Context,et: TextInputEditText): Boolean {

        val number = et.text.toString().trim { it <= ' ' }
        try {
            if (!number.matches("[0]*[1-9][0-9]*".toRegex())) {
                setError(context,et, "Enter valid Card Number")
                return false
            } else if (number.length < 16) {
                setError(context,et, "CardNumber must contain atleast 16 digits")
                return false
            }
            return true
        } catch (ex: NumberFormatException) {
            setError(context,et, "Enter valid Number")
            return false
        }

    }

    /**
     * Checks if Expiry Date Entered is valid or not
     */
    fun isValidExpiryDate(context: Context,et: TextInputEditText): Boolean {

        val expiryDate = et.text.toString().trim { it <= ' ' }
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date1 = Date()
        val date = dateFormat.format(date1)
        println("date.." + date)
        if (!expiryDate.matches("^\\d{4}/\\d{2}/\\d{2}$".toRegex())) {
            setError(context,et, "Date Format is YYYY/MM/dd")
            return false
        } else if (expiryDate == date) {
            setError(context,et, "Expiry date can't be the Current date")
            return false
        } else if (!expiryDate.matches("(((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01]))".toRegex())) {
            setError(context,et, "Enter valid date")
            return false
        }
        return true
    }

    /**
     * checks if User entered Valid CVV Number
     */

    fun isValidCVV(context: Context,cvv: String, et: EditText): Boolean {


        try {
            val num = Integer.parseInt(cvv)

            if (!cvv.matches("[0]*[1-9][0-9]*".toRegex())) {
                setError(context,et, "Enter valid CVV Number")
                return false
            } else if (cvv.length != 3) {
                setError(context,et, "CVV Number must contain 3 digits")
                return false
            }
            return true
        } catch (ex: NumberFormatException) {
            setError(context,et, "Enter valid CVV Number")
            return false
        }

    }

    /**
     * checks if user Entered valid PinCode Number
     */

    fun isValidZIPCode(context: Context,et: EditText): Boolean {
        val zipCode = et.text.toString().trim { it <= ' ' }

        if (!isEmpty(context,et)) {
            return false
        }

        return true

    }

    /**
     * checks if any EditText contains error if yes then return focus to it
     */
    fun setError(context: Context,et: View, s: String) {

        if(et is EditText) {
            Toasty.error(context,s).show()
            et.requestFocus()
        }
        else if(et is TextInputLayout)
        {
            Toasty.error(context,s).show()
            et.requestFocus()
        }
    }

    /**
     * checks if any TextView contains error if yes then return focus to it
     */
    fun setError(context: Context,et: TextInputEditText, s: String) {

        Toasty.error(context,s).show()

       // et.error = s
        et.requestFocus()
    }

    /**
     * checks if password matches or not
     */
    fun confirmPassword(context: Context,etNewPass: EditText, etConfirmPass: EditText): Boolean {

        val oldPassword = etNewPass.text.toString().trim { it <= ' ' }
        val newPassword = etConfirmPass.text.toString().trim { it <= ' ' }

        if (!oldPassword.matches(newPassword.toRegex())) {

            Toasty.error(context!!,context.getString(R.string.passMatch)).show()
            return false
        }
        return true
    }

    fun isValidImage(context: Context,outputFile:String?): Boolean {

        if (outputFile.isNullOrEmpty()) {
            Toasty.error(context,context.getString(R.string.addImage)).show()


          //  showAlert(context,context.getString(R.string.addImage),context.getString(R.string.ok),{})
        } else
            return true
        return false
    }



    fun capFirstLetterName(name: String): String {
            return (name[0] + "").toUpperCase() + name.substring(1)
    }



    fun isValidFile(context: Context, path: String?, message: String): Boolean{

        if(path.isNullOrEmpty()){
            Toasty.error(context!!,message).show()
            return false
        }

        return true
    }


    fun isAlphanumeric(str: String): Boolean {
        for (i in 0 until str.length) {
            val c = str[i]
            if (c.toInt() < 0x30 || c.toInt() >= 0x3a && c.toInt() <= 0x40 || c.toInt() > 0x5a && c.toInt() <= 0x60 || c.toInt() > 0x7a)
                return false
        }
        return true
    }


    fun containsAlphabets(string: String): Boolean{


        var pattern=Pattern.compile(".*[a-zA-Z]+.*")

        var matcher=pattern.matcher(string)

        if(matcher.matches()){
            return true
        }

        return false

    }


    fun containsNumber(string: String): Boolean{


        var pattern=Pattern.compile("(.)*(\\d)(.)*")

        var matcher=pattern.matcher(string)

        if(matcher.matches()){
            return true
        }

        return false

    }

    fun isEmpty(context: Context,et: EditText): Boolean {
        val data = et.text.toString().trim { it <= ' ' }

        if (data.isEmpty()) {
           setError(context,et,context.getString(R.string.please_enter_the_amount))
            return false
        }
        return true

    }


}