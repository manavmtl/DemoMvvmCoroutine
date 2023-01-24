import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.manav.demoapplication.CoreContextWrapper
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.regex.Pattern


object CoreUtils {
    private var countryListJsonData: String? = null
    private var countryCode = ""


    fun isInternetAvailable(context: Context? = CoreContextWrapper.getContext()): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            nwInfo.isConnected
        }
    }

    fun getTimeZoneoffset(): String {
        val mCalendar: Calendar = GregorianCalendar()
        val mTimeZone = mCalendar.timeZone
        return ((mTimeZone.rawOffset) / (1000 * 60)).toString()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    fun Activity.switchActivityClearAllWOData(className: Class<*>) {
        val intent = Intent(this, className)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        this.finishAffinity()
    }

    /*
* This method is used to check the validation for the email
* */
    fun isEmailValid(emailId: String): Boolean {
        val EMAIL_PATTERN = ("[A-Z0-9a-z.-_+]+[A-Z0-9a-z]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,50}")
        return if (emailId.length < 3 || emailId.length > 265) false else {
            emailId.matches(Regex(EMAIL_PATTERN))
        }
    }


    //This will check weather enter password contains one lower case, one upper case, one digit and one special symbol with atleast 8 characters
    fun isPasswordValid(password: String): Boolean {
        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )
        return passwordREGEX.matcher(password).matches()
    }


    fun validABN(abn: String): Boolean {
        val weighting = intArrayOf(10, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19)
        val new_abn = abn.replace("\\s".toRegex(), "")
        if (new_abn.length == 11) {
            var checksum = 0
            for (i in 0 until new_abn.length) {
                var posValue = Character.digit(new_abn[i], 10)
                // subtract 1 from first digit only
                if (i == 0) {
                    posValue--
                }
                // calculate value with position weighting
                checksum += posValue * weighting.get(i)
            }
            return checksum % 89 == 0
        }
        return false
    }


    fun String.isAtleastOneLowerCase() = matches(Regex(".*[a-z].*"))
    fun String.isAtleastOneUpperCase() = matches(Regex(".*[A-Z].*"))
    fun String.isAtleastOneDigit() = matches(Regex(".*\\d.*"))
    fun String.isAtleastOneSpecialCharacter() = matches(Regex(".*[!@#\$%^&*].*"))

    fun getDeviceToken(
        success: (String?) -> Unit,
        failure: (Exception?) -> Unit
    ) {
        try {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {

                    Log.i("token", it.result)
                    success(it.result)
                }
            }.addOnFailureListener {
                failure(it)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
                Log.i("token", it.result.token)
                success(it.result?.token)
            }.addOnFailureListener {
                failure(it)
            }
        }
    }

    /**
     * This method is used to hide keyboard
     *
     * @param mEditText view reference
     */
    fun hideKeyboard(mEditText: View, activity: Activity) {
        try {
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

}