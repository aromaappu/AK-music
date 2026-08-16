/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.aromaappu.akmusic.ui.screens.settings

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.aromaappu.akmusic.innertube.YouTube
import com.aromaappu.akmusic.LocalPlayerAwareWindowInsets
import com.aromaappu.akmusic.R
import com.aromaappu.akmusic.constants.*
import com.aromaappu.akmusic.ui.component.*
import com.aromaappu.akmusic.ui.utils.backToMain
import com.aromaappu.akmusic.utils.rememberEnumPreference
import com.aromaappu.akmusic.utils.rememberPreference
import com.aromaappu.akmusic.utils.setAppLocale
import java.net.Proxy
import java.util.Locale
import androidx.core.net.toUri

private fun getLanguageDisplayName(languageCode: String): String {
    return when (languageCode) {
        SYSTEM_DEFAULT -> "System Default"
        else -> LanguageCodeToName[languageCode] ?: languageCode
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val context = LocalContext.current

    val (appLanguage, onAppLanguageChange) = rememberPreference(
        key = AppLanguageKey,
        defaultValue = SYSTEM_DEFAULT
    )

    val (contentLanguage, onContentLanguageChange) = rememberPreference(
        key = ContentLanguageKey,
        defaultValue = "system"
    )
    val (contentCountry, onContentCountryChange) = rememberPreference(
        key = ContentCountryKey,
        defaultValue = "system"
    )
    val (hideExplicit, onHideExplicitChange) = rememberPreference(
        key = HideExplicitKey,
        defaultValue = false
    )
    val (hideVideo, onHideVideoChange) = rememberPreference(
        key = HideVideoKey,
        defaultValue = false
    )
    val (proxyEnabled, onProxyEnabledChange) = rememberPreference(
        key = ProxyEnabledKey,
        defaultValue = false
    )
    val (proxyType, onProxyTypeChange) = rememberEnumPreference(
        key = ProxyTypeKey,
        defaultValue = Proxy.Type.HTTP
    )
    val (proxyUrl, onProxyUrlChange) = rememberPreference(
        key = ProxyUrlKey,
        defaultValue = "host:port"
    )
    val (streamBypassProxy, onStreamBypassProxyChange) = rememberPreference(
        key = StreamBypassProxyKey,
        defaultValue = false
    )
    val (jossRedEnabled, onJossRedEnabledChange) = rememberPreference(
        key = JossRedMultimediaKey,
        defaultValue = true
    )

    var showLanguageSelector by remember { mutableStateOf(false) }

    val languageOptions = remember {
        LanguageCodeToName.map { (code, name) ->
            LanguageOption(code = code, displayName = name)
        }
    }

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState()),
    ) {
        PreferenceGroupTitle(title = stringResource(R.string.general))

        ListPreference(
            title = { Text(stringResource(R.string.content_language)) },
            icon = { Icon(painterResource(R.drawable.language), null) },
            selectedValue = contentLanguage,
            values = listOf(SYSTEM_DEFAULT) + LanguageCodeToName.keys.toList(),
            valueText = {
                LanguageCodeToName.getOrElse(it) { stringResource(R.string.system_default) }
            },
            onValueSelected = { newValue ->
                val locale = Locale.getDefault()
                val languageTag = locale.toLanguageTag().replace("-Hant", "")

                YouTube.locale = YouTube.locale.copy(
                    hl = newValue.takeIf { it != SYSTEM_DEFAULT }
                        ?: locale.language.takeIf { it in LanguageCodeToName }
                        ?: languageTag.takeIf { it in LanguageCodeToName }
                        ?: "en"
                )

                onContentLanguageChange(newValue)
            }
        )

        ListPreference(
            title = { Text(stringResource(R.string.content_country)) },
            icon = { Icon(painterResource(R.drawable.location_on), null) },
            selectedValue = contentCountry,
            values = listOf(SYSTEM_DEFAULT) + CountryCodeToName.keys.toList(),
            valueText = {
                CountryCodeToName.getOrElse(it) { stringResource(R.string.system_default) }
            },
            onValueSelected = { newValue ->
                val locale = Locale.getDefault()

                YouTube.locale = YouTube.locale.copy(
                    gl = newValue.takeIf { it != SYSTEM_DEFAULT }
                        ?: locale.country.takeIf { it in CountryCodeToName }
                        ?: "US"
                )

                onContentCountryChange(newValue)
            }
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.hide_explicit)) },
            icon = { Icon(painterResource(R.drawable.explicit), null) },
            checked = hideExplicit,
            onCheckedChange = onHideExplicitChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.hide_video)) },
            icon = { Icon(painterResource(R.drawable.slow_motion_video), null) },
            checked = hideVideo,
            onCheckedChange = onHideVideoChange,
        )

        PreferenceGroupTitle(title = stringResource(R.string.app_language))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PreferenceEntry(
                title = { Text(stringResource(R.string.app_language)) },
                subtitle = {
                    Text(
                        text = getLanguageDisplayName(appLanguage)
                    )
                },
                icon = { Icon(painterResource(R.drawable.translate), null) },
                onClick = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APP_LOCALE_SETTINGS,
                            "package:${context.packageName}".toUri()
                        )
                    )
                }
            )
        } else {
            PreferenceEntry(
                title = { Text(stringResource(R.string.app_language)) },
                subtitle = {
                    Text(
                        text = getLanguageDisplayName(appLanguage)
                    )
                },
                icon = { Icon(painterResource(R.drawable.language), null) },
                onClick = { showLanguageSelector = true }
            )
        }

        PreferenceGroupTitle(title = stringResource(R.string.proxy))

        SwitchPreference(
            title = { Text(stringResource(R.string.enable_proxy)) },
            icon = { Icon(painterResource(R.drawable.wifi_proxy), null) },
            checked = proxyEnabled,
            onCheckedChange = onProxyEnabledChange,
        )

        if (proxyEnabled) {
            Column {
                ListPreference(
                    title = { Text(stringResource(R.string.proxy_type)) },
                    selectedValue = proxyType,
                    values = listOf(Proxy.Type.HTTP, Proxy.Type.SOCKS),
                    valueText = { it.name },
                    onValueSelected = onProxyTypeChange,
                )
                EditTextPreference(
                    title = { Text(stringResource(R.string.proxy_url)) },
                    value = proxyUrl,
                    onValueChange = onProxyUrlChange,
                )
                SwitchPreference(
                    title = { Text(stringResource(R.string.stream_bypass_proxy)) },
                    description = stringResource(R.string.stream_bypass_proxy_desc),
                    icon = { Icon(painterResource(R.drawable.wifi_proxy), null) },
                    checked = streamBypassProxy,
                    onCheckedChange = {
                        onStreamBypassProxyChange(it)
                        YouTube.streamBypassProxy = it
                    },
                )
            }
        }

        PreferenceGroupTitle(title = stringResource(R.string.playback))

        SwitchPreference(
            title = { Text(stringResource(R.string.jossred_fallback_label)) },
            description = stringResource(R.string.jossred_fallback_description),
            icon = { Icon(painterResource(R.drawable.cloud_off), null) },
            checked = jossRedEnabled,
            onCheckedChange = onJossRedEnabledChange,
        )
    }

    LanguageSelectorBottomSheet(
        show = showLanguageSelector,
        title = "Select App Language",
        languages = languageOptions,
        selectedCode = appLanguage,
        systemDefaultCode = SYSTEM_DEFAULT,
        systemDefaultLabel = "System Default",
        searchPlaceholder = "Search language...",
        onDismiss = { showLanguageSelector = false },
        onLanguageSelected = { selectedCode ->
            onAppLanguageChange(selectedCode)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                val newLocale = if (selectedCode == SYSTEM_DEFAULT) {
                    Locale.getDefault()
                } else {
                    Locale.forLanguageTag(selectedCode)
                }
                setAppLocale(context, newLocale)
            }

            showLanguageSelector = false
        }
    )

    TopAppBar(
        title = { Text(stringResource(R.string.content)) },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain,
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null,
                )
            }
        }
    )
}