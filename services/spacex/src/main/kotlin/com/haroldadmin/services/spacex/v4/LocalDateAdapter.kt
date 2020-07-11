package com.haroldadmin.services.spacex.v4

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter {

   @FromJson
   fun fromJson(json: String): LocalDate? {
      val format = DateTimeFormatter.ISO_LOCAL_DATE
      return LocalDate.parse(json, format)
   }

   @ToJson
   fun toJson(localDate: LocalDate): String? {
      return localDate.toString()
   }

}