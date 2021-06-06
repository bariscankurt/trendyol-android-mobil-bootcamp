package school.cactus.succulentshop.signup

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.api.GenericRequestCallback
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.errorMessage
import school.cactus.succulentshop.api.signup.SignupRequest
import school.cactus.succulentshop.api.signup.SignupResponse

class SignupRepository {
    fun sendSignupRequest(
        email: String,
        password: String,
        username: String,
        callback: GenericRequestCallback
    ) {
        val request = SignupRequest(email, password, username)
        api.register(request).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                when (response.code()) {
                    200 -> callback.onSuccess(response.body()!!.jwt)
                    in 400..499 -> callback.onClientError(response.errorBody()!!.errorMessage())
                    else -> callback.onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                callback.onFailure()
            }
        })
    }
}