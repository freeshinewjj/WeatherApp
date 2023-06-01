package com.jay.weatherapp.common.domain.model.weather

data class SearchParameter(
    val city: String,
    val state: String? = null,
    val country: String? = null
) {
    fun toSearchString(): String {
        var ans = city
        state?.let {
            ans += ",$it"
        }
        country?.let {
            ans += ",$it"
        }
        return ans
    }
}
