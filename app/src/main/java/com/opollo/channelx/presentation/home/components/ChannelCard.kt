package com.opollo.channelx.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.opollo.channelx.domain.Channel

@Composable
fun ChannelCard(
    channel: Channel,
    onClick:()->Unit,
    modifier: Modifier = Modifier
){
    Card(modifier = modifier.fillMaxWidth()
        .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )){
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)){
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ){
                AsyncImage(
                    model = channel.imageUrl,
                    contentDescription = "${channel.name} logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = channel.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = channel.countryCode.uppercase(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                StatusBadge(isOnline = channel.isOnline)

            }

            if(channel.viewers>0){
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${channel.viewers} viewers",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }



        }

    }

}

@Composable
private fun StatusBadge(
    isOnline:Boolean,
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if(isOnline){
            Color(0xFF4CAF50).copy(alpha=0.15f)
        }else {
            Color(0xFFFF5252).copy(alpha = 0.15f)
        }
    ){
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Box(modifier = Modifier.size(6.dp)
                .background(
                    color = if (isOnline) Color(0xFF4CAF50) else Color(0xFFFF5252),
                    shape = RoundedCornerShape(50.dp)
                ))

            Text(
                text = if(isOnline)"Online" else "Offline",
                style = MaterialTheme.typography.labelSmall,
                color = if (isOnline) Color(0xFF2E7D32) else Color(0xFFC62828),
                fontWeight = FontWeight.Medium
            )

        }

    }
}