package das.losaparecidos.etzi.model.mockdata

import das.losaparecidos.etzi.model.entities.Building
import das.losaparecidos.etzi.model.entities.Lecture
import das.losaparecidos.etzi.model.entities.LectureEntity
import das.losaparecidos.etzi.model.entities.Professor

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

val lectures = mutableListOf(

    Lecture(
        lecture = LectureEntity(
            subjectName = "Desarrollo Avanzado de Software",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = 16,
            professorEmail = "iker@ehu.eus",
            roomNumber = 10,
            roomFloor = 3,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 16, 15, 0),
            endDate = LocalDateTime(2022, 5, 16, 17, 0)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Iker", "Sobrón", "iker.sobron@ehu.eus")

    ),
    Lecture(
        lecture = LectureEntity(
            subjectName = "Desarrollo de Aplicaciones Web Enriquecidas",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = -1,
            professorEmail = "ainhoa@ehu.eus",
            roomNumber = 7,
            roomFloor = 7,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 16, 17, 0),
            endDate = LocalDateTime(2022, 5, 16, 19, 0)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Ainhoa", "Yera", "ainhoa.yera@ehu.eus")

    ),
    Lecture(
        lecture = LectureEntity(
            subjectName = "Minería de Datos",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = 16,
            professorEmail = "alicia@ehu.eus",
            roomNumber = 10,
            roomFloor = 3,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 17, 18, 0),
            endDate = LocalDateTime(2022, 5, 17, 20, 0)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Alicia", "Pérez", "alicia.perez@ehu.eus")
    ),
    Lecture(
        lecture = LectureEntity(
            subjectName = "Técnicas de Inteligencia Artificial",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = -1,
            professorEmail = "aitziber@ehu.eus",
            roomNumber = 5,
            roomFloor = 6,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 18, 15, 0),
            endDate = LocalDateTime(2022, 5, 18, 17, 0)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Aitziber", "Atutxa", "aitziber.atutxa@ehu.eus")
    ),
    Lecture(
        lecture = LectureEntity(
            subjectName = "Administración de Sistemas",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = 16,
            professorEmail = "unai@ehu.eus",
            roomNumber = 7,
            roomFloor = 8,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 18, 17, 0),
            endDate = LocalDateTime(2022, 5, 18, 19, 0)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Unai", "Lopez", "unai.lopez@ehu.eus")
    ),
    Lecture(
        lecture = LectureEntity(
            subjectName = "Cálculo",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = 1,
            professorEmail = "alguien@ehu.eus",
            roomNumber = 4,
            roomFloor = 3,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 18, 19, 0),
            endDate = LocalDateTime(2022, 5, 18, 21, 0)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Alguien", "Alguien", "alguien.alguien@ehu.eus")
    ),
    Lecture(
        lecture = LectureEntity(
            subjectName = "Álgebra",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = -1,
            professorEmail = "alguien@ehu.eus",
            roomNumber = 5,
            roomFloor = 6,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 19, 17, 30),
            endDate = LocalDateTime(2022, 5, 19, 19, 30)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("Alguien", "Alguien", "alguien.alguien@ehu.eus")
    ),

    Lecture(
        lecture = LectureEntity(
            subjectName = "Métodos Estadísticos de la Ingeniería",
            academicYear = LocalDate(2022, 1, 1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            subgroup = 31,
            professorEmail = "porres@ehu.eus",
            roomNumber = 11,
            roomFloor = 2,
            roomBuilding = "i",
            startDate = LocalDateTime(2022, 5, 20, 17, 0),
            endDate = LocalDateTime(2022, 5, 20, 18, 30)
        ),
        building = Building("id", "I", "EIB/BIE II - I", "adress"),
        professor = Professor("JM", "porres", "porres@ehu.eus")
    )

)