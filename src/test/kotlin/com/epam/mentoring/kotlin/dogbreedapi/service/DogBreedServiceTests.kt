package com.epam.mentoring.kotlin.dogbreedapi.service

import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreed
import com.epam.mentoring.kotlin.dogbreedapi.data.DogBreedDTO
import com.epam.mentoring.kotlin.dogbreedapi.data_populator.DogBreedApiClient
import com.epam.mentoring.kotlin.dogbreedapi.repository.DogBreedRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DogBreedServiceTests {

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

        coEvery { repository.getOnlySubBreeds() } returns breeds.map { it.subBreed }.asFlow()

        coEvery { repository.getBreedsSubBreeds("dummyBreed2") } returns flowOf(breeds[1].subBreed)
        coEvery { repository.getBreedsSubBreeds("dummyBreed3") } returns flowOf(breeds[3].subBreed)

        coEvery { repository.getBreedByName("dummyBreed1") } returns breeds[0]
        coEvery { repository.getBreedByName("dummyBreed2") } returns breeds[1]
        coEvery { repository.getBreedByName("") } returns breeds[2]
        coEvery { repository.getBreedByName("dummyBreed3") } returns breeds[3]

        coEvery { apiClient.getBreedPictureFromExternalApiAndSaveNewLinkInDB(any()) } returns byteArrayOf(1, 2, 3, 4, 5)
    }

    @Test
    fun testGetBreeds() {
        val expectedResult = listOf(
            DogBreedDTO(
                breed = "dummyBreed1",
                subBreed = arrayOf("dummySubBreed1", "dummySubBreed2"),
                image = byteArrayOf(5, 4, 3, 2, 1)
            ),
            DogBreedDTO(
                breed = "dummyBreed2",
                subBreed = arrayOf("dummySubBreed3", "dummySubBreed4", "dummySubBreed5", "dummySubBreed6"),
                image = byteArrayOf(1, 2, 3, 4, 5)
            ),
            DogBreedDTO(
                breed = "",
                subBreed = arrayOf("nowasub"),
                image = null
            ),
            DogBreedDTO(
                breed = "dummyBreed3",
                subBreed = arrayOf(),
                image = null
            )
        )

        val result = runBlocking { service.getBreeds() }

        result.zip(expectedResult).forEach { pair ->
            Assertions.assertTrue(
                pair.first.breed == pair.second.breed
            )
            Assertions.assertTrue(
                pair.first.subBreed stringArrayContentEquals pair.second.subBreed
            )
            Assertions.assertTrue(
                pair.first.image.contentEquals(pair.second.image)
            )
            Assertions.assertNotNull(
                pair.first.id
            )
            Assertions.assertNotNull(
                pair.second.id
            )
        }
    }

    @Test
    fun testGetBreedsWithNoSubBreeds() {
        val expected = DogBreedDTO(
            breed = "dummyBreed3",
            subBreed = arrayOf(),
            image = null
        )

        val result = runBlocking { service.getBreedsWithNoSubBreeds() }

        Assertions.assertTrue(
            result.take(1)[0].breed == expected.breed
        )
        Assertions.assertTrue(
            result.take(1)[0].subBreed stringArrayContentEquals expected.subBreed
        )
        Assertions.assertTrue(
            result.take(1)[0].image.contentEquals(expected.image)
        )
        Assertions.assertNotNull(
            result.take(1)[0].id
        )
    }

    @Test
    fun testGetOnlySubBreeds() {
        val expectedResult = listOf(
            DogBreedDTO(
                breed = "dummyBreed1",
                subBreed = arrayOf("dummySubBreed1", "dummySubBreed2"),
                image = byteArrayOf(5, 4, 3, 2, 1)
            ),
            DogBreedDTO(
                breed = "dummyBreed2",
                subBreed = arrayOf("dummySubBreed3", "dummySubBreed4", "dummySubBreed5", "dummySubBreed6"),
                image = byteArrayOf(1, 2, 3, 4, 5)
            ),
            DogBreedDTO(
                breed = "",
                subBreed = arrayOf("nowasub"),
                image = null
            ),
            DogBreedDTO(
                breed = "dummyBreed3",
                subBreed = arrayOf(),
                image = null
            )
        )

        val result = runBlocking { service.getOnlySubBreeds() }

        result.zip(expectedResult).forEach { pair ->
            Assertions.assertTrue(pair.first.split(",")
                .map { it.trim() }
                .toTypedArray() stringArrayContentEquals pair.second.subBreed)
        }
    }

    @Test
    fun testGetBreedsSubBreeds() {
        val expectedResult = listOf(
            DogBreedDTO(
                breed = "dummyBreed2",
                subBreed = arrayOf("dummySubBreed3", "dummySubBreed4", "dummySubBreed5", "dummySubBreed6"),
                image = byteArrayOf(1, 2, 3, 4, 5)
            ),
            DogBreedDTO(
                breed = "dummyBreed3",
                subBreed = arrayOf(),
                image = null
            )
        )

        val result1 = runBlocking { service.getBreedsSubBreeds("dummyBreed2") }
        val result2 = runBlocking { service.getBreedsSubBreeds("dummyBreed3") }

        Assertions.assertTrue(
            result1.last() == expectedResult[0].subBreed.joinToString()
        )
        Assertions.assertTrue(
            result2.last() == expectedResult[1].subBreed.joinToString()
        )
    }

    @Test
    fun testGetBreedPicture() {
        val expectedImage = byteArrayOf(1, 2, 3, 4, 5)

        val resultImage = runBlocking { service.getBreedPicture("dummyBreed2") }
        val resultNull = runBlocking { service.getBreedPicture("dummyBreed3") }

        Assertions.assertTrue(
            resultImage.body contentEquals expectedImage
        )
        Assertions.assertTrue(
            resultNull.body contentEquals expectedImage
        )
    }


    infix fun Array<String>.stringArrayContentEquals(other: Array<String>): Boolean {
        this.zip(other).forEach { pair ->
            if (pair.first != pair.second) return false
        }
        return true
    }
}