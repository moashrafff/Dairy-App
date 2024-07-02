package com.moashraf.diaryapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}