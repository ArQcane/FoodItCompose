package com.example.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

//@Composable
//fun CltDropDownMenu(optionList: List<String>,label:String,) {
//    var expanded by remember { mutableStateOf(false) }
//
//    var selectedText by remember { mutableStateOf("") }
//
//    var textfieldSize by remember { mutableStateOf(Size.Zero) }
//
//    val icon = if (expanded)
//        Icons.Filled.KeyboardArrowUp
//    else
//        Icons.Filled.KeyboardArrowDown
//
//
//    Column() {
//        OutlinedTextField(
//            value = selectedText,
//            onValueChange = { selectedText = it },
//            enabled = false,
//            modifier = Modifier
//                .fillMaxWidth()
//                .onGloballyPositioned { coordinates ->
//                    //This value is used to assign to the DropDown the same width
//                    textfieldSize = coordinates.size.toSize()
//                }
//                .clickable { expanded = !expanded },
//            label = { Text(label) },
//            trailingIcon = {
//                Icon(icon, "Drop Down Icon",
//                    Modifier.clickable { expanded = !expanded })
//            }
//        )
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier
//                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
//        ) {
//            optionList.forEach { label ->
//                DropdownMenuItem(onClick = {
//                    selectedText = label
//                    expanded = !expanded
//                }) {
//                    Text(text = label)
//                }
//            }
//        }
//    }
//}

@Composable
fun CltDropDownMenu(
    optionList: List<String>,
    value: String,
    label: String,
    error: String?,
    modifier: Modifier = Modifier,
) {

    var expanded by remember { mutableStateOf(false) }

    var value by remember {
        mutableStateOf(value)
    }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(10.dp),
            enabled = false,
            singleLine = true,
            value = value,
            label = { Text(text = label) },
            onValueChange = { value = it },
            isError = error != null,
            trailingIcon = {
                Icon(icon, "Drop Down Icon",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            optionList.forEach { label ->
                DropdownMenuItem(onClick = {
                    value = label
                    expanded = !expanded
                }) {
                    Text(text = label)
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            visible = error != null,
            enter = fadeIn() + slideInHorizontally(animationSpec = spring()),
        ) {
            error?.let {
                Text(
                    text = error,
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }
}