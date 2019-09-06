package com.tino.vote.widget

import android.animation.*
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.*
import androidx.core.content.ContextCompat
import com.tino.vote.R

class VoteView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var titleView: TextView
    private var optionsLayout: LinearLayout
    private var totalNumberView: TextView
    private lateinit var options: List<OptionInfo>
    private var mChose = false;

    init {
        inflate(context, R.layout.vote_layout, this).run {
            titleView = findViewById(R.id.title_text_view)
            optionsLayout = findViewById(R.id.options_layout)
            totalNumberView = findViewById(R.id.total_number_text_view)
        }
    }

    fun setData(chose: Boolean, title: String, totalNumber: Int, options: List<OptionInfo>) {
        mChose = chose
        titleView.text = title
        totalNumberView.text = "${totalNumber}人参与"
        this.options = options
        var current = 0
        for (index in options.indices) {
            if (options[index].chose) current = index
            (inflate(context, R.layout.vote_option_layout, optionsLayout) as? LinearLayout)?.getChildAt(index)?.let {
                var progressView: ProgressBar = it.findViewById(R.id.progress_view)
                val nameView: TextView = it.findViewById(R.id.name_text_view)
                val numberView: TextView = it.findViewById(R.id.number_text_view)
                nameView.text = options[index].name
                if (chose && options[index].chose) nameView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.chose_icon), null)
                numberView.alpha = if (chose) 1f else 0f
                numberView.text = "${options[index].number}人"
                it.setTag(R.string.tag_progress_view, progressView)
                it.setTag(R.string.tag_name_view, nameView)
                it.setTag(R.string.tag_number_view, numberView)
                it.setOnClickListener {
                    if (!mChose) {
                        mChose = true
                        nameView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.chose_icon), null)
                        vote(index, totalNumber)
                    } else {
                        Toast.makeText(context, "您已投过票", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if (chose) vote(current, totalNumber)
    }

    private fun vote(current: Int, totalNumber: Int) {
        optionsLayout.post {
            for (index in 0 until optionsLayout.childCount) {
                optionsLayout.getChildAt(index)?.let {
                    var progressView: ProgressBar = it.getTag(R.string.tag_progress_view) as ProgressBar
                    val nameView: TextView = it.getTag(R.string.tag_name_view) as TextView
                    val numberView: TextView = it.getTag(R.string.tag_number_view) as TextView
                    it.isSelected = current == index
                    progressView.isSelected = current == index
                    nameView.setTextColor(if (current == index) Color.parseColor("#2B8DE2") else Color.parseColor("#777777"))
                    numberView.setTextColor(nameView.currentTextColor)
                    AnimatorSet().run {
                        playTogether(arrayListOf<Animator>(ObjectAnimator.ofInt(progressView, "progress", (options[index].number.toFloat() / totalNumber.toFloat() * 100).toInt()),
                            ObjectAnimator.ofFloat(nameView, "x",  resources.getDimension(R.dimen.name_view_left_margin)),
                            ObjectAnimator.ofFloat(numberView, "alpha", 1f)).apply {
                            if (nameView.width > it.width - numberView.width - resources.getDimension(R.dimen.number_view_right_margin) - resources.getDimension(R.dimen.name_view_left_margin) - resources.getDimension(R.dimen.name_view_number_view_margin)) {
                                add(ObjectAnimator.ofInt(ViewWrapper(nameView), "customWidth", nameView.width, it.width - numberView.width - (resources.getDimension(R.dimen.number_view_right_margin) + resources.getDimension(R.dimen.name_view_left_margin) + resources.getDimension(R.dimen.name_view_number_view_margin)).toInt()))
                            }
                        })
                        duration = 800
                        start()
                    }
                }
            }
        }
    }

    data class OptionInfo(val name: String, val number: Int, var chose: Boolean)
}