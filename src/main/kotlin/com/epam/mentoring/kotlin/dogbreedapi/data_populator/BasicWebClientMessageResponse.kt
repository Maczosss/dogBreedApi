package com.epam.mentoring.kotlin.dogbreedapi.data_populator

class BasicWebClientMessageResponse (var status: String,
                                     var message: List<String>
){
    constructor(): this(
    "",
    mutableListOf()
    )
}