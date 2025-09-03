package shop.maeum.domain.friend.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import shop.maeum.domain.friend.domain.Friend
import shop.maeum.domain.friend.domain.FriendStatus
import shop.maeum.domain.member.entity.Member

interface FriendRepository : JpaRepository<Friend, Long> {
    fun findByFromMemberAndToMember(from: Member, to: Member): Friend?
    fun findAllByFromMemberAndFriendStatus(from: Member, status: FriendStatus): List<Friend>
    fun findAllByToMemberAndFriendStatus(to: Member, status: FriendStatus): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    WHERE (f.fromMember.id = :memberId OR f.toMember.id = :memberId)
      AND f.friendStatus = 'ACCEPTED'
      AND (:cursor IS NULL OR f.id < :cursor)
    ORDER BY f.id DESC
""")
    fun findAllAcceptedFriendsWithCursor(
        @Param("memberId") memberId: String,
        @Param("cursor") cursor: Long?,
        pageable: org.springframework.data.domain.Pageable
    ): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    WHERE f.toMember = :member
      AND f.friendStatus = :status
      AND (:cursor IS NULL OR f.id < :cursor)
    ORDER BY f.id DESC
""")
    fun findReceivedRequestsWithCursor(
        @Param("member") member: Member,
        @Param("status") status: FriendStatus,
        @Param("cursor") cursor: Long?,
        pageable: org.springframework.data.domain.Pageable
    ): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    WHERE f.fromMember = :member
      AND f.friendStatus = :status
      AND (:cursor IS NULL OR f.id < :cursor)
    ORDER BY f.id DESC
""")
    fun findSentRequestsWithCursor(
        @Param("member") member: Member,
        @Param("status") status: FriendStatus,
        @Param("cursor") cursor: Long?,
        pageable: org.springframework.data.domain.Pageable
    ): List<Friend>
}

