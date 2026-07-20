package de.lijucay.damier.widget.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.Button
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import de.lijucay.damier.MainActivity
import de.lijucay.damier.R
import de.lijucay.damier.core.DataPreferences
import de.lijucay.damier.core.Logger
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.widget.data.LogCheckInAction
import de.lijucay.damier.widget.domain.WidgetActivityData
import de.lijucay.damier.widget.domain.WidgetActivityState
import de.lijucay.damier.widget.domain.WidgetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import java.util.UUID

class DamierWidget : GlanceAppWidget() {
    override val stateDefinition = PreferencesGlanceStateDefinition
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Logger.d(context, "provideGlance started")

        val repo: WidgetRepository = GlobalContext.get().get()

        provideContent {
            val prefs = currentState<Preferences>()
            val rawId = prefs[DataPreferences.Keys.activityId]
            val activityName = prefs[DataPreferences.Keys.activityName]

            val activityId = runCatching { UUID.fromString(rawId) }.getOrNull()
            val activityFlow = activityId?.let { repo.observeActivity(it) } ?: flowOf(null)
            val state by activityFlow.collectAsState(initial = WidgetActivityState.Loading)

            GlanceTheme {
                when {
                    activityId == null -> NoActivitySelectedLayout(context)
                    state is WidgetActivityState.Loaded -> WidgetLayout(context, (state as WidgetActivityState.Loaded).data)
                    state is WidgetActivityState.Deleted -> DeletedActivityLayout(context)
                    else -> LoadingLayout(context, activityName)
                }
            }
        }
    }

    private fun getReference(activityData: WidgetActivityData): Int {
        return when(activityData.activityInfo.referenceType) {
            ReferenceType.GOAL, ReferenceType.LIMIT -> activityData.activityInfo.reference
            ReferenceType.MAX -> activityData.maxDailyAmount
        }
    }

    private fun getProgress(
        activityData: WidgetActivityData
    ) = activityData.todaysAmount.toFloat() / getReference(activityData).coerceAtLeast(1)

    @Composable
    fun WidgetLayout(
        context: Context,
        activityData: WidgetActivityData
    ) {
        Scaffold(
            modifier = GlanceModifier
                .appWidgetBackground()
                .clickable(
                    onClick = actionStartActivity(
                        Intent(context, MainActivity::class.java).apply {
                            putExtra(
                                "activityId",
                                activityData.activityInfo.id.toString()
                            )
                        }
                    )
                )
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(16.dp)
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = GlanceModifier
                            .size(40.dp)
                            .background(GlanceTheme.colors.primaryContainer)
                            .cornerRadius(100.dp),
                        provider = ImageProvider(R.drawable.ic_launcher_foreground),
                        colorFilter = ColorFilter
                            .tint(GlanceTheme.colors.onPrimaryContainer),
                        contentDescription = null,
                    )
                    Spacer(GlanceModifier.defaultWeight())
                    Text(
                        context.getString(
                            R.string.amount_of_reference,
                            activityData.todaysAmount,
                            getReference(activityData)
                        ),
                        style = TextStyle(
                            color = GlanceTheme.colors.onBackground
                        )
                    )
                }
                Spacer(GlanceModifier.height(16.dp))
                Text(
                    modifier = GlanceModifier
                        .defaultWeight(),
                    text = activityData.activityInfo.activityName,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlanceTheme.colors.onBackground
                    )
                )
                Column(
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = getProgress(activityData),
                        modifier = GlanceModifier
                            .cornerRadius(20.dp)
                            .fillMaxWidth(),
                        color = GlanceTheme.colors.onPrimaryContainer,
                        backgroundColor = GlanceTheme.colors.primaryContainer
                    )
                    Spacer(GlanceModifier.height(16.dp))
                    Button(
                        modifier = GlanceModifier.fillMaxWidth(),
                        text = context.getString(R.string.check_in),
                        onClick = actionRunCallback<LogCheckInAction>(
                            actionParametersOf(
                                LogCheckInAction.ACTIVITY_ID_KEY to
                                        activityData.activityInfo.id.toString()
                            )
                        ),
                    )
                }
            }
        }
    }

    @Composable
    fun LoadingLayout(context: Context, activityName: String?) {
        Scaffold(modifier = GlanceModifier.fillMaxSize()) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (activityName != null) {
                        Text(
                            text = activityName,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = GlanceTheme.colors.onBackground
                            )
                        )
                        Spacer(GlanceModifier.height(8.dp))
                    }
                    Text(
                        text = context.getString(R.string.loading),
                        style = TextStyle(color = GlanceTheme.colors.onBackground)
                    )
                }
            }
        }
    }

    @Composable
    fun DeletedActivityLayout(context: Context) {
        Scaffold(
            modifier = GlanceModifier
                .appWidgetBackground()
                .clickable(
                    onClick = actionRunCallback<ClearDeletedActivityAction>()
                )
        ) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = context.getString(R.string.activity_no_longer_exists),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = GlanceTheme.colors.onBackground
                        )
                    )
                    Spacer(GlanceModifier.height(8.dp))
                    Text(
                        text = context.getString(R.string.tap_to_reconfigure),
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }

    @Composable
    fun NoActivitySelectedLayout(context: Context) {
        Scaffold(modifier = GlanceModifier.fillMaxSize()) {
            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(context.getString(R.string.no_activity_selected))
            }
        }
    }

    fun updateOnReboot(context: Context?) {
        context?.let {
            CoroutineScope(Dispatchers.IO).launch {
                updateAll(context)
            }
        }
    }
}