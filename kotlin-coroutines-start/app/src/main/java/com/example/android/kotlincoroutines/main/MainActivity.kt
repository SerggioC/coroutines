/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.kotlincoroutines.main

import android.os.Bundle
import android.view.Menu
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.android.kotlincoroutines.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Main Activity for our application. This activity uses [MainViewModel] to implement MVVM.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Inflate layout and setup click listeners and LiveData observers.
     */
    @UiThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        val viewModel = ViewModelProviders.of(this)
            .get(MainViewModel::class.java)

        // When rootLayout is clicked call onMainViewClicked in ViewModel
        rootLayout.setOnClickListener {
            //CoroutineScope(Dispatchers.Main).launch { viewModel.onMainViewClicked() }
            viewModel.onMainViewClicked()
        }

        // Show a snackbarLiveData whenever the [ViewModel.snackbarLiveData] is updated with a non-null value
        viewModel.snackbarLiveData.observe(this, Observer {
            it?.let {
                Snackbar.make(rootLayout, it, Snackbar.LENGTH_SHORT).show()
                viewModel.onSnackbarShown()
            }

        }
        )
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

}
