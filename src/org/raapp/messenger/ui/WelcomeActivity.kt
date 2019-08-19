package org.raapp.messenger.ui


import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.raapp.messenger.R
import org.thoughtcrime.securesms.RegistrationActivity


class WelcomeActivity : AppCompatActivity() {
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

                textView("Einfach Recht"){
                    textSize = 35f
                }
                textView("Die Kommunikations-App für Anwälte&Mandanten"){
                    textSize = 20f
                }.lparams{bottomMargin = dip(30)}

                themedButton("Weiter", R.style.ra_button_style){
                    background = resources.getDrawable(R.drawable.blue_to_green)

                    onClick {
                        startActivity<PermissonRequestActivity>()
                    }
                }.lparams(width= matchParent, height = dip(42))
            }.lparams(width = matchParent, height = matchParent) {
                topMargin = dip(100)
            }
        }
    }
}