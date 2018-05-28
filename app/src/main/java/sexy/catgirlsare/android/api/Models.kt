package sexy.catgirlsare.android.api

import com.google.gson.annotations.SerializedName

data class Credentials(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)

data class CredentialsResponse(
    @SerializedName("type")
    val type: String,
    @SerializedName("data")
    val data: ResponseData
) {
    data class ResponseData(
        @SerializedName("message")
        val message: String,
        @SerializedName("apiKey")
        val key: String?
    )
}

data class DisownRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("file")
    val file: String
)

data class DisownResponse(
    @SerializedName("error")
    val error: String?,
    @SerializedName("success")
    val success: String?
) {
    val isSuccessful = error.isNullOrBlank()
}

data class UploadsRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("page")
    val page: Int
)

data class Upload(
    @SerializedName("path")
    val path: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("original_name")
    val originalName: String
)
