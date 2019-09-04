package com.tino.vote.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tino.vote.R
import com.tino.vote.util.UIUtil

class VoteView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var titleView: TextView
    private var optionsLayout: LinearLayout
    private var totalNumberView: TextView
    private lateinit var options: List<OptionInfo>
    private var mChose = false;

    init {
        val view = inflate(context, R.layout.vote_layout, this)
        titleView = view.findViewById(R.id.title_text_view)
        optionsLayout = view.findViewById(R.id.options_layout)
        totalNumberView = view.findViewById(R.id.total_number_text_view)
    }

    fun setData(chose: Boolean, title: String, totalNumber: Int, options: List<OptionInfo>) {
        mChose = chose
        titleView.text = title
        totalNumberView.text = "${totalNumber}人参与"
        this.options = options
        var current = 0
        for (i in options.indices) {
            if (options[i].chose) current = i
            val optionView: View = inflate(context, R.layout.vote_option_layout, null)
            var progressView: View = optionView.findViewById(R.id.progress_view)
            val nameView: TextView = optionView.findViewById(R.id.name_text_view)
            val numberView: TextView = optionView.findViewById(R.id.number_text_view)
            if (chose) {
                numberView.alpha = 1f
                if (options[i].chose) nameView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.chose_icon), null)
            } else {
                numberView.alpha = 0f
            }
            nameView.text = options[i].name
            numberView.text = "${options[i].number}人"
            optionView.setTag(R.string.tag_progress_view, progressView)
            optionView.setTag(R.string.tag_name_view, nameView)
            optionView.setTag(R.string.tag_number_view, numberView)
            optionView.setOnClickListener {
                if (!mChose) {
                    mChose = true
                    nameView.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.mipmap.chose_icon), null)
                    vote(i, totalNumber)
                } else {
                    Toast.makeText(context, "您已投过票", Toast.LENGTH_SHORT).show()
                }
            }
            optionsLayout.addView(optionView)
        }
        if (chose) vote(current, totalNumber)
    }

    private fun vote(current: Int, totalNumber: Int) {
        optionsLayout.post {
            for (i in 0 until optionsLayout.childCount) {
                val optionView = optionsLayout.getChildAt(i)
                var progressView: View = optionView.getTag(R.string.tag_progress_view) as View
                val nameView: TextView = optionView.getTag(R.string.tag_name_view) as TextView
                val numberView: TextView = optionView.getTag(R.string.tag_number_view) as TextView
                numberView.visibility = View.VISIBLE
                if (current == i) {
                    progressView.isSelected = true
                    nameView.setTextColor(Color.parseColor("#2B8DE2"))
                    numberView.setTextColor(Color.parseColor("#2B8DE2"))
                } else {
                    progressView.isSelected = false
                    nameView.setTextColor(Color.parseColor("#999999"))
                    numberView.setTextColor(Color.parseColor("#999999"))
                }
                if (optionView.width - (options[i].number.toFloat() / totalNumber.toFloat()) * optionView.width > UIUtil.dip2px(context, 5f)) {
                    progressView.background = ContextCompat.getDrawable(context, R.drawable.progress_view_bg)
                } else {
                    progressView.background = ContextCompat.getDrawable(context, R.drawable.progress_view_bg2)
                }
                ObjectAnimator.ofInt(ViewWrapper(progressView), "customWidth", ((options[i].number.toFloat() / totalNumber.toFloat()) * optionView.width).toInt()).setDuration(1000).start()
                ObjectAnimator.ofFloat(nameView, "translationX", -(optionView.width - UIUtil.dip2px(context, 20f) - nameView.width) / 2f).setDuration(1000).start()
                ObjectAnimator.ofFloat(numberView, "alpha", 1f).setDuration(1000).start()
            }
        }
    }

    data class OptionInfo(val name: String, val number: Int, var chose: Boolean)
}