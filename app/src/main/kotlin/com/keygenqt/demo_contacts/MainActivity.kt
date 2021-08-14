/*
 * Copyright 2021 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.demo_contacts

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.keygenqt.demo_contacts.base.LocalBaseViewModel
import com.keygenqt.demo_contacts.modules.common.navigation.GuestNavGraph
import com.keygenqt.demo_contacts.modules.common.ui.viewModels.ViewModelMain
import com.keygenqt.demo_contacts.theme.MyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ViewModelMain by viewModels()

    private lateinit var navController: NavHostController

    @ExperimentalPagingApi
    @ExperimentalComposeUiApi
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set graph user/guest
        setContent {
            navController = rememberNavController()
            CompositionLocalProvider(LocalBaseViewModel provides viewModel) {
                MyTheme {
                    // change status bar color
                    this@MainActivity.window.statusBarColor = MaterialTheme.colors.primaryVariant.toArgb()
                    // select graph
                    GuestNavGraph(navController)
                }
            }
        }

        // Splash delay
        window.decorView.findViewById<View>(android.R.id.content)?.let { content ->
            content.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        return if (viewModel.isReady.value) {
                            // remove BG splash
                            this@MainActivity.window.decorView.setBackgroundColor(Color.WHITE)
                            // done splash remove listener
                            content.viewTreeObserver.removeOnPreDrawListener(this); true
                        } else false
                    }
                }
            )
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.route) {
            // Show snackBar before exit
            // @todo
//            ListChats.route -> viewModel.apply { if (showSnackBar.value) finishAffinity() else toggleSnackBar() }
            else -> super.onBackPressed()
        }
    }
}