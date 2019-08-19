package org.raapp.messenger.ui


import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder.Side.*
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.raapp.messenger.R


class TermsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout{
            verticalLayout {
                padding = dip(20)
                themedTextView("Allgemeine Geschäftsbedingungen", theme = R.style.header_text) {
                    textSize = 18f
                }
                themedTextView("Um fortzufahren, lesen Sie die AGB sorgfältig durch.", theme = R.style.header_text) {
                    textSize = 14f
                }
            }


            constraintLayout  {
                background = resources.getDrawable(R.color.white)

                padding = dip(20)


                val termsHeadline = textView("AGB"){
                    id = R.id.text
                    textSize = 18f
                    textColor = resources.getColor(R.color.blue)
                }
                val scrollText = scrollView {
                    id = R.id.scrollView
                    val termsText = textView("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut lacus velit, mollis vitae pellentesque vel, laoreet ac orci. Fusce consectetur elementum turpis, a aliquet arcu maximus sit amet. Integer consectetur orci a justo lobortis, nec placerat risus posuere. Nunc sed lacus non risus pellentesque tristique. Vestibulum erat dui, semper non hendrerit nec, finibus eu purus. Etiam mollis hendrerit odio. Sed eget metus tellus. Nunc dictum enim nulla, vitae iaculis ligula malesuada a. Nam feugiat pretium tellus, sit amet auctor eros consectetur a. Donec tristique ex felis, et mollis turpis ultricies eu. Duis efficitur congue nisl, a varius ex accumsan eu. Morbi laoreet lectus ac consectetur fringilla. Praesent viverra tincidunt velit, sed efficitur quam vehicula a. Proin vitae diam orci. Duis in nisl dolor. Vivamus lacus elit, commodo non erat at, commodo rutrum est. Quisque vestibulum neque id semper luctus. Fusce posuere scelerisque pharetra. Curabitur at elit eget felis aliquet molestie. Nam tortor mi, tincidunt nec fringilla eget, commodo et metus. Donec nunc dui, pulvinar vitae accumsan quis, suscipit ac sem. Quisque ac interdum nulla, nec lobortis odio. Morbi malesuada, nisl non lobortis ullamcorper, erat elit pretium massa, nec venenatis massa lorem sed nisi. Aliquam eget viverra leo. In id lectus quam. Quisque id consectetur metus. Nunc pulvinar faucibus metus, eu euismod justo vehicula euismod. Praesent quis libero enim. Etiam a elit non mauris consequat ultrices. Aliquam erat volutpat. Sed ex enim, venenatis ut aliquam eu, viverra sed urna. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut lacus velit, mollis vitae pellentesque vel, laoreet ac orci. Fusce consectetur elementum turpis, a aliquet arcu maximus sit amet. Integer consectetur orci a justo lobortis, nec placerat risus posuere. Nunc sed lacus non risus pellentesque tristique. Vestibulum erat dui, semper non hendrerit nec, finibus eu purus. Etiam mollis hendrerit odio. Sed eget metus tellus. Nunc dictum enim nulla, vitae iaculis ligula malesuada a. Nam feugiat pretium tellus, sit amet auctor eros consectetur a. Donec tristique ex felis, et mollis turpis ultricies eu. Duis efficitur congue nisl, a varius ex accumsan eu. Morbi laoreet lectus ac consectetur fringilla. Praesent viverra tincidunt velit, sed efficitur quam vehicula a. Proin vitae diam orci. Duis in nisl dolor. Vivamus lacus elit, commodo non erat at, commodo rutrum est. Quisque vestibulum neque id semper luctus. Fusce posuere scelerisque pharetra. Curabitur at elit eget felis aliquet molestie. Nam tortor mi, tincidunt nec fringilla eget, commodo et metus. Donec nunc dui, pulvinar vitae accumsan quis, suscipit ac sem. Quisque ac interdum nulla, nec lobortis odio. Morbi malesuada, nisl non lobortis ullamcorper, erat elit pretium massa, nec venenatis massa lorem sed nisi. Aliquam eget viverra leo. In id lectus quam. Quisque id consectetur metus. Nunc pulvinar faucibus metus, eu euismod justo vehicula euismod. Praesent quis libero enim. Etiam a elit non mauris consequat ultrices. Aliquam erat volutpat. Sed ex enim, venenatis ut aliquam eu, viverra sed urna."){
                        id = R.id.text2
                        textSize = 14f
                    }

                }.lparams(height = 0)

                val toggle = checkBox {
                    id = R.id.checkbox
                }

                val acceptanceText = textView("Ich akzeptiere die AGB."){
                    id = R.id.confirmation_text
                    padding = dip(10)
                    textSize = 14f

                }

                val continueButton = themedButton("Weiter", R.style.ra_button_style){
                    id = R.id.continue_button

                    background = resources.getDrawable(R.drawable.blue_to_green)

                    onClick {
                        startActivity<PermissonRequestActivity>()
                    }
                }.lparams(width= matchParent, height = dip(42))

                applyConstraintSet {
                    termsHeadline{
                        connect(START to START of ConstraintSet.PARENT_ID,
                                TOP to TOP of ConstraintSet.PARENT_ID
                        )
                    }
                    scrollText{
                        connect(START to START of ConstraintSet.PARENT_ID,
                                TOP to BOTTOM of termsHeadline,
                                BOTTOM to TOP of acceptanceText)
                    }
                    toggle{
                        connect(BOTTOM to TOP of continueButton,
                                START to START of ConstraintSet.PARENT_ID)
                    }


                    acceptanceText{
                        connect(BOTTOM to TOP of continueButton,
                                START to END of toggle)
                    }
                    continueButton{
                        connect(
                                BOTTOM to BOTTOM of ConstraintSet.PARENT_ID,
                                START to START of ConstraintSet.PARENT_ID
                        )
                    }
                }

            }.lparams(width = matchParent, height = matchParent) {
            }
        }
    }
}