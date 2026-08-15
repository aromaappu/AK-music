/*
 * OpenTune Project Original (2026)
 * Arturo254 (github.com/Arturo254)
 * Licensed Under GPL-3.0 | see git history for contributors
 */

package com.aromaappu.akmusic.ui.screens.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.aromaappu.akmusic.BuildConfig
import com.aromaappu.akmusic.R

@Composable
fun buildQuickActions(
    navController: NavController,
    resetSearch: () -> Unit,
): List<SettingsQuickAction> =
    listOf(
        SettingsQuickAction(
            icon = painterResource(R.drawable.palette),
            label = stringResource(R.string.appearance),
            onClick = { resetSearch(); navController.navigate("settings/appearance") },
            accentColor = MaterialTheme.colorScheme.primary,
        ),
        SettingsQuickAction(
            icon = painterResource(R.drawable.play),
            label = stringResource(R.string.player_and_audio),
            onClick = { resetSearch(); navController.navigate("settings/player") },
            accentColor = MaterialTheme.colorScheme.tertiary,
        ),
        SettingsQuickAction(
            icon = painterResource(R.drawable.security),
            label = stringResource(R.string.privacy),
            onClick = { resetSearch(); navController.navigate("settings/privacy") },
            accentColor = MaterialTheme.colorScheme.error,
        ),
        SettingsQuickAction(
            icon = painterResource(R.drawable.storage),
            label = stringResource(R.string.storage),
            onClick = { resetSearch(); navController.navigate("settings/storage") },
            accentColor = MaterialTheme.colorScheme.secondary,
        ),
    )

@Composable
fun buildSettingsGroups(
    navController: NavController,
    hasUpdate: Boolean,
    resetSearch: () -> Unit,
): List<SettingsGroup> =
    buildList {
        add(
            SettingsGroup(
                title = stringResource(R.string.settings_section_player_content),
                items = listOf(
                    SettingsItem(
                        icon = painterResource(R.drawable.language),
                        title = stringResource(R.string.content),
                        subtitle = stringResource(R.string.content_language),
                        accentColor = MaterialTheme.colorScheme.secondary,
                        keywords = listOf("language", "content", "lyrics", "translation", "region"),
                        onClick = { resetSearch(); navController.navigate("settings/content") },
                    ),
                ),
            ),
        )

        add(
            SettingsGroup(
                title = stringResource(R.string.settings_section_storage),
                items = listOf(
                    SettingsItem(
                        icon = painterResource(R.drawable.storage),
                        title = stringResource(R.string.storage),
                        subtitle = stringResource(R.string.cache),
                        accentColor = MaterialTheme.colorScheme.secondary,
                        keywords = listOf("storage", "cache", "offline", "downloads", "cleanup"),
                        onClick = { resetSearch(); navController.navigate("settings/storage") },
                    ),
                    SettingsItem(
                        icon = painterResource(R.drawable.restore),
                        title = stringResource(R.string.backup_restore),
                        subtitle = stringResource(R.string.action_backup),
                        accentColor = MaterialTheme.colorScheme.tertiary,
                        keywords = listOf("backup", "restore", "import", "export", "migration"),
                        onClick = { resetSearch(); navController.navigate("settings/backup_restore") },
                    ),
                ),
            ),
        )

        add(
            SettingsGroup(
                title = stringResource(R.string.settings_section_system),
                items = buildList {
                    add(
                        SettingsItem(
                            icon = painterResource(R.drawable.update),
                            title = stringResource(R.string.updates),
                            subtitle = if (hasUpdate) {
                                stringResource(R.string.new_version_available)
                            } else {
                                BuildConfig.VERSION_NAME
                            },
                            showUpdateIndicator = hasUpdate,
                            accentColor = if (hasUpdate) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            keywords = listOf("update", "version", "release", "changelog"),
                            onClick = { resetSearch(); navController.navigate("settings/update") },
                        ),
                    )
                    add(
                        SettingsItem(
                            icon = painterResource(R.drawable.info),
                            title = stringResource(R.string.about),
                            subtitle = "AK music",
                            accentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            keywords = listOf("about", "app info", "license", "contributors"),
                            onClick = { resetSearch(); navController.navigate("settings/about") },
                        ),
                    )
                },
            ),
        )
    }

@Composable
fun buildInternalItems(
    navController: NavController,
    resetSearch: () -> Unit,
): List<SettingsItem> =
    listOf(
        SettingsItem(
            icon = painterResource(R.drawable.palette),
            title = stringResource(R.string.theme_creator_title),
            subtitle = stringResource(R.string.theme_creator_subtitle),
            accentColor = MaterialTheme.colorScheme.primary,
            keywords = listOf("theme", "creator", "seed", "material", "palette", "import", "export"),
            onClick = { resetSearch(); navController.navigate("settings/appearance/theme_creator") },
        ),
        SettingsItem(
            icon = painterResource(R.drawable.palette),
            title = stringResource(R.string.customize_colors),
            subtitle = stringResource(R.string.appearance),
            accentColor = MaterialTheme.colorScheme.primary,
            keywords = listOf("palette", "color", "accent", "tone", "dynamic color"),
            onClick = { resetSearch(); navController.navigate("settings/appearance/palette_picker") },
        ),
        SettingsItem(
            icon = painterResource(R.drawable.image),
            title = stringResource(R.string.customize_background_title),
            subtitle = stringResource(R.string.appearance),
            accentColor = MaterialTheme.colorScheme.secondary,
            keywords = listOf("background", "wallpaper", "image", "blur", "gradient"),
            onClick = { resetSearch(); navController.navigate("customize_background") },
        ),
        SettingsItem(
            icon = painterResource(R.drawable.integration),
            title = stringResource(R.string.lastfm_integration),
            subtitle = stringResource(R.string.integration),
            accentColor = MaterialTheme.colorScheme.secondary,
            keywords = listOf("lastfm", "last.fm", "scrobble", "listening history"),
            onClick = { resetSearch(); navController.navigate("settings/lastfm") },
        ),
        SettingsItem(
            icon = painterResource(R.drawable.fire),
            title = stringResource(R.string.music_together),
            subtitle = stringResource(R.string.integration),
            accentColor = MaterialTheme.colorScheme.tertiary,
            keywords = listOf("together", "session", "sync", "party", "join", "host"),
            onClick = { resetSearch(); navController.navigate("settings/music_together") },
        ),
    )
