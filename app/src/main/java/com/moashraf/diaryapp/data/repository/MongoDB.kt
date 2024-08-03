package com.moashraf.diaryapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.moashraf.diaryapp.model.Diary
import com.moashraf.diaryapp.model.RequestState
import com.moashraf.diaryapp.utils.Constants.APP_ID
import com.moashraf.diaryapp.utils.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.time.ZoneId


object MongoDB : MongoDBRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var  realm : Realm

    init {
        configureRealm()
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllDiaries(): Flow<Diaries> {
        return if (user != null){
            try {
                realm.query<Diary>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING).asFlow().map { result ->
                        RequestState.Success(data = result.list.groupBy {
                            it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        })
                    }
            }
            catch (e : Exception){
                flow { emit(RequestState.Error(e)) }
            }
        }else{
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }
}
private class UserNotAuthenticatedException : Exception("User is not Logged in.")
