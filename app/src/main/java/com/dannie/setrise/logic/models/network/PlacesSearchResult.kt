package com.dannie.setrise.logic.models.network

import com.google.gson.annotations.SerializedName

data class Candidates(
    @SerializedName("results") val candidates: ArrayList<Candidate>?
)

data class Candidate(
    @SerializedName("formatted_address") val formattedAddress: String?,
    @SerializedName("geometry") val geometry: Geometry?,
    @SerializedName("name") val name: String?
)

data class Geometry(
    @SerializedName("location") val location: Location?
)

data class Location(
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?
)