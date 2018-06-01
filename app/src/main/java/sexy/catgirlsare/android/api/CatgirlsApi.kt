package sexy.catgirlsare.android.api

import android.util.Log
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.InputStream
import java.nio.charset.Charset

private const val API_URL = "https://catgirlsare.sexy/api/"

private val api: CatgirlsApi = Retrofit.Builder()
    .baseUrl(API_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder().apply {
        addNetworkInterceptor { chain ->
            val request = chain.request()

            val requestBuffer = Buffer()
            request.body()?.writeTo(requestBuffer)
            val requestBody = requestBuffer.readString(Charset.forName("UTF-8")) ?: ""

            val startTime = System.currentTimeMillis()
            val response = chain.proceed(request)
            val time = System.currentTimeMillis() - startTime

            val contentType = response.body()?.contentType()
            val responseBody = response.body()?.string() ?: ""

            var message = "${request.method()} ${request.url()}\n"
            message += "${response.code()} ${response.message()}".trim()
            message += " ${time}ms"
            if (!requestBody.isBlank()) message += "\n\nreq: $requestBody"
            if (!responseBody.isBlank()) message += "\n\nres: $responseBody"
            Log.d("HTTP", message)

            response.newBuilder()
                .body(ResponseBody.create(contentType, responseBody))
                .build()
        }
    }.build())
    .build()
    .create(CatgirlsApi::class.java)

var key: String = ""
fun setApiKey(apiKey: String) {
    key = apiKey
}

fun login(username: String, password: String) = api.login(Credentials(username, password)).execute()!!
fun isAdmin() = api.isAdmin(IsAdminRequest(key)).execute()!!
fun disown(file: String) = api.disown(DisownRequest(key, file)).execute()!!
fun getUploads(page: Int, count: Int) = api.getUploads(UploadsRequest(key, count, page)).execute()!!
fun upload(name: String, stream: InputStream, size: Long): Response<UploadResponse> {
    val bytes = stream.readBytes(size.toInt())
    stream.close()
    return api.upload(
        MultipartBody.Part.createFormData("file", name, RequestBody.create(null, bytes)),
        RequestBody.create(null, key)
    ).execute()!!
}

interface CatgirlsApi {

    @POST("user/auth")
    fun login(@Body credentials: Credentials): Call<CredentialsResponse>

    @POST("user/is_admin")
    fun isAdmin(@Body request: IsAdminRequest): Call<IsAdminResponse>

    @POST("disown")
    fun disown(@Body request: DisownRequest): Call<DisownResponse>

    @POST("uploads")
    fun getUploads(@Body request: UploadsRequest): Call<UploadList>

    @Multipart
    @POST("upload")
    fun upload(@Part file: MultipartBody.Part, @Part("key") key: RequestBody): Call<UploadResponse>
}
