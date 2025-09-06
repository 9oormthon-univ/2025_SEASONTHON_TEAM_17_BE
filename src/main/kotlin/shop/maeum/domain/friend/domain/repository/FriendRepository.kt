package shop.maeum.domain.friend.domain.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import shop.maeum.domain.friend.domain.Friend
import shop.maeum.domain.friend.domain.FriendStatus
import shop.maeum.domain.member.entity.Member

interface FriendRepository : JpaRepository<Friend, Long> {
    fun findByFromMemberAndToMember(from: Member, to: Member): Friend?

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
        pageable: Pageable
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
        pageable: Pageable
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
        pageable: Pageable
    ): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    JOIN f.fromMember fm
    JOIN f.toMember tm
    WHERE (fm.id = :memberId OR tm.id = :memberId)
      AND (
        (CASE WHEN fm.id = :memberId THEN tm.nickname ELSE fm.nickname END) LIKE %:keyword%
        OR (CASE WHEN fm.id = :memberId THEN tm.email ELSE fm.email END) LIKE %:keyword%
      )
      AND (:cursor IS NULL OR f.id > :cursor)
    ORDER BY f.id ASC
""")
    fun searchFriendsWithCursor(
        @Param("memberId") memberId: String,
        @Param("keyword") keyword: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    JOIN f.fromMember fm
    JOIN f.toMember tm
    WHERE f.friendStatus = 'ACCEPTED'
      AND (fm.id = :memberId OR tm.id = :memberId)
      AND (
        (CASE WHEN fm.id = :memberId THEN tm.nickname ELSE fm.nickname END) LIKE %:keyword%
        OR (CASE WHEN fm.id = :memberId THEN tm.email ELSE fm.email END) LIKE %:keyword%
      )
      AND (:cursor IS NULL OR f.id > :cursor)
    ORDER BY f.id ASC
""")
    fun searchMyFriendsWithCursor(
        @Param("memberId") memberId: String,
        @Param("keyword") keyword: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    JOIN f.fromMember fm
    JOIN f.toMember tm
    WHERE f.friendStatus = 'REQUESTED'
      AND fm.id = :memberId
      AND (
        (CASE WHEN fm.id = :memberId THEN tm.nickname ELSE fm.nickname END) LIKE %:keyword%
        OR (CASE WHEN fm.id = :memberId THEN tm.email ELSE fm.email END) LIKE %:keyword%
      )
      AND (:cursor IS NULL OR f.id > :cursor)
    ORDER BY f.id ASC
""")
    fun searchSentFriendRequestsWithCursor(
        @Param("memberId") memberId: String,
        @Param("keyword") keyword: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Friend>

    @Query("""
    SELECT f
    FROM Friend f
    JOIN f.fromMember fm
    JOIN f.toMember tm
    WHERE f.friendStatus = 'REQUESTED'
      AND tm.id = :memberId
      AND (
        (CASE WHEN fm.id = :memberId THEN tm.nickname ELSE fm.nickname END) LIKE %:keyword%
        OR (CASE WHEN fm.id = :memberId THEN tm.email ELSE fm.email END) LIKE %:keyword%
      )
      AND (:cursor IS NULL OR f.id > :cursor)
    ORDER BY f.id ASC
""")
    fun searchReceivedFriendRequestsWithCursor(
        @Param("memberId") memberId: String,
        @Param("keyword") keyword: String,
        @Param("cursor") cursor: Long?,
        pageable: Pageable
    ): List<Friend>
}

