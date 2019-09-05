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
        for (index in options.indices) {
            if (options[index].chose) current = index
            (inflate(context, R.layout.vote_option_layout, optionsLayout) as? LinearLayout)?.getChildAt(index)?.let {
                var progressView: View = it.findViewById(R.id.progress_view)
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
                    var progressView: View = it.getTag(R.string.tag_progress_view) as View
                    val nameView: TextView = it.getTag(R.string.tag_name_view) as TextView
                    val numberView: TextView = it.getTag(R.string.tag_number_view) as TextView
                    progressView.isSelected = current == index
                    progressView.background = if (it.width - (options[index].number.toFloat() / totalNumber.toFloat()) * it.width > UIUtil.dip2px(context, 5f)) {
                        ContextCompat.getDrawable(context, R.drawable.progress_view_bg)
                    } else {
                        ContextCompat.getDrawable(context, R.drawable.progress_view_bg2)
                    }
                    nameView.setTextColor(if (current == index) Color.parseColor("#2B8DE2") else Color.parseColor("#999999"))
                    numberView.setTextColor(nameView.currentTextColor)
                    ObjectAnimator.ofInt(ViewWrapper(progressView), "customWidth", ((options[index].number.toFloat() / totalNumber.toFloat()) * it.width).toInt()).setDuration(1000).start()
                    ObjectAnimator.ofFloat(nameView, "translationX", (nameView.width - it.width + UIUtil.dip2px(context, 20f)) / 2f).setDuration(1000).start()
                    ObjectAnimator.ofFloat(numberView, "alpha", 1f).setDuration(1000).start()
                }
            }
        }
    }

    data class OptionInfo(val name: String, val number: Int, var chose: Boolean)
}