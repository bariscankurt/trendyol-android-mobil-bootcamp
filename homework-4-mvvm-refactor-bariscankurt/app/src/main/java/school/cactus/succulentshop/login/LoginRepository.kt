package school.cactus.succulentshop.login

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.api.GenericRequestCallback
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.errorMessage
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse

class LoginRepository {
    fun sendLoginRequest(
        identifier: String,
        password: String,
        callback: GenericRequestCallback
    ) {
        val request = LoginRequest(identifier, password)

        api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                when (response.code()) {
                    200 -> callback.onSuccess(response.body()!!.jwt)
                    in 400..499 -> callback.onClientError(response.errorBody()!!.errorMessage())
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onFailure()
            }
        })
    }
}