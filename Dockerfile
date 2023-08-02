FROM openjdk:17

WORKDIR /app
ADD build/libs/DogBreedAPI.jar /app/DogBreedAPI.jar

ENTRYPOINT ["java", "-jar", "/app/DogBreedAPI.jar"]
