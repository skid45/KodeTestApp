package com.skid.kodetestapp.data.mapper

import com.skid.kodetestapp.data.model.UserNetworkEntity
import com.skid.kodetestapp.domain.model.UserItem
import java.time.format.DateTimeFormatter
import java.util.Locale

fun UserNetworkEntity.toUserItem(): UserItem {
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale.forLanguageTag("ru"))
    return UserItem(
        id = id,
        avatarUrl = avatarUrl,
        name = "$firstName $lastName",
        birthday = birthday,
        department = department.getDisplayDepartment(),
        userTag = userTag.lowercase(),
        phone = phone,
        monthDayOfBirthday = birthday.format(formatter).dropLastWhile { it == '.' }
    )
}

private fun String.getDisplayDepartment(): String {
    return when (this) {
        "android" -> "Android"
        "ios" -> "iOS"
        "design" -> "Дизайн"
        "management" -> "Менеджмент"
        "qa" -> "QA"
        "back_office" -> "Бэк-офис"
        "frontend" -> "Frontend"
        "hr" -> "HR"
        "pr" -> "PR"
        "backend" -> "Backend"
        "support" -> "Техподдержка"
        "analytics" -> "Аналитика"
        else -> this
    }
}