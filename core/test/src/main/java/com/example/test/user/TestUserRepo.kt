package com.example.test.user

import android.media.Image
import com.example.domain.user.ReviewUser
import com.example.domain.user.User
import com.example.domain.user.UserRepository
import com.example.domain.utils.Resource
import com.example.domain.utils.ResourceError
import java.util.*

class TestUserRepo : UserRepository {
    var token: String? = UUID.randomUUID().toString()
    var users: List<User> = emptyList()

    init {
        repeat(10) {
            users = users.toMutableList().apply {
                val user = User(
                    user_id = it,
                    first_name = "firstname$it",
                    last_name = "lastname$it",
                    username = "username$it",
                    gender = "M",
                    mobile_number = 6587789994,
                    email = "$it${10 - it}@gmail.com",
                    address = "living in street$it",
                    profile_pic = null,
                )
                add(user)
            }
        }
    }

    override fun getToken(): Resource<String> {
        return token?.let {
            Resource.Success(it)
        } ?: Resource.Failure(
            ResourceError.Default(
                error = "Must be logged in to do this task"
            )
        )
    }

    override fun saveToken(token: String) {
        this.token = token
    }

    override fun deleteToken() {
        token = null
    }

    override suspend fun getAllUsers(): Resource<List<User>> {
        return Resource.Success(users)
    }

    override suspend fun getUserById(id: String): Resource<ReviewUser> {
        return users.find { it.user_id.toString() == id }?.let {
            Resource.Success(ReviewUser(it.username, it.profile_pic!!))
        } ?: Resource.Failure(
            ResourceError.Default("Cannot find user with id $id")
        )
    }

    override suspend fun validateToken(token: String): Resource<User> {
        if (this.token != token) return Resource.Failure(
            ResourceError.Default("Invalid Token")
        )
        return Resource.Success(users.last())
    }

    override suspend fun forgotPassword(email: String): Resource<String> {
        return Resource.Success("Sent email successfully")
    }

    override suspend fun updateAccount(
        userId: String?,
        firstName: String?,
        lastName: String?,
        phoneNumber: Long?,
        address: String?,
        profile_pic: String?,
        deleteImage: Boolean?
    ): Resource<String> {
        if (this.token != token) return Resource.Failure(
            ResourceError.Default("Invalid token")
        )
        val user = users.last()
        var updatedImage = profile_pic?.let {
            ""
        } ?: user.profile_pic
        if (deleteImage == true) updatedImage = null
        val updated = user.copy(
            first_name = firstName ?: user.first_name,
            last_name = lastName ?: user.last_name,
            profile_pic = updatedImage
        )
        users = users.toMutableList().apply {
            val index = map { it.user_id }.indexOf(updated.user_id)
            set(index, updated)
        }
        return Resource.Success("Updated account with id ${updated.user_id}")
    }

    override suspend fun deleteAccount(userId: String): Resource<String> {
        if (this.token != token) return Resource.Failure(
            ResourceError.Default("Invalid token")
        )
        val account = users.last()
        users = users.filter { it.user_id != account.user_id }
        return Resource.Success("Successfully deleted account")
    }

    override suspend fun login(username: String, user_pass: String): Resource<String> {
        val account = users.find { it.username == username } ?: return Resource.Failure(
            ResourceError.Default("Account does not exist")
        )
        users = users.toMutableList().apply {
            val index = map { it.user_id }.indexOf(account.user_id)
            removeAt(index)
            add(account)
        }
        return Resource.Success(
            UUID.randomUUID().toString()
        )
    }

    override suspend fun register(
        first_name: String,
        last_name: String,
        username: String,
        user_pass: String,
        email: String,
        mobile_number: Long,
        gender: String,
        address: String,
        profile_pic: String?
    ): Resource<String> {
        users = users.toMutableList().apply {
            add(
                User(
                    user_id = users.size,
                    first_name = first_name,
                    last_name = last_name,
                    username = username,
                    email = email,
                    gender = "",
                    mobile_number = 65,
                    address = "",
                    profile_pic = null,
                )
            )
        }
        return Resource.Success(UUID.randomUUID().toString())
    }
}