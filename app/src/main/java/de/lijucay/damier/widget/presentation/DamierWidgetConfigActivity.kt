package de.lijucay.damier.widget.presentation

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.lifecycle.lifecycleScope
import de.lijucay.damier.R
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.ui.theme.DamierTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class DamierWidgetConfigActivity : ComponentActivity(), KoinComponent {
    private val activityDao: ActivityInfoDao by inject()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(RESULT_CANCELED)

        appWidgetId = intent.extras
            ?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            DamierTheme {
                ActivityPickerScreen(
                    onActivitySelected = { activity ->
                        saveSelectionAndFinish(activity)
                    },
                    onCancel = { finish() }
                )
            }
        }
    }

    private fun saveSelectionAndFinish(activity: ActivityInfo) {
        lifecycleScope.launch {
            updateAppWidgetState(
                context = this@DamierWidgetConfigActivity,
                definition = PreferencesGlanceStateDefinition,
                glanceId = GlanceAppWidgetManager(this@DamierWidgetConfigActivity)
                    .getGlanceIdBy(appWidgetId)
            ) { prefs ->
                prefs.toMutablePreferences().apply {
                    this[DamierWidgetState.ACTIVITY_ID] = activity.id.toString()
                    this[DamierWidgetState.ACTIVITY_NAME] = activity.activityName
                }
            }

            Log.d("DamierWidgetConfig", "State updated, calling widget update")

            DamierWidget().update(
                context =this@DamierWidgetConfigActivity,
                id = GlanceAppWidgetManager(this@DamierWidgetConfigActivity)
                    .getGlanceIdBy(appWidgetId)
            )

            Log.d("DamierWidgetConfig", "Widget update called")

            val resultIntent = Intent().apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    @Composable
    fun ActivityPickerScreen(
        modifier: Modifier = Modifier,
        onActivitySelected: (ActivityInfo) -> Unit,
        onCancel: () -> Unit
    ) {
        val activities = remember { mutableStateListOf<ActivityInfo>() }
        LaunchedEffect(Unit) {
            Log.e("ActivityWidgetConfig", "Loading activities")
            activities.addAll(activityDao.getActivitiesForWidgetConfig())
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = stringResource(R.string.activities))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onCancel
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(activities) { activity ->
                    Card(
                        onClick = { onActivitySelected(activity) },
                        shape = RoundedCornerShape(
                            topStart = if (activities.first() == activity) 40.dp else 4.dp,
                            topEnd = if (activities.first() == activity) 40.dp else 4.dp,
                            bottomEnd = if (activities.last() == activity) 40.dp else 4.dp,
                            bottomStart = if (activities.last() == activity) 40.dp else 4.dp
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            text = activity.activityName
                        )
                    }
                }
            }
        }
    }
}