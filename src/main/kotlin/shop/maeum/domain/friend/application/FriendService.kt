package shop.maeum.domain.friend.application

import jakarta.validation.constraints.Email
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import shop.maeum.domain.friend.api.dto.response.FriendSimpleResDto
import shop.maeum.domain.friend.domain.Friend
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
    private val securityUtil: SecurityUtil
) {

    @Transactional
    fun requestFriend(toMemberEmail: String) {
        val fromMember = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("fromMember with id ${securityUtil.getCurrentEmail()} not found")
        val toMember = memberRepository.findByEmail(toMemberEmail)
            ?: throw IllegalArgumentException("toMember with id $toMemberEmail not found")

        if (fromMember.id == toMember.id) throw FriendRequestInvalidException("자기 자신에게는 친구 요청할 수 없습니다.")


        val existing = friendRepository.findByFromMemberAndToMember(fromMember, toMember)
        if (existing != null) throw FriendAlreadyExistsException("이미 친구 요청을 보냈습니다.")

        val request = Friend(
            fromMember = fromMember,
            toMember = toMember,
            friendStatus = FriendStatus.REQUESTED
        )
        friendRepository.save(request)
    }

    @Transactional
    fun acceptFriend(requestMemberEmail: String) {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val requestMember = memberRepository.findByEmail(requestMemberEmail)
            ?: throw IllegalArgumentException("Member with id $requestMemberEmail not found")

        val request = friendRepository.findByFromMemberAndToMember(requestMember, member)
            ?: throw FriendNotFoundException("친구 요청이 존재하지 않습니다.")

        if (request.friendStatus != FriendStatus.REQUESTED)
            throw FriendAccessDeniedException("친구 요청중인 상태가 아닙니다.")

        request.friendStatus = FriendStatus.ACCEPTED
    }

    @Transactional
    fun rejectFriend(requestMemberEmail: String) {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val requestMember = memberRepository.findByEmail(requestMemberEmail)
            ?: throw IllegalArgumentException("Member with id $requestMemberEmail not found")

        val request = friendRepository.findByFromMemberAndToMember(requestMember, member)
            ?: throw FriendNotFoundException("친구 요청이 존재하지 않습니다.")

        request.friendStatus = FriendStatus.REJECTED
    }

    @Transactional
    fun cancelFriendRequest(toMemberEmail: String) {
        val fromMember = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")
        val toMember = memberRepository.findByEmail(toMemberEmail)
            ?: throw IllegalArgumentException("Member with id $toMemberEmail not found")

        val request = friendRepository.findByFromMemberAndToMember(fromMember, toMember)
            ?: throw FriendNotFoundException("친구 요청이 존재하지 않습니다.")

        if (request.friendStatus != FriendStatus.REQUESTED) {
            throw FriendAccessDeniedException("요청 상태가 아니므로 취소할 수 없습니다.")
        }

        friendRepository.delete(request)
    }

    fun getFriends(cursor: Long?, limit: Int = 5): CursorPageResDto<FriendSimpleResDto> {
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

    fun getReceivedFriendRequests(cursor: Long?, limit: Int = 5): CursorPageResDto<FriendSimpleResDto> {
        val member = memberRepository.findByEmail(securityUtil.getCurrentEmail())
            ?: throw IllegalArgumentException("Member with id ${securityUtil.getCurrentEmail()} not found")

        val requests = friendRepository.findReceivedRequestsWithCursor(
            member = member,
            status = FriendStatus.REQUESTED,
            cursor = cursor,
            pageable = PageRequest.of(0, limit + 1)
        )

        val sliced = requests.take(limit)
        val hasNext = requests.size > limit
        val nextCursor = if (hasNext) requests[limit - 1].id else null

        return CursorPageResDto(
            data = sliced.map { FriendSimpleResDto.of(it.fromMember) },
            nextCursor = nextCursor,
            hasNext = hasNext
        )
    }

    fun getSentFriendRequests(cursor: Long?, limit: Int = 5): CursorPageResDto<FriendSimpleResDto> {
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

//    @Transactional
//    fun searchFriend(
//        memberId: String,
//        email: String?,
//        nickname: String?
//    ): List<FriendSearchResDto> {
//        val me = getMemberOrThrow(memberId)
//
//        val candidates = memberRepository.searchByEmailOrNickname(email, nickname)
//            .filter { it.id != memberId }
//
//        return candidates.map {
//            val sent = friendRepository.findByFromMemberAndToMember(me, it)
//            val received = friendRepository.findByFromMemberAndToMember(it, me)
//
//            FriendSearchResDto(
//                memberId = it.id,
//                email = it.email,
//                nickname = it.nickname,
//                profileImageUrl = it.profileImageUrl,
//                isFriend = (sent?.status == FriendStatus.ACCEPTED || received?.status == FriendStatus.ACCEPTED),
//                isRequested = sent?.status == FriendStatus.REQUESTED
//            )
//        }
//    }
//
//    private fun getMemberOrThrow(id: String): Member =
//        memberRepository.findById(id).orElseThrow { NoSuchElementException("회원을 찾을 수 없습니다.") }
//
//    private fun Member.toSimpleDto(): FriendSimpleResDto = FriendSimpleResDto(
//        memberId = this.id!!,
//        nickname = this.nickname,
//        email = this.email,
//        profileImageUrl = this.profilePath!!
//    )
}
