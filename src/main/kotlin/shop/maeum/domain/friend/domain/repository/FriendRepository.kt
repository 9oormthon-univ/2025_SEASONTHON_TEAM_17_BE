package shop.maeum.domain.friend.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import shop.maeum.domain.friend.domain.Friend
import shop.maeum.domain.friend.domain.FriendStatus
import shop.maeum.domain.member.entity.Member

interface FriendRepository : JpaRepository<Friend, Long> {
    fun findByFromMemberAndToMember(from: Member, to: Member): Friend?
    fun findAllByFromMemberAndFriendStatus(from: Member, status: FriendStatus): List<Friend>
    fun findAllByToMemberAndFriendStatus(to: Member, status: FriendStatus): List<Friend>

    @Query("""
    SELECT f FROM Friend f
    WHERE f.friendStatus = :status
      AND (f.fromMember = :member OR f.toMember = :member)
""")
    fun findAllAcceptedFriends(member: Member, status: FriendStatus = FriendStatus.ACCEPTED): List<Friend>

}

