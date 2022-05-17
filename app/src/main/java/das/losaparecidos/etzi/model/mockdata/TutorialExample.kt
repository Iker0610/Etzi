package das.losaparecidos.etzi.model.mockdata

import das.losaparecidos.etzi.model.entities.Building
import das.losaparecidos.etzi.model.entities.LectureRoom
import das.losaparecidos.etzi.model.entities.Professor
import das.losaparecidos.etzi.model.entities.Tutorial
import java.time.LocalDateTime

val tutorials = mutableListOf(
    Tutorial(
        professor = Professor("Iker", "Sobrón", "iker.sobron@ehu.eus"),
        lectureRoom = LectureRoom(
            number = 25,
            floor = 3,
            building = Building("id", "I", "EIB/BIE II - I", "address"),
        ),
        startDate = LocalDateTime.of(2022, 5, 17, 17, 0),
        endDate =LocalDateTime.of(2022, 5, 17, 19, 0)
    ),
    Tutorial(
        professor = Professor("Iker", "Sobrón", "iker.sobron@ehu.eus"),
        lectureRoom = LectureRoom(
            number = 25,
            floor = 3,
            building = Building("id", "I", "EIB/BIE II - I", "address"),
        ),
        startDate = LocalDateTime.of(2022, 5, 19, 12, 0),
        endDate =LocalDateTime.of(2022, 5, 19, 14, 0)
    ),
    Tutorial(
        professor = Professor("Alicia", "Perez", "alicia.perez@ehu.eus"),
        lectureRoom = LectureRoom(
            number = 30,
            floor = 3,
            building = Building("id", "I", "EIB/BIE II - I", "address"),
        ),
        startDate = LocalDateTime.of(2022, 5, 17, 17, 0),
        endDate =LocalDateTime.of(2022, 5, 17, 19, 0)
    ),
    Tutorial(
        professor = Professor("Alicia", "Perez", "alicia.perez@ehu.eus"),
        lectureRoom = LectureRoom(
            number = 30,
            floor = 3,
            building = Building("id", "I", "EIB/BIE II - I", "address"),
        ),
        startDate = LocalDateTime.of(2022, 5, 20, 11, 0),
        endDate =LocalDateTime.of(2022, 5, 20, 13, 0)
    )
)