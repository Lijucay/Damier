package de.lijucay.damier.settings.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowBack
import compose.icons.tablericons.Share
import compose.icons.tablericons.Trash
import de.lijucay.damier.R
import de.lijucay.damier.core.Logger
import de.lijucay.damier.core.domain.DataUtil
import de.lijucay.damier.core.presentation.components.ScreenContainer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ReadLogsScreen(context: Context = LocalContext.current, onNavigateBack: () -> Unit) {
    var logContent by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val file = File(context.filesDir, DataUtil.LOG_FILE_NAME)
        while (isActive) {
            val content = withContext(Dispatchers.IO) {
                if (file.exists()) file.readText() else ""
            }
            if (content != logContent) {
                logContent = content
            }
            delay(1000.milliseconds)
        }
    }

    val shareLogsText = stringResource(R.string.share_logs)

    ScreenContainer(
        isWidthAtLeastExpanded = false,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack
            ) {
                Icon(
                    imageVector = TablerIcons.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        title = stringResource(R.string.logs),
        topAppBarActions = {
            IconButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, logContent)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, shareLogsText))
                }
            ) {
                Icon(
                    imageVector = TablerIcons.Share,
                    contentDescription = shareLogsText
                )
            }
            IconButton(
                onClick = {
                    Logger.clear(context)
                }
            ) {
                Icon(
                    imageVector = TablerIcons.Trash,
                    contentDescription = shareLogsText
                )
            }
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            item {
                Text(
                    text = logContent,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}