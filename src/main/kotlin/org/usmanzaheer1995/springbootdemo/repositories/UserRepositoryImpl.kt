package org.usmanzaheer1995.springbootdemo.repositories

import org.jooq.DSLContext
import org.jooq.generated.persistence.db.tables.references.USERS
import org.springframework.stereotype.Repository
import org.usmanzaheer1995.springbootdemo.models.RegisterUserDto
import org.usmanzaheer1995.springbootdemo.models.User

@Repository
class UserRepositoryImpl(
    private val dslContext: DSLContext,
) : UserRepository {
    override fun findByEmail(email: String): User? {
        val usersContext = dslContext.selectFrom(USERS)
        val user = usersContext.where(USERS.EMAIL.eq(email)).fetchOne()

        return user?.let { User.fromUserRecord(it) }
    }

    override fun save(registerUserDto: RegisterUserDto): User {
        val user = dslContext.newRecord(USERS)
        user.fullName = registerUserDto.fullName
        user.email = registerUserDto.email
        user.password = registerUserDto.password
        user.userRole = registerUserDto.role.name
        user.createdAt = registerUserDto.createdAt
        user.updatedAt = registerUserDto.updatedAt
        user.store()
        return User.fromUserRecord(user)
    }

    override fun findAll(): List<User> {
        val usersContext = dslContext.selectFrom(USERS)
        val users = usersContext.fetch()

        return users.map { User.fromUserRecord(it) }
    }
}
