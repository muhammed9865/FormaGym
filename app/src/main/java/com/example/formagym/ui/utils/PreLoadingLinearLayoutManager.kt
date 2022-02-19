package com.example.formagym.ui.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class PreLoadingLinearLayoutManager constructor(context: Context) :
    LinearLayoutManager(context) {
    constructor(context: Context, mPages: Int) : this(context) {
        this.mPages = mPages
    }

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : this(context) {
        super.setReverseLayout(reverseLayout)
        super.setOrientation(orientation)
    }

    private var mOrientationHelper: OrientationHelper? = null
    private var mPages: Int = 0
    override fun setOrientation(orientation: Int) {
        super.setOrientation(orientation)
        mOrientationHelper = null
    }

    fun setPages(pages: Int) {
        this.mPages = pages
    }

    override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
        if (mOrientationHelper == null) {
            mOrientationHelper = OrientationHelper.createOrientationHelper(this, orientation)
        }
        return mOrientationHelper!!.totalSpace * mPages
    }
}