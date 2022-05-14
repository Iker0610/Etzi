package das.losaparecidos.etzi.model.entities

import android.graphics.Bitmap

data class Student(
    val ldap: String,
    val email: String,
    val name: String,
    val surname: String,
    val enrolled_degree: String,
    val profile_image: Bitmap?
)
