package com.tino.vote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tino.vote.widget.VoteView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val options: ArrayList<VoteView.OptionInfo> = arrayListOf()
    /**
     * 标识是否已经投过票
     */
    private val chose = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        voteView.setData(chose, "你最喜欢哪个季节？", getData(), options)
    }

    private fun getData(): Int {
        options.add(VoteView.OptionInfo("万紫千红的春天", 53000, false))
        options.add(VoteView.OptionInfo("郁郁葱葱的夏天", 34000, false))
        options.add(VoteView.OptionInfo("橙黄橘绿的秋天", 157000, chose))
        options.add(VoteView.OptionInfo("白雪皑皑的冬天", 6000, false))
        var totalNumber = 0
        for (option in options) totalNumber += option.number
        return totalNumber
    }

}