package xyz.eatsteak.kusaint.model

data class LectureData(
    val syllabus: String, // 계획
    val lectureTypeMajor: String, // 이수구분(주전공)
    val lectureTypeMultiMajor: String, // 이수구분(다전공)
    val engineeringCert: String, // 공학인증
    val category: String, // 교과영역
    val lectureId: String, // 과목번호
    val lectureName: String, // 과목명
    val group: String, // 분반
    val professor: String, // 교수명
    val department: String, // 개설학과
    val periodAndGrade: String, // 시간/학점(설계)
    val participantsNum: String, // 수강인원
    val seatLeft: String, // 여석
    val lectureTime: String // 강의시간(강의실)
)
