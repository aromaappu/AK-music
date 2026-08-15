/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */



package com.aromaappu.akmusic.ui.screens.settings

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aromaappu.akmusic.LocalPlayerAwareWindowInsets
import com.aromaappu.akmusic.R
import com.aromaappu.akmusic.constants.DarkModeKey
import com.aromaappu.akmusic.constants.DynamicThemeKey
import com.aromaappu.akmusic.constants.PureBlackKey
import com.aromaappu.akmusic.constants.RandomThemeOnStartupKey
import com.aromaappu.akmusic.constants.UseSystemFontKey
import com.aromaappu.akmusic.constants.DisableBlurKey
import com.aromaappu.akmusic.ui.component.EnumListPreference
import com.aromaappu.akmusic.ui.component.IconButton
import com.aromaappu.akmusic.ui.component.PreferenceEntry
import com.aromaappu.akmusic.ui.component.PreferenceGroupTitle
import com.aromaappu.akmusic.ui.component.SwitchPreference
import com.aromaappu.akmusic.ui.utils.backToMain
import com.aromaappu.akmusic.utils.rememberEnumPreference
import com.aromaappu.akmusic.utils.rememberPreference
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val (dynamicTheme, onDynamicThemeChange) = rememberPreference(
        DynamicThemeKey,
        defaultValue = true
    )
    val (randomThemeOnStartup, onRandomThemeOnStartupChange) = rememberPreference(
        RandomThemeOnStartupKey,
        defaultValue = false
    )
    val (darkMode, onDarkModeChange) = rememberEnumPreference(
        DarkModeKey,
        defaultValue = DarkMode.AUTO
    )
    val (pureBlack, onPureBlackChange) = rememberPreference(PureBlackKey, defaultValue = false)
    val (disableBlur, onDisableBlurChange) = rememberPreference(DisableBlurKey, defaultValue = true)
    val (useSystemFont, onUseSystemFontChange) = rememberPreference(UseSystemFontKey, defaultValue = false)

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val useDarkTheme =
        remember(darkMode, isSystemInDarkTheme) {
            if (darkMode == DarkMode.AUTO) isSystemInDarkTheme else darkMode == DarkMode.ON
        }


    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current)
            .verticalScroll(rememberScrollState()),
    ) {
        PreferenceGroupTitle(
            title = stringResource(R.string.theme),
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.enable_dynamic_theme)) },
            icon = { Icon(painterResource(R.drawable.palette), null) },
            checked = dynamicTheme,
            onCheckedChange = onDynamicThemeChange,
        )

        AnimatedVisibility(visible = !dynamicTheme || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            SwitchPreference(
                title = { Text(stringResource(R.string.random_theme_on_startup)) },
                description = stringResource(R.string.random_theme_on_startup_desc),
                icon = { Icon(painterResource(R.drawable.shuffle), null) },
                checked = randomThemeOnStartup,
                onCheckedChange = onRandomThemeOnStartupChange,
            )
        }

        AnimatedVisibility(visible = !dynamicTheme || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            PreferenceEntry(
                title = { Text(stringResource(R.string.color_palette)) },
                description = stringResource(R.string.customize_theme_colors),
                icon = { Icon(painterResource(R.drawable.format_paint), null) },
                onClick = { navController.navigate("settings/appearance/palette_picker") }
            )
        }

        EnumListPreference(
            title = { Text(stringResource(R.string.dark_theme)) },
            icon = { Icon(painterResource(R.drawable.dark_mode), null) },
            selectedValue = darkMode,
            onValueSelected = onDarkModeChange,
            valueText = {
                when (it) {
                    DarkMode.ON -> stringResource(R.string.dark_theme_on)
                    DarkMode.OFF -> stringResource(R.string.dark_theme_off)
                    DarkMode.AUTO -> stringResource(R.string.dark_theme_follow_system)
                }
            },
        )

        AnimatedVisibility(useDarkTheme) {
            SwitchPreference(
                title = { Text(stringResource(R.string.pure_black)) },
                icon = { Icon(painterResource(R.drawable.contrast), null) },
                checked = pureBlack && useDarkTheme,
                onCheckedChange = { newValue ->
                    if (useDarkTheme) {
                        onPureBlackChange(newValue)
                    }
                },
                isEnabled = useDarkTheme
            )
        }

        SwitchPreference(
            title = { Text(stringResource(R.string.disable_blur)) },
            description = stringResource(R.string.disable_blur_desc),
            icon = { Icon(painterResource(R.drawable.blur_off), null) },
            checked = disableBlur,
            onCheckedChange = onDisableBlurChange,
        )

        SwitchPreference(
            title = { Text(stringResource(R.string.use_system_font)) },
            description = stringResource(R.string.use_system_font_desc),
            icon = { Icon(painterResource(R.drawable.text_fields), null) },
            checked = useSystemFont,
            onCheckedChange = onUseSystemFontChange,
        )

    }

    TopAppBar(
        title = { Text(stringResource(R.string.appearance)) },
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


enum class DarkMode {
    ON,
    OFF,
    AUTO,
}

enum class NavigationTab {
    HOME,
    SEARCH,
    LIBRARY,
}

enum class LyricsPosition {
    LEFT,
    CENTER,
    RIGHT,
}

enum class PlayerTextAlignment {
    SIDED,
    CENTER,
}
