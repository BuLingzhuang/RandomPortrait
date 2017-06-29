package com.bulingzhuang.randomportrait

import android.content.ClipData
import android.content.ClipboardManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        rpv.random()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener(this)
        rpv.mCallback = object : RandomPortraitView.CodeChangeCallback {
            override fun notify(portraitCode: String) {
                code.text = portraitCode
            }
        }
        code.setOnLongClickListener {
            if (!TextUtils.isEmpty(code.text)) {
                val cm = this.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.primaryClip = ClipData.newPlainText("Label", code.text)
                Toast.makeText(this, "头像代码已保存到剪贴板", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }
    }
}
