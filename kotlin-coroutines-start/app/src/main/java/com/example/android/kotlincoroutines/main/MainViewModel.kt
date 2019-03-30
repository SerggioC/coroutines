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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel : ViewModel() {

    /** Children of a supervisor job can fail independently of each other. */
    private val supervisorJob = SupervisorJob()

    /**Jobs can be arranged into parent-child hierarchies where cancellation
     * of parent lead to an immediate cancellation of all its children.
     * Failure or cancellation of a child with an exception other than
     * CancellationException immediately cancels its parent.
     * This way, parent can cancel its own children (including all their
     * children recursively) without cancelling itself.*/
    private val viewModelJob: Job = Job()
    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
        uiScope.cancel() // DON'T!
    }

    /**
     * Request a snackbarLiveData to display a string.
     *
     * This variable is private because we don't want to expose MutableLiveData
     *
     * MutableLiveData allows anyone to set a value, and MainViewModel is the only
     * class that should be setting values.
     */
    private val _snackBarLiveData = MutableLiveData<String>()

    /**
     * Request a snackbarLiveData to display a string.
     */
    val snackbarLiveData: LiveData<String>
        get() = _snackBarLiveData

    fun onViewClicked2(): Unit {
        viewModelScope.launch(viewModelJob, CoroutineStart.LAZY) {
            delay(1000) //non blocking delay; Schedules the operation for the specified time in future
        }
    }

    /**
     * Wait one second then display a snackbarLiveData.
     */
    fun onMainViewClicked() {
        // TODO: Replace with coroutine implementation
        val current = Thread.currentThread().name
        Log.i("Sergio> ", "current: $current")
        uiScope.launch {
            val currente = Thread.currentThread().name
            Log.i("Sergio> ", "current: $currente")
            Thread.sleep(5_000)
            Log.i("Sergio> ", "current: $currente")
            // use postValue since we're in a background thread
            _snackBarLiveData.postValue("Hello, from threads!")
        }


//        BACKGROUND.submit {
//            Thread.sleep(1_000)
//            // use postValue since we're in a background thread
//            _snackBarLiveData.postValue("Hello, from threads!")
//        }
    }

    /**
     * Called immediately after the UI shows the snackbarLiveData.
     */
    fun onSnackbarShown() {
        _snackBarLiveData.value = null
    }
}
