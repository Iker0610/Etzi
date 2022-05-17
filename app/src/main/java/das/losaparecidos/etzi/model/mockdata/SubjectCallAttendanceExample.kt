import das.losaparecidos.etzi.model.entities.Subject
import das.losaparecidos.etzi.model.entities.SubjectCall
import das.losaparecidos.etzi.model.entities.SubjectCallAttendance
import java.time.LocalDate
import java.time.LocalDateTime

val subtectsCallAttendance = mutableListOf(

    // APROBADA
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Minería de datos",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,1,15,18,0)

        ),
        grade = "10",
        distinction = true
    ),

    // SIN EVALUAR
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo Avanzado de Software",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,30,15,0)

        ),
        grade = "",
        distinction = false
    ),

    // APROBADA
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Técnicas de Inteligencia Artificial",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,1,15,15,0)

        ),
        grade = "9",
        distinction = false
    ),

    // SIN EVALUAR
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "",
        distinction = false
    ),

    // PENDIENTE
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,6,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "",
        distinction = false
    ),

    // PENDIENTE
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "3",
        distinction = false
    ),

    // APROBADA
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "5",
        distinction = false
    ),

    // PENDIENTE
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,9,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "",
        distinction = false
    ),

    // APROBADA
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "8",
        distinction = false
    ),

    // SIN EVALUAR
    SubjectCallAttendance(
        SubjectCall(
            subject = Subject(
                "Desarrollo de Aplicaciones Web Enriquecidas",
                LocalDate.of(2021, 9, 1),
                "Ingeniería Informática de Gestión y Sistemas de Información",
                "Optativa",
                6,
                4
            ),
            academicYear = LocalDate.of(2022,1,1),
            degree = "Ingeniería Informática de Gestión y Sistemas de Información",
            callType = "",
            examDate = LocalDateTime.of(2022,5,15,15,0)

        ),
        grade = "",
        distinction = false
    ),

)