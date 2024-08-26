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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import org.mongodb.kbson.ObjectId
import java.time.ZoneId


object MongoDB : MongoDBRepository {
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureRealm()
    }

    override fun configureRealm() {
        if (user != null) {
            val config =
                SyncConfiguration.Builder(user, setOf(Diary::class)).initialSubscriptions { sub ->
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
        return if (user != null) {
            try {
                realm.query<Diary>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .mapLatest { result ->
                        val groupedDiaries = result.list.groupBy {
                            it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        RequestState.Success(data = groupedDiaries)
                    }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override suspend fun deleteDiary(id: ObjectId): RequestState<Boolean> {
        return if (user != null) {
            try {
                realm.write {
                    val diary =
                        query<Diary>(query = "_id == $0 AND ownerId == $1", id, user.id)
                            .first().find()
                    if (diary != null) {
                        try {
                            // Log before deletion
                            delete(diary)
                            // Log after successful deletion
                            RequestState.Success(data = true)
                        } catch (e: Exception) {
                            RequestState.Error(e)
                        }
                    } else {
                        RequestState.Error(Exception("Diary does not exist."))
                    }
                }
            } catch (e: Exception) {
                RequestState.Error(e)
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }



    override fun getSelectedDiary(diaryId: ObjectId): Flow<RequestState<Diary>> {
        return if (user != null) {
            try {
                realm.query<Diary>(query = "_id == $0", diaryId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }
            } catch (e: Exception) {
                flow { emit(RequestState.Error(e)) }
            }
        } else {
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addNewDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                try {
                    val addedDiary = copyToRealm(diary.apply { ownerId = user.id })
                    RequestState.Success(addedDiary)
                } catch (e: Exception) {
                    RequestState.Error(e)
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun updateDiary(diary: Diary): RequestState<Diary> {
        return if (user != null) {
            realm.write {
                val queriedDiary = query<Diary>(query = "_id == $0", diary._id).first().find()
                if (queriedDiary != null) {
                    queriedDiary.title = diary.title
                    queriedDiary.description = diary.description
                    queriedDiary.mood = diary.mood
                    queriedDiary.images = diary.images
                    queriedDiary.date = diary.date
                    RequestState.Success(data = queriedDiary)
                } else {
                    RequestState.Error(error = Exception("Queried Diary does not exist."))
                }
            }
        } else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }


}

private class UserNotAuthenticatedException : Exception("User is not Logged in.")
