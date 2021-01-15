package com.example.wstest.screen.auth.model

import com.example.wstest.database.entiry.UserEntity
import com.example.wstest.database.entiry.UserImageEntity

data class UserAndImage(val userEntity: UserEntity, val userImageEntity: UserImageEntity)
