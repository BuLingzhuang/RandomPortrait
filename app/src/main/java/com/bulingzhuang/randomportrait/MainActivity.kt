package com.bulingzhuang.randomportrait

import android.content.ClipData
import android.content.ClipboardManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val adapter = PortraitHistoryAdapter(this, object : PortraitHistoryAdapter.Callback {
        override fun notifyChange(code: String) {
            rpv.showCode(code)
        }
    })

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_random -> {
                rpv.random()
            }
            R.id.btn_random2 -> {//3连抽
                val list = rpv.randomX3()
                adapter.addAll(list)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_random.setOnClickListener(this)
        btn_random2.setOnClickListener(this)

        val layoutManager = GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, true)
        rv_image.layoutManager = layoutManager

        rv_image.adapter = adapter

        rpv.mCallback = object : RandomPortraitView.CodeChangeCallback {
            override fun show(portraitCode: String) {
                tv_code.text = portraitCode
            }

            override fun notify(portraitCode: String) {
                tv_code.text = portraitCode
                adapter.add(portraitCode)
            }
        }

        ll_code.setOnLongClickListener {
            if (!TextUtils.isEmpty(tv_code.text)) {
                val cm = this.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.primaryClip = ClipData.newPlainText("Label", tv_code.text)
                Toast.makeText(this, "头像代码已保存到剪贴板", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }

    }
}
