package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GmsCoreInfo
import com.example.ui.theme.YTRed

@Composable
fun GmsCoreSettingsScreen(
    gmsInfo: GmsCoreInfo,
    onRefreshGmsStatus: () -> Unit,
    onLaunchGmsSettings: () -> Boolean,
    onToggleSponsorBlock: () -> Unit,
    onToggleReturnDislike: () -> Unit,
    onToggleBackgroundPlay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Title Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(YTRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "YouTube cx",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "Version 0.2",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = YTRed
                            )
                        }
                    }

                    IconButton(onClick = onRefreshGmsStatus) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh Status")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.dp))

                // GMS Package Info Box
                Text(
                    text = "MicroG / GMS Core Status",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (gmsInfo.isInstalled) Color(0xFF132A1D) else Color(0xFF2A1313)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (gmsInfo.isInstalled) Icons.Default.CheckCircle else Icons.Default.Info,
                                contentDescription = null,
                                tint = if (gmsInfo.isInstalled) Color(0xFF4CAF50) else Color(0xFFFF5252)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Package: ${gmsInfo.packageName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (gmsInfo.isInstalled)
                                "Status: Installed & Connected (${gmsInfo.versionName})"
                            else
                                "Status: Package com.gmscx.services not detected on system. Install gms-core with package name com.gmscx.services for Google Account sync.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { onLaunchGmsSettings() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (gmsInfo.isInstalled) Color(0xFF2E7D32) else YTRed
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("launch_gms_btn")
                        ) {
                            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (gmsInfo.isInstalled) "Open com.gmscx.services App Settings" else "Launch GMS Core com.gmscx.services",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account Sync Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Google Account Sync (com.gmscx.services)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Active Account: ${gmsInfo.accountEmail}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF60A5FA)
                )

                Text(
                    text = "Account credentials and sync tokens retrieved via package com.gmscx.services AccountManager",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { onLaunchGmsSettings() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Manage Accounts in com.gmscx.services")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // YouTube CX Custom Patches & Mods Section
        Text(
            text = "YouTube CX Ad Shield & Mod Settings",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Ad Shield Video Ad Blocker
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Ad Shield (Video Ad Blocker)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Block all YouTube preroll, midroll, and banner advertisements", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Switch(
                    checked = true,
                    onCheckedChange = { },
                    colors = SwitchDefaults.colors(checkedThumbColor = YTRed)
                )
            }
        }

        // SponsorBlock
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "SponsorBlock (Ad Shield)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Automatically skip sponsored segments and video intros", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Switch(
                    checked = gmsInfo.sponsorBlockEnabled,
                    onCheckedChange = { onToggleSponsorBlock() },
                    colors = SwitchDefaults.colors(checkedThumbColor = YTRed)
                )
            }
        }

        // Return YouTube Dislike
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.ThumbDown, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Return YouTube Dislike (RYD)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Fetch exact dislike counts for all videos and shorts", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Switch(
                    checked = gmsInfo.returnDislikeEnabled,
                    onCheckedChange = { onToggleReturnDislike() },
                    colors = SwitchDefaults.colors(checkedThumbColor = YTRed)
                )
            }
        }

        // Background Playback
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Headphones, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Background Audio Engine", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Continue video audio playback when screen is turned off or minimized", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Switch(
                    checked = gmsInfo.backgroundPlayEnabled,
                    onCheckedChange = { onToggleBackgroundPlay() },
                    colors = SwitchDefaults.colors(checkedThumbColor = YTRed)
                )
            }
        }

        // Preferred Quality
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = YTRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Default Quality Preference", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Target Wi-Fi video resolution: ${gmsInfo.preferredQuality}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
