package pt.ipt.finalproject.models
// where i save the strings
data class Moment(
    val id: String,
    val ImgUri: String,
    val description: String,
    val date: String,
    val location: String
    //val userId: String
)

data class Emotions(
    val id: String,
    val type: String,
    val name: String,
    val date: String,
)