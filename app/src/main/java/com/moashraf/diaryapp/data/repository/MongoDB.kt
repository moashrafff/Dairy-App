package com.moashraf.diaryapp.data.repository

import com.moashraf.diaryapp.model.Diary
import com.moashraf.diaryapp.utils.Constants.APP_ID
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration

object MongoDB : MongoDBRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var  realm : Realm
    override fun configureRealm() {
        if (user != null) {
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Diary>(query = "ownerId == $0", user.id),
                        name = "User's Diaries"
                    )
                }
//                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
        }
    }
}