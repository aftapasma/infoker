package org.d3if.infoker.model

data class Job(
    val id: String = "",
    val title: String = "",
    val company: String = "",
    val location: String = "",
    val salary: Float = 0.0f,
    val description: String = ""
)
