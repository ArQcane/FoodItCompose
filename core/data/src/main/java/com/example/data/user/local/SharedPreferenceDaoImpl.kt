package com.example.data.user.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceDaoImpl @Inject constructor(
    @ApplicationContext context: Context
) : SharedPreferenceDao {
    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREF_NAME,
        MODE_PRIVATE
    )

    override fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(TOKEN, token)
            .apply()
    }

    override fun getToken(): Resource<String> {
        val token = sharedPreferences.getString(TOKEN, null)
            ?: return Resource.Failure(
                ResourceError.Default("Not logged in. Login to continue")
            )
        return Resource.Success(token)
    }

    companion object {
        const val SHARED_PREF_NAME = "FOODIT_PREFS"
        const val TOKEN = "FOODIT_TOKEN"
    }
}