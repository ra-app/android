package org.raapp.messenger.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder.Side.*
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.raapp.messenger.R

//import org.jetbrains.anko.*
//import org.jetbrains.anko.constraint.layout.constraintLayout
//

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        constraintLayout {
            padding = dip(20)

//            val logo = imageView {
//                id = R.id.icon
//
//                setImageResource(R.mipmap.ic_launcher)
//            }.lparams(width = wrapContent, height = wrapContent)

            val searchAttorney = button("Ich suche einen Anwalt"){
                id = R.id.audio_button
            }
            val registerFirm = button("Ich bin eine Kanzlei") {
                id = R.id.camera_button
            }

//            textView("Headline")

            applyConstraintSet {
                searchAttorney{
                    connect(
                            START to START of PARENT_ID,
                            TOP to TOP of PARENT_ID
                    )
                }
                registerFirm{
                    connect(
                            START to START of PARENT_ID,
                            TOP to BOTTOM of searchAttorney
                    )
                }
            }
        }
    }
}