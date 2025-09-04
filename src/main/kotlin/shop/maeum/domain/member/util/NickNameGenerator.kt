package shop.maeum.domain.member.util

object NickNameGenerator {

    private val adjectives = listOf(
        "용감한", "지혜로운", "재치있는", "날쌘", "위대한",
        "온화한", "사나운", "고귀한", "충직한", "대담한",
        "정직한", "까다로운", "활발한", "위풍당당한", "엉뚱한"
    )

    private val nouns = listOf(
        "사자", "독수리", "늑대", "호랑이", "곰",
        "여우", "부엉이", "토끼", "용", "팬더",
        "스컹크", "돌고래", "고양이", "강아지", "펭귄"
    )

    fun generateNickName(): String {
        val adjective = adjectives.random()
        val noun = nouns.random()
        val number = (1..100).random()
        return "$adjective$noun$number"
    }
}