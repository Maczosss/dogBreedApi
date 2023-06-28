package com.epam.mentoring.kotlin.dogbreedapi.data

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.NamedQuery
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.util.*


    @Entity
    @Table(name = "dog_breed")
    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @NamedQuery(
//        name = "Note.findByTitle",
//        query = "SELECT n FROM dog_breed n WHERE n.breed LIKE :breed"
//    )
    class DogBreed(
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        @Column(columnDefinition = "varchar(36)")
        var id: String = "",
        var breed: String,
        var subBreed: String,
        var image: String = "",
        @CreationTimestamp
        var created: Date = Date(),
        @UpdateTimestamp
        var modified: Date = Date()
    ){

        constructor(): this(
            "","","", ""
        )
    }