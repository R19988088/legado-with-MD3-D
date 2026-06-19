package io.legado.app.ui.book.read

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.capsule.ContinuousCapsule
import io.legado.app.ui.config.themeConfig.ThemeConfig
import io.legado.app.ui.widget.components.FloatingBottomBar
import io.legado.app.ui.widget.components.FloatingBottomBarConfig
import io.legado.app.ui.widget.components.LocalFloatingBottomBarTabScale

data class ReaderBottomBarAction(
    val id: String,
    @DrawableRes val iconRes: Int,
    val label: String,
    val checked: Boolean = false,
    val badgeCount: Int = 0,
    val onClick: () -> Unit,
    val onLongClick: (() -> Unit)? = null
)

fun readerFloatingBottomBarConfig(): FloatingBottomBarConfig {
    return FloatingBottomBarConfig.resolve(
        useFloatingBottomBar = true,
        useFloatingBottomBarLiquidGlass = ThemeConfig.useFloatingBottomBarLiquidGlass,
        blurRadius = ThemeConfig.bottomBarBlurRadius,
        blurAlpha = ThemeConfig.bottomBarBlurAlpha,
        lensRadius = ThemeConfig.bottomBarLensRadius
    )
}

@Composable
fun ReaderFloatingBottomBar(
    actions: List<ReaderBottomBarAction>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    if (actions.isEmpty()) return
    val safeSelectedIndex = selectedIndex.coerceIn(actions.indices)
    val backdrop = rememberLayerBackdrop()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .layerBackdrop(backdrop)
            )
            FloatingBottomBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                selectedIndex = { safeSelectedIndex },
                onSelected = {},
                backdrop = backdrop,
                tabsCount = actions.size,
                config = readerFloatingBottomBarConfig()
            ) {
                actions.forEach { action ->
                    ReaderFloatingBottomBarItem(action = action, showLabel = showLabel)
                }
            }
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .padding(4.dp)
            ) {
                actions.forEach { action ->
                    ReaderFloatingBottomBarHitTarget(action)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowScope.ReaderFloatingBottomBarHitTarget(action: ReaderBottomBarAction) {
    Box(
        Modifier
            .fillMaxHeight()
            .weight(1f)
            .clip(ContinuousCapsule)
            .combinedClickable(
                role = Role.Button,
                onClick = action.onClick,
                onLongClick = action.onLongClick
            )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RowScope.ReaderFloatingBottomBarItem(
    action: ReaderBottomBarAction,
    showLabel: Boolean
) {
    val scale = LocalFloatingBottomBarTabScale.current
    Column(
        Modifier
            .defaultMinSize(minWidth = 64.dp)
            .clip(ContinuousCapsule)
            .fillMaxHeight()
            .weight(1f)
            .graphicsLayer {
                val currentScale = scale()
                scaleX = currentScale
                scaleY = currentScale
            },
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
            1.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReaderFloatingBottomBarIcon(action)
        if (showLabel) {
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
private fun ColumnScope.ReaderFloatingBottomBarIcon(action: ReaderBottomBarAction) {
    val icon: @Composable () -> Unit = {
        Icon(
            painter = painterResource(action.iconRes),
            contentDescription = action.label,
            tint = Color.Unspecified,
            modifier = Modifier.size(26.dp)
        )
    }
    if (action.badgeCount != 0 && action.checked) {
        BadgedBox(
            badge = {
                Badge {
                    Text(action.badgeCount.coerceAtMost(999).toString())
                }
            }
        ) {
            icon()
        }
    } else {
        icon()
    }
}
