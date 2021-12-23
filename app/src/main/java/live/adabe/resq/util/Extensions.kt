package live.adabe.resq.util

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import live.adabe.resq.model.LocationDTO

fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}

fun convertToDto(location: Location): LocationDTO = LocationDTO(location.longitude, location
    .latitude)