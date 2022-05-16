package das.losaparecidos.etzi.app.utils

import android.telephony.PhoneNumberUtils
import java.util.*


/*******************************************************************************
 ****                  User Input Validators and Formatters                 ****
 *******************************************************************************/

/**
 * Set of validators for user input. They all return a boolean indicating input's validity.
 *
 * Validators starting with "is..." check if the input fulfills, strictly, the required condition.
 * Validators starting with "canBe..." check if the inputs fulfills less strict conditions,
 * hopping that the input will be converted to a full valid input.
 *
 * Example: Spanish ZIP Code (5 numbers)
 * - 489 fulfills canBeZIP but not isZIP, when the user finishes entering the ZIP it will fulfill both.
 *
 *
 * Formatters return input string's formatted version.
 */

/*************************************************
 **                Basic Validators             **
 *************************************************/

fun isNumeric(number: String): Boolean = number.all { it.isDigit() }

fun isText(text: String) = text.all { it == ' ' || it.isLetter() }
fun isNonEmptyText(text: String) = text.isNotBlank() && isText(text)

fun isAlphaNumeric(text: String): Boolean = text.all { it.isLetterOrDigit() }


/*************************************************
 **        Username & Password Validators       **
 *************************************************/

//----------   Username Validators   -----------//
fun canBeValidUsername(username: String): Boolean = username.length == 6 && isAlphaNumeric(username)
fun isValidUsername(username: String): Boolean = username.isNotBlank() && canBeValidUsername(username)

//----------   Password Validators   -----------//
fun canBePassword(password: String): Boolean = password.length <= 100 && password.all { it != ' ' }
fun isValidPassword(password: String): Boolean = password.length in 5..100 && canBePassword(password)


/*************************************************
 **            Form's Field Validators          **
 *************************************************/

//----------   ZIP Code Validators   -----------//
fun canBeZIP(zip: String): Boolean = zip.length <= 5 && isNumeric(zip)
fun isZIP(zip: String): Boolean = zip.length == 5 && isNumeric(zip)


/*------------------------------------------------
|            Phone Number Validators             |
------------------------------------------------*/

fun canBePhoneNumber(phoneNumber: String): Boolean =
    phoneNumber.isEmpty() || ((phoneNumber[0].isDigit() || phoneNumber[0] == '+') && phoneNumber.drop(1).all { it.isDigit() || it == ' ' })

fun isValidPhoneNumber(phoneNumber: String, country: String = Locale.getDefault().country): Boolean =
    PhoneNumberUtils.formatNumberToE164(phoneNumber, country) != null

//---------   Phone Number Formatter   ---------//
fun formatPhoneNumber(phoneNumber: String, country: String = Locale.getDefault().country): String =
    PhoneNumberUtils.formatNumberToE164(phoneNumber, country) ?: phoneNumber


