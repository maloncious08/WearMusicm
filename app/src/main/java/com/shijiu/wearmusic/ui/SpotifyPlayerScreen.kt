package com.lanmiemie.wearmusic.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import coil.compose.AsyncImage

val SpotifyGreen = Color(0xFF1DB954)
val SpotifyDarkGray = Color(0xFF282828)
val SpotifySubText = Color(0xFFB3B3B3)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpotifyWearPlayerScreen(
    title: String,
    artist: String,
    albumArtUrl: Any?, // 支持 URL、本地 URI 或 Drawable ID
    isPlaying: Boolean,
    isLiked: Boolean,
    progress: Float, // 0f ~ 1f
    onPlayPauseClick: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onLikeToggle: () -> Unit,
    onVolumeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 默认停留在第 1 页（控制面板），向下滑动查看第 0 页（纯封面）
    val pagerState = rememberPagerState(initialPage = 1) { 2 }

    VerticalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { page ->
        when (page) {
            0 -> {
                // ================= Page 0: 无遮挡纯封面 =================
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = albumArtUrl,
                        contentDescription = "Full Album Art",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            1 -> {
                // ================= Page 1: Spotify 播放控制主页 =================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // 1. 曲目与歌手（防溢出跑马灯）
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title.ifEmpty { "未在播放" },
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .basicMarquee(iterations = Int.MAX_VALUE)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = artist.ifEmpty { "未知歌手" },
                            color = SpotifySubText,
                            fontSize = 12.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .basicMarquee(iterations = Int.MAX_VALUE)
                        )
                    }

                    // 2. 核心大触控按键区
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 上一曲
                        Button(
                            onClick = onSkipPrevious,
                            modifier = Modifier.size(42.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Previous",
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        // 居中大号反色播放/暂停按键
                        Button(
                            onClick = onPlayPauseClick,
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        // 下一曲
                        Button(
                            onClick = onSkipNext,
                            modifier = Modifier.size(42.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Next",
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    // 3. 底部进度条与辅助按键
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 线性进度条
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.55f)
                                .height(3.dp)
                                .clip(CircleShape)
                                .background(SpotifyDarkGray)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                                    .fillMaxHeight()
                                    .background(SpotifyGreen)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(0.65f),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onLikeToggle,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Like",
                                    tint = if (isLiked) SpotifyGreen else SpotifySubText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            IconButton(
                                onClick = onVolumeClick,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Volume",
                                    tint = SpotifySubText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
    
