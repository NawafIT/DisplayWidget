package com.api.displaywidget.ui

import android.content.Context

import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import com.api.displaywidget.ui.Adkar.list


object DisplayAdkar : GlanceAppWidget() {

    val countKey = intPreferencesKey("count")

    @Composable
    override fun Content() {
        val count = currentState(key = countKey) ?: 0
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color.DarkGray),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            androidx.glance.text.Text(
                text = list[count],
                style = androidx.glance.text.TextStyle(
                    fontWeight = androidx.glance.text.FontWeight.Medium,
                    color = ColorProvider(Color.White),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                ),

                )
            Spacer(modifier = GlanceModifier.height(10.dp))
            Button(
                text = "تغير الذكر",
                onClick = actionRunCallback(UpdateData::class.java)
            )
        }
    }
}

class ShowMyWidget: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = DisplayAdkar
}

class UpdateData: ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val currentCount = prefs[DisplayAdkar.countKey]
            if(currentCount != null) {
                prefs[DisplayAdkar.countKey] = if(currentCount == list.size - 1) 0 else currentCount + 1
            } else {
                prefs[DisplayAdkar.countKey] = 1
            }
        }
        DisplayAdkar.update(context, glanceId)
    }
}