package com.example.juicetracker.ui.bottomsheet

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.Spinner
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.juicetracker.R
import com.example.juicetracker.data.JuiceColor

@Composable
fun ColorSpinnerRow(
    colorSpinnerPosition: Int,
    onColorChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val juiceColorArray =
        JuiceColor.values().map { juiceColor -> stringResource(juiceColor.label) }

    InputRow(inputLabel = stringResource(R.string.color), modifier = modifier) {
        AndroidView(modifier = Modifier.fillMaxWidth(),
            factory = {context ->
                Spinner(context).apply {
                    adapter =
                        ArrayAdapter(
                            context,
                            android.R.layout.simple_spinner_dropdown_item,
                            juiceColorArray
                        )
                }


            },
            update = {
                 val sp =  it as Spinner
                 sp.setSelection(colorSpinnerPosition)
                 sp.onItemSelectedListener = SpinnerAdapter(onColorChange)

            }
        )
    }


}

class SpinnerAdapter(val onColorChange: (Int) -> Unit): AdapterView.OnItemSelectedListener    {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onColorChange(position)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        onColorChange(0)
    }

}

