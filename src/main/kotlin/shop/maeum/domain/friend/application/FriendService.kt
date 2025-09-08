package shop.maeum.domain.friend.application

import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.fcm.event.friend.FriendAcceptedEvent
import shop.maeum.domain.fcm.event.friend.FriendRequestedEvent
import shop.maeum.domain.friend.api.dto.response.FriendSearchResDto
import shop.maeum.domain.friend.api.dto.response.FriendSimpleResDto
import shop.maeum.domain.friend.domain.Friend
import shop.maeum.domain.friend.domain.FriendRelationStatus
import shop.maeum.domain.friend.domain.FriendStatus
import shop.maeum.domain.friend.domain.repository.FriendRepository
import shop.maeum.domain.friend.exception.FriendAccessDeniedException
import shop.maeum.domain.friend.exception.FriendAlreadyExistsException
import shop.maeum.domain.friend.exception.FriendNotFoundException
import shop.maeum.domain.friend.exception.FriendRequestInvalidException
import shop.maeum.domain.member.repository.MemberRepository
import shop.maeum.domain.security.util.SecurityUtil
import shop.maeum.global.dto.CursorPageResDto

@Service
@Transactional(readOnly = true)
class FriendService(
    private val friendRepository: FriendRepository,
    private val memberRepository: MemberRepository,
    private val securityUtil: SecurityUtil,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun requestFriend(toMemberEmail: String) {
        val fromMember = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val toMember = memberRepository.findByEmail(toMemberEmail)
            ?: throw IllegalArgumentException("Member with id $toMemberEmail not found")

        if (fromMember.id == toMember.id)
            throw FriendRequestInvalidException("자기 자신에게는 친구 요청할 수 없습니다.")

        val existing = friendRepository.findByFromMemberAndToMember(fromMember, toMember)
        if (existing != null) {
            when (existing.friendStatus) {
                FriendStatus.REQUESTED -> throw FriendAlreadyExistsException("이미 요청을 보냈습니다.")
                FriendStatus.ACCEPTED -> throw FriendAlreadyExistsException("이미 친구입니다.")
                FriendStatus.REJECTED -> {
                    existing.friendStatus = FriendStatus.REQUESTED

                    eventPublisher.publishEvent(
                        FriendRequestedEvent(
                            fromMemberId = fromMember.id!!,
                            fromMemberNickname = fromMember.nickname,
                            toMemberId = toMember.id!!
                        )
                    )
                    return
                }
            }
        }

        val reverseExisting = friendRepository.findByFromMemberAndToMember(toMember, fromMember)
        if (reverseExisting != null) {
            when (reverseExisting.friendStatus) {
                FriendStatus.REQUESTED -> throw FriendAlreadyExistsException("상대방이 이미 요청을 보냈습니다.")
                FriendStatus.ACCEPTED -> throw FriendAlreadyExistsException("이미 친구입니다.")
                FriendStatus.REJECTED -> {
                    reverseExisting.friendStatus = FriendStatus.REQUESTED

                    eventPublisher.publishEvent(
                        FriendRequestedEvent(
                            fromMemberId = fromMember.id!!,
                            fromMemberNickname = fromMember.nickname,
                            toMemberId = toMember.id!!
                        )
                    )
                    return
                }
            }
        }

        val request = Friend(fromMember = fromMember, toMember = toMember, friendStatus = FriendStatus.REQUESTED)
        friendRepository.save(request)

        eventPublisher.publishEvent(
            FriendRequestedEvent(
                fromMemberId = fromMember.id!!,
                fromMemberNickname = fromMember.nickname,
                toMemberId = toMember.id!!
            )
        )
    }

    @Transactional
    fun acceptFriend(requestMemberEmail: String) {
        val me = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val requester = memberRepository.findByEmail(requestMemberEmail)
            ?: throw IllegalArgumentException("Member with id $requestMemberEmail not found")

        val request = friendRepository.findByFromMemberAndToMember(requester, me)
            ?: throw FriendNotFoundException("친구 요청이 존재하지 않습니다.")

        if (request.friendStatus != FriendStatus.REQUESTED) {
            throw FriendAccessDeniedException("친구 요청중인 상태가 아닙니다.")
        }

        request.friendStatus = FriendStatus.ACCEPTED

        eventPublisher.publishEvent(
            FriendAcceptedEvent(
                fromMemberId = requester.id!!,
                toMemberId = me.id!!,
                toMemberNickname = me.nickname
            )
        )
    }

    @Transactional
    fun rejectFriend(requestMemberEmail: String) {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val requestMember = memberRepository.findByEmail(requestMemberEmail)
            ?: throw IllegalArgumentException("Member with id $requestMemberEmail not found")

        val request = friendRepository.findByFromMemberAndToMember(requestMember, member)
            ?: throw FriendNotFoundException("친구 요청이 존재하지 않습니다.")

        when (request.friendStatus) {
            FriendStatus.REQUESTED -> {
                request.friendStatus = FriendStatus.REJECTED
            }
            FriendStatus.REJECTED -> {
                throw FriendAccessDeniedException("이미 거절한 요청입니다.")
            }
            FriendStatus.ACCEPTED -> {
                throw FriendAccessDeniedException("이미 수락한 요청은 거절할 수 없습니다.")
            }
        }
    }

    @Transactional
    fun cancelFriendRequest(toMemberEmail: String) {
        val fromMember = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val toMember = memberRepository.findByEmail(toMemberEmail)
            ?: throw IllegalArgumentException("Member with id $toMemberEmail not found")

        val request = friendRepository.findByFromMemberAndToMember(fromMember, toMember)
            ?: throw FriendNotFoundException("친구 요청이 존재하지 않습니다.")

        when (request.friendStatus) {
            FriendStatus.REQUESTED -> {
                friendRepository.delete(request)
            }
            FriendStatus.REJECTED -> {
                throw FriendAccessDeniedException("이미 취소된 요청입니다.")
            }
            FriendStatus.ACCEPTED -> {
                throw FriendAccessDeniedException("이미 수락된 요청은 취소할 수 없습니다.")
            }
        }
    }

    @Transactional
    fun removeFriend(friendEmail: String) {
        val me = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val friend = memberRepository.findByEmail(friendEmail)
            ?: throw IllegalArgumentException("Member with id $friendEmail not found")

        val relation = friendRepository.findByFromMemberAndToMember(me, friend)
        val reverseRelation = friendRepository.findByFromMemberAndToMember(friend, me)

        if (relation == null && reverseRelation == null) {
            throw FriendNotFoundException("친구 관계가 존재하지 않습니다.")
        }

        if ((relation?.friendStatus == FriendStatus.ACCEPTED) ||
            (reverseRelation?.friendStatus == FriendStatus.ACCEPTED)
        ) {
            relation?.let { friendRepository.delete(it) }
            reverseRelation?.let { friendRepository.delete(it) }
        } else {
            throw FriendAccessDeniedException("친구 상태가 아니므로 삭제할 수 없습니다.")
        }
    }

    fun getFriends(cursor: Long?, limit: Int = 5): CursorPageResDto<FriendSimpleResDto, Long> {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")

        val friendEntities = friendRepository.findAllAcceptedFriendsWithCursor(
            memberId = member.id!!,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val friends = friendEntities.map {
            if (it.fromMember == member) it.toMember else it.fromMember
        }

        val sliced = friends.take(limit)
        val hasNext = friendEntities.size > limit
        val nextCursor = if (hasNext) friendEntities[limit - 1].id else null

        return CursorPageResDto(
            data = sliced.map { FriendSimpleResDto.of(it) },
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun getReceivedFriendRequests(cursor: Long?, limit: Int = 5): CursorPageResDto<FriendSimpleResDto, Long> {
        val currentEmail = securityUtil.getCurrentEmail()
        println("=== DEBUG: Current email = '$currentEmail'")
        
        val member = memberRepository.findByEmail(currentEmail)
            ?: throw IllegalArgumentException("Member with id $currentEmail not found")
            
        println("=== DEBUG: Found member id = '${member.id}', email = '${member.email}'")

        val requests = friendRepository.findReceivedRequestsWithCursor(
            memberId = member.id!!,
            status = FriendStatus.REQUESTED,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )
        
        println("=== DEBUG: Found ${requests.size} requests")
        requests.forEach { 
            println("=== DEBUG: Request from '${it.fromMember.id}' to '${it.toMember.id}', status: ${it.friendStatus}")
        }

        val sliced = requests.take(limit)
        val hasNext = requests.size > limit
        val nextCursor = if (hasNext) requests[limit - 1].id else null

        return CursorPageResDto(
            data = sliced.map { FriendSimpleResDto.of(it.fromMember) },
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun getSentFriendRequests(cursor: Long?, limit: Int = 5): CursorPageResDto<FriendSimpleResDto, Long> {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")

        val requests = friendRepository.findSentRequestsWithCursor(
            member = member,
            status = FriendStatus.REQUESTED,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val sliced = requests.take(limit)
        val hasNext = requests.size > limit
        val nextCursor = if (hasNext) requests[limit - 1].id else null

        return CursorPageResDto(
            data = sliced.map { FriendSimpleResDto.of(it.toMember) },
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun searchAllMembersWithCursor(
        keyword: String,
        cursor: Long?,
        limit: Int = 5
    ): CursorPageResDto<FriendSearchResDto, String> {
        val me = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val pageable = PageRequest.of(0, limit + 1, Sort.by("id").ascending())

        val members = memberRepository.searchMembersWithCursor(
            keyword = keyword,
            currentMemberId = me.id!!,
            cursor = cursor,
            pageable = pageable
        )

        val sliced = members.take(limit)
        val hasNext = members.size > limit
        val nextCursor = if (hasNext) sliced.last().id else null

        val results = sliced.map {
            val friendRelationStatus = when {
                friendRepository.existsByFromMemberIdAndToMemberIdAndFriendStatus(
                    fromMemberId = me.id,
                    toMemberId = it.id!!,
                    friendStatus = FriendStatus.ACCEPTED
                ) || friendRepository.existsByFromMemberIdAndToMemberIdAndFriendStatus(
                    fromMemberId = it.id,
                    toMemberId = me.id,
                    friendStatus = FriendStatus.ACCEPTED
                ) -> FriendRelationStatus.FRIEND

                friendRepository.existsByFromMemberIdAndToMemberIdAndFriendStatus(
                    fromMemberId = me.id,
                    toMemberId = it.id,
                    friendStatus = FriendStatus.REQUESTED
                ) -> FriendRelationStatus.REQUESTED_BY_ME

                friendRepository.existsByFromMemberIdAndToMemberIdAndFriendStatus(
                    fromMemberId = it.id,
                    toMemberId = me.id,
                    friendStatus = FriendStatus.REQUESTED
                ) -> FriendRelationStatus.REQUESTED_BY_OTHER

                else -> FriendRelationStatus.NONE
            }

            FriendSearchResDto(
                memberId = it.id,
                email = it.email,
                nickname = it.nickname,
                profileImageUrl = it.profilePath ?: "",
                relationStatus = friendRelationStatus
            )
        }

        return CursorPageResDto(
            data = results,
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun searchMyFriendsWithCursor(
        keyword: String,
        cursor: Long?,
        limit: Int = 5
    ): CursorPageResDto<FriendSimpleResDto, Long> {
        val me = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val friends = friendRepository.searchMyFriendsWithCursor(
            memberId = me.id!!,
            keyword = keyword,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val sliced = friends.take(limit)
        val hasNext = friends.size > limit
        val nextCursor = if (hasNext) sliced.last().id else null

        val results = sliced.map { friend ->
            val target = if (friend.fromMember == me) friend.toMember else friend.fromMember
            FriendSimpleResDto.of(target)
        }

        return CursorPageResDto(
            data = results,
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun searchSentFriendRequestsWithCursor(
        keyword: String,
        cursor: Long?,
        limit: Int = 5
    ): CursorPageResDto<FriendSimpleResDto, Long> {
        val me = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val requests = friendRepository.searchSentFriendRequestsWithCursor(
            memberId = me.id!!,
            keyword = keyword,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val sliced = requests.take(limit)
        val hasNext = requests.size > limit
        val nextCursor = if (hasNext) sliced.last().id else null

        val results = sliced.map { FriendSimpleResDto.of(it.toMember) }

        return CursorPageResDto(
            data = results,
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun searchReceivedFriendRequestsWithCursor(
        keyword: String,
        cursor: Long?,
        limit: Int = 5
    ): CursorPageResDto<FriendSimpleResDto, Long> {
        val me = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with email ${securityUtil.getCurrentEmail()} not found")

        val requests = friendRepository.searchReceivedFriendRequestsWithCursor(
            memberId = me.id!!,
            keyword = keyword,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val sliced = requests.take(limit)
        val hasNext = requests.size > limit
        val nextCursor = if (hasNext) sliced.last().id else null

        val results = sliced.map { FriendSimpleResDto.of(it.fromMember) }

        return CursorPageResDto(
            data = results,
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

}
