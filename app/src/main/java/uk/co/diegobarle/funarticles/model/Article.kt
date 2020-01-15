package uk.co.diegobarle.funarticles.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Long,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String,

    @ColumnInfo(name = "subtitle")
    @SerializedName("subtitle")
    var subtitle: String,

    @ColumnInfo(name = "body")
    @SerializedName("body")
    var body: String?,

    @ColumnInfo(name = "date")
    @SerializedName("date")
    var date: String
){
    override fun equals(other: Any?): Boolean {
        return other != null
                && other is Article
                && other.id == id
                && other.title == title
                && other.date == date
                && other.subtitle == subtitle
                && other.body == body
    }
}