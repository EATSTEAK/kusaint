package xyz.eatsteak.kusaint.model

data class ElectiveData(
    val electiveType: ElectiveType,
    val electiveCategories: Collection<String>
)

enum class ElectiveType {
    REQUIRED, // 교양필수
    OPTIONAL // 교양선택
}
