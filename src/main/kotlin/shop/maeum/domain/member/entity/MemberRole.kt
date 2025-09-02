package shop.maeum.domain.member.entity

enum class MemberRole (val value: String, val description: String) {
    Member("MEMBER","서비스 이용자"),
    Admin("ADMIN","서비스 관리자")
}