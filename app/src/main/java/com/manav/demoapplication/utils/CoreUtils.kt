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
import com.manav.demoapplication.response.ContactInfo
import com.manav.demoapplication.utils.CoreContextWrapper
import java.util.*
import java.util.regex.Pattern


object CoreUtils {
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
                    success(it.result)
                }
            }.addOnFailureListener {
                failure(it)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
                success(it.result?.token)
            }.addOnFailureListener {
                failure(it)
            }
        }
    }


    @SuppressLint("Range")
    fun readContacts(context: Context): ArrayList<ContactInfo> {
        try {
            /*val contactRequestModel: ContactRequestModel = ContactRequestModel()
            contactRequestModel.data.clear*/
            val contactInfo = ArrayList<ContactInfo>()
            val selection: String? = null
            val selectionArgs: Array<String>? = null

            val projectionToCheck = arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )


            val data = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projectionToCheck,
                selection,
                selectionArgs,
                ContactsContract.Contacts.SORT_KEY_PRIMARY
            )
            var contactData: ContactInfo
            val contactPhoneNumberIndex =
                data?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val contactNameIndex =
                data?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val contactId = data?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

            while (data != null && data.moveToNext()) {
                contactData = ContactInfo()
                val phoneNumber = data.getString(contactPhoneNumberIndex!!)
                contactData.name = data.getString(contactNameIndex!!)
                try {
                    if (contactData.name?.indexOf(" ") != -1) {
                        contactData.firstName =
                            contactData.name?.substring(0, contactData.name?.indexOf(" ") ?: 1)
                        contactData.lastName = contactData.name?.substring(
                            contactData.name?.indexOf(" ")!! + 1,
                            contactData.name?.length ?: 0
                        )
                    } else {
                        contactData.firstName = contactData.name
                        contactData.lastName = null
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }



                contactData.contactId = data.getString(contactId!!)

                val emailCur: Cursor? = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    arrayOf(contactData.contactId ?: ""),
                    null
                )
                while (emailCur != null && emailCur.moveToNext()) {
                    val email =
                        emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    contactData.email = email
                    break
                }
                emailCur?.close()


                if (phoneNumber.startsWith("0")) {
                    contactData.phone =
                        countryCode + phoneNumber.replaceFirst(
                            "^0+(?!$)",
                            ""
                        ).replace("[\\D]", "")
                            .replace(" ", "")
                            .replace("-", "")
                            .replace("(", "")
                            .replace(")", "")

                } else if (phoneNumber.startsWith("+")) {
                    val rawNumber: String = phoneNumber.substring(1)

                    contactData.phone =
                        "+" + rawNumber.replace(
                            "[\\D]".toRegex(),
                            ""
                        ).replace(" ", "")
                            .replace("-", "")
                            .replace("(", "")
                            .replace(")", "")
                } else {
                    contactData.phone =
                        countryCode + phoneNumber.replace(
                            "[\\D]",
                            ""
                        ).replace(" ", "")
                            .replace("-", "")
                            .replace("(", "")
                            .replace(")", "")
                }
                contactInfo.add(contactData)
                Log.d("PhoneNo", contactData.name + " " + contactData.phone)

            }
            data?.close()
            return contactInfo

        } catch (e: Exception) {
            Log.e("ContactsUpdate", "--" + e.message + "--" + e.cause)
            return ArrayList<ContactInfo>()
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