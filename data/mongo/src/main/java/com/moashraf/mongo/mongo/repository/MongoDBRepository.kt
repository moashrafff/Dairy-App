package com.moashraf.mongo.mongo.repository

import com.moashraf.util.model.Diary
import com.moashraf.util.model.RequestState
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import java.time.LocalDate
import java.time.ZonedDateTime

typealias Diaries  = RequestState<Map<LocalDate, List<Diary>>>

interface MongoDBRepository {
    fun configureRealm()
    fun getAllDiaries() : Flow<Diaries>
    fun getSelectedDiary(diaryId : ObjectId) : Flow<RequestState<Diary>>
    fun getFilteredDiaries(zonedDateTime: ZonedDateTime): Flow<Diaries>
    suspend fun addNewDiary(diary : Diary) : RequestState<Diary>
    suspend fun updateDiary(diary: Diary) : RequestState<Diary>
    suspend fun deleteDiary(id: ObjectId): RequestState<Boolean>
    suspend fun deleteAllDiaries(): RequestState<Boolean>

}