package com.moashraf.diaryapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.moashraf.diaryapp.utils.toRealmInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
class Diary : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId.invoke()
    var ownerId: String = ""
    var title : String = ""
    var description : String = ""
    var date : RealmInstant = Instant.now().toRealmInstant()
    var mood : String = Mood.Neutral.name
    var images : RealmList<String> = realmListOf()
}