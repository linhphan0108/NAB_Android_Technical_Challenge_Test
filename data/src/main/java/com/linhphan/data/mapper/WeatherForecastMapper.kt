package com.linhphan.data.mapper

import com.linhphan.data.entity.ForecastResponse
import com.linhphan.data.extensions.getAvgTemp
import com.linhphan.data.extensions.toPercent
import com.linhphan.data.extensions.toStringDate
import com.linhphan.domain.entity.ForecastEntity

fun ForecastResponse.toWeatherInfo(): ForecastEntity {
    return ForecastEntity(
        date = dt.toStringDate(),
        avgTemp = temp.getAvgTemp(),
        humidity = humidity.toPercent(),
        pressure = pressure.toString(),
        desc = weathers.joinToString { weather -> weather.description }
    )
}

//fun ForecastResponse.toLocalWeatherInfo(cityName: String): LocalWeatherInfo {
//    return LocalWeatherInfo(
//        cityName = cityName,
//        dt = dt,
//        temp = temp.toJson(),
//        humidity = humidity,
//        pressure = pressure,
//        weathers = weathers.toJson()
//    )
//}

//fun LocalWeatherInfo.toWeatherInfoResponse(): WeatherInfoResponse {
//    return WeatherInfoResponse(
//        dt = dt,
//        temp = Gson().fromJson(temp, TempResponse::class.java),
//        humidity = humidity,
//        pressure = pressure,
//        weathers = Gson().fromJson(
//            weathers, object : TypeToken<List<WeatherDescResponse>?>() {}.type
//        )
//    )
//}