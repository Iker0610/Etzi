import das.losaparecidos.etzi.model.entities.Subject
import das.losaparecidos.etzi.model.entities.SubjectCall
import das.losaparecidos.etzi.model.entities.SubjectCallAttendance
import das.losaparecidos.etzi.model.entities.SubjectEnrollment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime


val subjectEnrollments = mutableListOf(

    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Minería de datos",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Optativa",
            6,
            4
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(

                    SubjectCallAttendance( grade = "4", distinction = false, provisional = false)
                )

            ),
            SubjectCall(
                callType = "Extraordinaria",
                examDate = LocalDateTime(2022, 5, 15, 15, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "5", distinction = false, provisional = true)
                )

            )

        )
    ),

    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Administración de Sistemas",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            4
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "10", distinction = true, provisional = false)
                )

            )

        )
    ),



    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Técnicas de Inteligencia Artificial",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            4
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "8", distinction = false, provisional = true)
                )

            )

        )
    ),

    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Álgebra",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            1
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2023, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf()

            )

        )
    ),



    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Desarrollo Avanzado de Sofware",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            4
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "5", distinction = false, provisional = false)
                )

            )

        )
    ),



    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Desarrollo de Aplicaciones Web Enriquecidas",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            4
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "3", distinction = false, provisional = false)
                )

            )

        )
    ),



    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Diseño de bases de datos",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            3
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "10", distinction = true, provisional = false)
                )

            )

        )
    ),

    SubjectEnrollment(

        // Asignatura
        subject = Subject(
            "Cálculo",
            LocalDate(2021, 9, 1),
            "Ingeniería Informática de Gestión y Sistemas de Información",
            "Obligatoria",
            6,
            1
        ),

        // Subgrupo del alumno
        subgroup = 1,

        // Convocatorias (matriculaciones)
        subjectCalls = listOf(


            SubjectCall(
                callType = "Ordinaria",
                examDate = LocalDateTime(2022, 1, 15, 11, 0),

                // Lista de un elemento siempre
                subjectCallAttendances = listOf(
                    SubjectCallAttendance( grade = "6", distinction = true, provisional = false)
                )

            )

        )
    ),
)