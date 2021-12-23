package live.adabe.resq.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LocationDTO(
    val longitude: Double,
    val latitude: Double
): Parcelable