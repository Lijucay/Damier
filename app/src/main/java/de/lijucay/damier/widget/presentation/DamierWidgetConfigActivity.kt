package de.lijucay.damier.widget.presentation

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import de.lijucay.damier.R
import de.lijucay.damier.core.DataPreferences
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.ui.theme.DamierTheme
import de.lijucay.damier.widget.domain.WidgetRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class DamierWidgetConfigActivity : ComponentActivity(), KoinComponent {
    private val widgetRepository: WidgetRepository by inject()
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
                    this[DataPreferences.Keys.activityId] = activity.id.toString()
                    this[DataPreferences.Keys.activityName] = activity.activityName
                }
            }

            DamierWidget().update(
                context =this@DamierWidgetConfigActivity,
                id = GlanceAppWidgetManager(this@DamierWidgetConfigActivity)
                    .getGlanceIdBy(appWidgetId)
            )

            val resultIntent = Intent().apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ActivityPickerScreen(
        onActivitySelected: (ActivityInfo) -> Unit,
        onCancel: () -> Unit
    ) {
        val activities = remember { mutableStateListOf<ActivityInfo>() }
        LaunchedEffect(Unit) {
            activities.addAll(widgetRepository.getAllActivities())
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
                                imageVector = TablerIcons.ArrowLeft,
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
                        modifier = Modifier.padding(horizontal = 16.dp),
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