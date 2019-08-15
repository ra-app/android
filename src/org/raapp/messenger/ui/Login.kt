package org.raapp.messenger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView

//import org.jetbrains.anko.*
//import org.jetbrains.anko.constraint.layout.constraintLayout
//

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        constraintLayout {
            padding = dip(20)

//            val logo = imageView {
//
//
//                id = R.id.logo
//
//                setImageResource(R.drawable.ra_icon_white_back_full_200)
//            }.lparams(width = wrapContent, height = wrapContent)
//
            val searchAttorney = button("Ich suche einen Anwalt"){
//                id = R.id.button1
            }
            val registerFirm = button("Ich bin eine Kanzlei") {
//                id = R.id.button2
            }

            textView("Headline")
        }
    }
}