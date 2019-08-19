package org.raapp.messenger.ui


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.raapp.messenger.R
import org.thoughtcrime.securesms.RegistrationActivity
import org.thoughtcrime.securesms.logging.Log
import org.thoughtcrime.securesms.permissions.Permissions


class PermissonRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout{
            linearLayout  {
                background = resources.getDrawable(R.color.white)
                orientation = LinearLayout.VERTICAL

                padding = dip(20)

                imageView {
                    setImageResource(R.drawable.ra_icon_1024)
                }.lparams(width = dip(200), height = dip(200))


                textView("RA-App benötigt einen Zugriff zu Ihren Kontakten, Kamera und Fotos, um zu kommunizieren, Benachrichtigung zu erhalten und sichere Anrufe zu tätigen."){
                    textSize = 20f
                }.lparams{bottomMargin = dip(30)}

                themedButton("Berechtigungen gewähren", R.style.ra_button_style){
                    background = resources.getDrawable(R.drawable.blue_to_green)

                    onClick {
                        requestPermissions()
                    }
                }.lparams(width= matchParent, height = dip(42))

                themedButton("Nicht jetzt", theme = R.style.ra_transparent_button_style){
                    backgroundColor = resources.getColor(R.color.transparent)
                    gravity = left

                    onClick {
                        startActivity<RegistrationActivity>()
                    }

                }



            }.lparams(width = matchParent, height = matchParent) {
                topMargin = dip(100)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                requestWriteContactPermissios()
            }
            MY_PERMISSIONS_REQUEST_WRITE_CONTACTS ->{
                requestPhoneStatePermissios()
            }
            MY_PERMISSIONS_REQUEST_PHONE_STATE ->{
                startActivity<RegistrationActivity>()
            }

            else -> {
            }
        }
    }


    companion object{
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
        val MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 2
        val MY_PERMISSIONS_REQUEST_PHONE_STATE = 3


    }

    fun requestReadContactPermissios(){
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS)

        } else {
            requestWriteContactPermissios()
        }
    }

    fun requestWriteContactPermissios(){
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_CONTACTS),
                    MY_PERMISSIONS_REQUEST_WRITE_CONTACTS)

        } else {
            requestPhoneStatePermissios()
        }
    }

    fun requestPhoneStatePermissios(){
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    MY_PERMISSIONS_REQUEST_PHONE_STATE)

        } else {
            startActivity<RegistrationActivity>()
        }
    }

    fun requestPermissions(){
        requestReadContactPermissios()
    }
}