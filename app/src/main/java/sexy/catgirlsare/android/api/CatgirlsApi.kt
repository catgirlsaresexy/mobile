package sexy.catgirlsare.android.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val API_URL = "https://catgirlsare.sexy/api"

private val api: CatgirlsApi = Retrofit.Builder()
    .baseUrl(API_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(CatgirlsApi::class.java)

var key: String = ""

fun login(username: String, password: String) = api.login(Credentials(username, password)).execute()
fun disown(file: String) = api.disown(DisownRequest(key, file)).execute()
fun getUploads(page: Int, count: Int) = api.getUploads(UploadsRequest(key, count, page)).execute()

interface CatgirlsApi {

    @POST("auth/user")
    fun login(@Body credentials: Credentials): Call<CredentialsResponse>

    @POST("disown")
    fun disown(@Body request: DisownRequest): Call<DisownResponse>

    @POST("uploads")
    fun getUploads(@Body request: UploadsRequest): Call<List<Upload>>
}
