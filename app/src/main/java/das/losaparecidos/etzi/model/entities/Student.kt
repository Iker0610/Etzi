package das.losaparecidos.etzi.model.entities

data class Student(
    val ldap: String,
    val password: String,
    val name: String,
    val surname: String,
    val email: String,
    val enrolled_degree: String,
    val profile_image: String
)
