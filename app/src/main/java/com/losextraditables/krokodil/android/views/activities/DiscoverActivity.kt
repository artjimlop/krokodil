package com.losextraditables.krokodil.android.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.losextraditables.krokodil.R

class DiscoverActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        component.inject(this)
    }
}
