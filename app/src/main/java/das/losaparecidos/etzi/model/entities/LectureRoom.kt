package das.losaparecidos.etzi.model.entities

data class LectureRoom(
    val number: Int,
    val floor: Int,
    val building: Building,
){
    val fullCode get() = "P$floor${building.abbreviation}$number"
}
