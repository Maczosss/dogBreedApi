package com.epam.mentoring.kotlin.dogbreedapi.controller

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import com.epam.mentoring.kotlin.dogbreedapi.service.DogBreedService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
class DogBreedControllerTests {

    private lateinit var webClient: WebTestClient

    private lateinit var repository: DogBreedRepository
    private lateinit var apiClient: DogBreedApiClient
    private lateinit var service: DogBreedService

    val breeds = listOf(
        DogBreed(
            breed = "dummyBreed1",
            subBreed = "dummySubBreed1, dummySubBreed2",
            image = byteArrayOf(5, 4, 3, 2, 1)
        ),
        DogBreed(
            breed = "dummyBreed2",
            subBreed = "dummySubBreed3, dummySubBreed4, dummySubBreed5, dummySubBreed6",
            image = byteArrayOf(1, 2, 3, 4, 5)
        ),
        DogBreed(
            breed = "",
            subBreed = "nowasub",
            image = null
        ),
        DogBreed(
            breed = "dummyBreed3",
            subBreed = "",
            image = null
        )
    )

    @BeforeEach
    fun setUpRepository() {

        repository = mockk<DogBreedRepository>("repository")
        apiClient = mockk<DogBreedApiClient>("apiClient")
        service = DogBreedService(repository, apiClient)

        coEvery { repository.findAll() } returns breeds.asFlow()

        coEvery { repository.findAllBreedsWhereThereAreNoSubBreeds() } returns breeds
            .filter { it.subBreed.isEmpty() }
            .asFlow()

        coEvery { repository.getOnlySubBreeds() } returns breeds
            .filter { it.subBreed.isNotEmpty() }
            .map { it.subBreed }
            .asFlow()

        coEvery { repository.getBreedsSubBreeds("dummyBreed1") } returns flowOf(breeds[0].subBreed)
        coEvery { repository.getBreedsSubBreeds("dummyBreed2") } returns flowOf(breeds[1].subBreed)
        coEvery { repository.getBreedsSubBreeds("") } returns flowOf(breeds[2].subBreed)
        coEvery { repository.getBreedsSubBreeds("dummyBreed3") } returns flowOf(breeds[3].subBreed)

        coEvery { repository.getBreedByName("dummyBreed1") } returns breeds[0]
        coEvery { repository.getBreedByName("dummyBreed2") } returns breeds[1]
        coEvery { repository.getBreedByName("") } returns breeds[2]
        coEvery { repository.getBreedByName("dummyBreed3") } returns breeds[3]

        coEvery { apiClient.getBreedPictureFromExternalApiAndSaveNewLinkInDB("dummyBreed3") } returns null

        webClient = WebTestClient.bindToController(DogBreedController(service)).build()
    }
    @Test
    fun testGetAllDogBreeds() {
        webClient.get().uri("/breeds")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                  {
                      "breed": "dummyBreed1",
                      "subBreeds": [
                          "dummySubBreed1",
                          "dummySubBreed2"
                      ]
                  },
                  {
                      "breed": "dummyBreed2",
                      "subBreeds": [
                      "dummySubBreed3",
                      "dummySubBreed4",
                      "dummySubBreed5",
                      "dummySubBreed6"
                      ]
                  },
                  {
                      "breed": "",
                      "subBreeds": [
                      "nowasub"
                      ]
                  },
                  {
                      "breed": "dummyBreed3",
                      "subBreeds": [
                      ""
                  ]
                  }
    ]
            """.trimIndent()
            )
    }

    @Test
    fun testGetBreedsWithNoSubBreeds() {
        webClient.get().uri("/breeds/nonSubBreed")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                  {
                      "breed": "dummyBreed3",
                      "subBreeds": [
                      ""
                  ]
                  }
                ]
            """.trimIndent()
            )
    }

    @Test
    fun testGetOnlySubBreeds() {
        webClient.get().uri("/breeds/subBreeds")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                "dummySubBreed1, dummySubBreed2",
                "dummySubBreed3, dummySubBreed4, dummySubBreed5, dummySubBreed6",
                "nowasub"
                ]
            """.trimIndent()
            )
    }

    @Test
    fun testGetBreedsSubBreeds() {
        webClient.get().uri("/breeds/subBreedsForSpecificBreed/{breed}", "dummyBreed1")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                "dummySubBreed1, dummySubBreed2"
                ]
            """.trimIndent()
            )
        webClient.get().uri("/breeds/subBreedsForSpecificBreed/{breed}", "dummyBreed2")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                "dummySubBreed3, dummySubBreed4, dummySubBreed5, dummySubBreed6"
                ]
            """.trimIndent()
            )
        webClient.get().uri("/breeds/subBreedsForSpecificBreed/{breed}", "dummyBreed3")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                ""
                ]
            """.trimIndent()
            )
        webClient.get().uri("/breeds/subBreedsForSpecificBreed/{breed}", " ")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(
                """
                [
                "nowasub"
                ]
            """.trimIndent()
            )
    }

    @Test
    fun testGetBreedPicture(){
        val expectedByteArray = webClient.get().uri("/breeds/getPicture/{breed}", "dummyBreed1")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.IMAGE_JPEG)
            .expectBody(ByteArray::class.java)
            .returnResult().responseBody

        val expectedByteArray2 = webClient.get().uri("/breeds/getPicture/{breed}", "dummyBreed2")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.IMAGE_JPEG)
            .expectBody(ByteArray::class.java)
            .returnResult().responseBody

        val expectedNull = webClient.get().uri("/breeds/getPicture/{breed}", "dummyBreed3")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.IMAGE_JPEG)
            .expectBody(ByteArray::class.java)
            .returnResult().responseBody

        Assertions.assertTrue(expectedByteArray contentEquals byteArrayOf(5, 4, 3, 2, 1))
        Assertions.assertTrue(expectedByteArray2 contentEquals byteArrayOf(1, 2, 3, 4, 5))
        Assertions.assertNull(expectedNull)
    }
}
