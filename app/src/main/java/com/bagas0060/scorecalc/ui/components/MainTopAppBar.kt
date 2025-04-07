package com.bagas0060.scorecalc.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.bagas0060.scorecalc.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = title,
        navigationIcon = {
            navigationIcon?.invoke()
        },
        actions = {
            actions?.invoke()
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = colorResource(id = R.color.red),
            titleContentColor = colorResource(id = R.color.white)
        )
    )
}
