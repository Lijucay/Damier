package de.lijucay.damier.settings.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.IosShare
import androidx.compose.material.icons.rounded.PrivacyTip
import androidx.compose.material.icons.rounded.ReportGmailerrorred
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.processphoenix.ProcessPhoenix
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.domain.ExportResult
import de.lijucay.damier.core.domain.InfoMode
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.components.SwitchPreference
import de.lijucay.damier.core.presentation.components.VersionInfo
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    snackbarHost: @Composable (() -> Unit),
    showCrashlyticsChangedSnackbar: (Int, Boolean, Int, () -> Unit) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val uiViewModel = koinViewModel<UIViewModel>()
    val activityListViewModel = koinViewModel<ActivityListViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()

    val showReference by uiViewModel.showReference.collectAsStateWithLifecycle()
    val showMaxAmount by uiViewModel.showMaxAmount.collectAsStateWithLifecycle()
    val savedDirUri by uiViewModel.savedDirUri.collectAsStateWithLifecycle()

    var backupUri by remember { mutableStateOf<Uri?>(null) }

    val crashlytics = FirebaseCrashlytics.getInstance()

    var crashlyticsEnabled by remember {
        mutableStateOf(crashlytics.isCrashlyticsCollectionEnabled)
    }

    LaunchedEffect(savedDirUri) {
        savedDirUri?.let { savedDirUriStr ->
            val uri = savedDirUriStr.toUri()
            val persistedUriPermissions = context.contentResolver.persistedUriPermissions
            val hasPermission = persistedUriPermissions.any {
                it.uri == uri && it.isReadPermission && it.isWritePermission
            }

            if (hasPermission) {
                backupUri = uri
            } else {
                uiViewModel.setSavedDirUri(null)
                backupUri = null
            }
        } ?: run {
            backupUri = null
        }
    }

    val invalidDirStr = stringResource(R.string.invalid_directory)
    val invalidVersionStr = stringResource(R.string.invalid_version)

    val directoryPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)

            uiViewModel.setSavedDirUri(uri.toString())
            backupUri = uri

            // Starte Export sofort nach Auswahl
            activityListViewModel.exportData(uri) { result ->
                when (result) {
                    is ExportResult.Success -> uiViewModel.setInfoMode(InfoMode.BackupSuccess)
                    is ExportResult.Failure -> uiViewModel.setInfoMode(InfoMode.BackupError(result.message))
                }
            }
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fileUri = result.data?.data

            fileUri?.let {
                activityListViewModel.import(
                    fileUri = fileUri,
                    onTotalCountUpdate = {},
                    onCurrentCountUpdate = {},
                    onComplete = { result ->
                        if (result) {
                            uiViewModel.setInfoMode(InfoMode.ImportSuccess)
                        }
                    },
                    onIncompatibleVersion = {
                        uiViewModel.setInfoMode(InfoMode.ImportError(invalidVersionStr))
                    }
                )
            }
        }
    }

    ScreenContainer(
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.settings),
        bottomBarContent = {
            VersionInfo()
        },
        snackbarHost = snackbarHost,
        showBottomBarContent = true,
        navigationIcon = {
            if (!isWidthAtLeastExpanded) {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            PreferenceCategoryTitle(
                title = stringResource(R.string.app_settings)
            )
            SwitchPreference(
                title = stringResource(R.string.enable_crashlytics),
                subTitle = stringResource(R.string.crashlytics_info),
                icon = Icons.Rounded.ReportGmailerrorred,
                checked = crashlyticsEnabled
            ) {
                crashlytics.isCrashlyticsCollectionEnabled = !crashlytics.isCrashlyticsCollectionEnabled
                crashlyticsEnabled = !crashlyticsEnabled
                showCrashlyticsChangedSnackbar(
                    R.string.restart_info,
                    true,
                    R.string.restart
                ) {
                    ProcessPhoenix.triggerRebirth(context)
                }
            }

            PreferenceCategoryTitle(
                title = stringResource(R.string.export_import)
            )
            Preference(
                title = stringResource(R.string.backup_data),
                summary = stringResource(R.string.backup_data_summary),
                iconVector = Icons.Rounded.IosShare
            ) {
                val currentUri = backupUri
                if (currentUri != null) {
                    activityListViewModel.exportData(currentUri) { result ->
                        when (result) {
                            is ExportResult.Success -> uiViewModel.setInfoMode(InfoMode.BackupSuccess)
                            is ExportResult.Failure -> {
                                if (result.message == invalidDirStr) {
                                    uiViewModel.setSavedDirUri(null)
                                    backupUri = null
                                    directoryPicker.launch(null)
                                } else {
                                    uiViewModel.setInfoMode(InfoMode.BackupError(result.message))
                                }
                            }
                        }
                    }
                } else {
                    directoryPicker.launch(null)
                }
            }
            Preference(
                title = stringResource(R.string.import_data),
                summary = stringResource(R.string.import_data_summary),
                iconVector = Icons.Rounded.FileOpen
            ) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "application/octet-stream"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }

                filePickerLauncher.launch(intent)
            }

            PreferenceCategoryTitle(
                title = stringResource(R.string.activity_options)
            )
            SwitchPreference(
                title = stringResource(R.string.show_reference),
                subTitle = stringResource(id = R.string.show_reference_explanation),
                checked = showReference,
                icon = ImageVector.vectorResource(R.drawable.rounded_counter_ref)
            ) { checked ->
                uiViewModel.changeShowReference(checked)
            }
            AnimatedVisibility(
                visible = showReference
            ) {
                SwitchPreference(
                    title = stringResource(R.string.show_max),
                    subTitle = stringResource(R.string.show_max_explanation),
                    checked = showMaxAmount,
                    icon = ImageVector.vectorResource(R.drawable.rounded_counter_max)
                ) { checked ->
                    uiViewModel.changeShowMaxAmount(checked)
                }
            }

            PreferenceCategoryTitle(
                title = stringResource(R.string.privacy_policy)
            )

            Preference(
                title = stringResource(R.string.privacy_policy),
                summary = stringResource(R.string.privacy_policy_sum),
                iconVector = Icons.Rounded.PrivacyTip
            ) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "https://damier.lijucay.de/privacy".toUri()
                }

                context.startActivity(intent)
            }

            PreferenceCategoryTitle(
                title = stringResource(R.string.about_and_help)
            )
            Preference(
                title = stringResource(R.string.show_onboarding_again),
                summary = stringResource(R.string.show_onboarding_again_summary),
                iconVector = Icons.Rounded.Info
            ) {
                uiViewModel.setFirstLaunch(true)
            }
        }
    }
}
