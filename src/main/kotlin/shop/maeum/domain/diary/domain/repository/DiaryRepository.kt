package shop.maeum.domain.diary.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import shop.maeum.domain.diary.domain.Diary

interface DiaryRepository : JpaRepository<Diary, Long> {
    // fun findAllByMemberIdOrderByCreatedAtDesc(memberId: Long): List<Diary>
}