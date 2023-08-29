/*
 * Copyright (C) 2023 The Android Open Source Project
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
package com.example.marsphotos.ui.screens

import android.provider.ContactsContract.CommonDataKinds.Photo
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.network.MarsApi
import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MarsUiState {
    data class Success(val photos: MarsPhoto) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}


class MarsViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    /*var marsUiState: String by mutableStateOf("")
        private set*/


    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set


    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
            //marsUiState = "Set the Mars API status response here!"
        viewModelScope.launch {
            marsUiState =  try {
                val listResult = MarsApi.retrofitService.getPhotos()
                listResult.forEach { Log.d("${it.id}", "${it.img_src}" ) }
                MarsUiState.Success(
                    listResult[0]
                )
            } catch (e: IOException) {
                MarsUiState.Error
            }

        }

    }
}
