package com.example.ekran.customView

import android.content.Context
import android.util.AttributeSet

class EkranVideoView: androidx.media3.ui.PlayerView{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}