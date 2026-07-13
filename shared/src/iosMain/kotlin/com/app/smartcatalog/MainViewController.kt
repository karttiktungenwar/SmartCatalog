package com.app.smartcatalog

import androidx.compose.ui.window.ComposeUIViewController
import com.app.smartcatalog.app.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}