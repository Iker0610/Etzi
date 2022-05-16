import das.losaparecidos.etzi.model.entities.LectureEntity

import java.time.LocalDate
import java.time.LocalDateTime

val lectures = mutableListOf(

    LectureEntity(
        subjectName = "Desarrollo Avanzado de Software",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 16,
        professorEmail = "iker@ehu.eus",
        roomNumber = 10,
        roomFloor = 3,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 16, 15, 0),
        endDate = LocalDateTime.of(2022, 5, 16, 17, 0)
    ),
    LectureEntity(
        subjectName = "Desarrollo de Aplicaciones Web Enriquecidas",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 16,
        professorEmail = "ainhoa@ehu.eus",
        roomNumber = 7,
        roomFloor = 7,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 16, 17, 0),
        endDate = LocalDateTime.of(2022, 5, 16, 19, 0)
    ),
    LectureEntity(
        subjectName = "Minería de Datos",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 16,
        professorEmail = "alicia@ehu.eus",
        roomNumber = 10,
        roomFloor = 3,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 17, 18, 0),
        endDate = LocalDateTime.of(2022, 5, 17, 20, 0)
    ),
    LectureEntity(
        subjectName = "Técnicas de Inteligencia Artificial",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 16,
        professorEmail = "aitziber@ehu.eus",
        roomNumber = 5,
        roomFloor = 6,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 18, 15, 0),
        endDate = LocalDateTime.of(2022, 5, 18, 17, 0)
    ),
    LectureEntity(
        subjectName = "Administración de Sistemas",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 16,
        professorEmail = "unai@ehu.eus",
        roomNumber = 7,
        roomFloor = 8,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 18, 17, 0),
        endDate = LocalDateTime.of(2022, 5, 18, 19, 0)
    ),
    LectureEntity(
        subjectName = "Cálculo",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 1,
        professorEmail = "alguien@ehu.eus",
        roomNumber = 4,
        roomFloor = 3,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 18, 19, 0),
        endDate = LocalDateTime.of(2022, 5, 18, 21, 0)
    ),
    LectureEntity(
        subjectName = "Álgebra",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 1,
        professorEmail = "alguien@ehu.eus",
        roomNumber = 5,
        roomFloor = 6,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 19, 17, 30),
        endDate = LocalDateTime.of(2022, 5, 19, 19, 30)
    ),

    LectureEntity(
        subjectName = "Métodos Estadísticos de la Ingeniería",
        academicYear = LocalDate.of(2022, 1, 1),
        degree = "Ingeniería Informática de Gestión y Sistemas de Información",
        subgroup = 31,
        professorEmail = "porres@ehu.eus",
        roomNumber = 11,
        roomFloor = 2,
        roomBuilding = "i",
        startDate = LocalDateTime.of(2022, 5, 20, 17, 0),
        endDate = LocalDateTime.of(2022, 5, 20, 18, 30)
    )

)