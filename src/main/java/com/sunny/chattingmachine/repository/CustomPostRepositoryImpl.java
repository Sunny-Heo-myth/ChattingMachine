package com.sunny.chattingmachine.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunny.chattingmachine.domain.Post;
import com.sunny.chattingmachine.domain.PostSearchCondition;
import com.sunny.chattingmachine.domain.QAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sunny.chattingmachine.domain.QAccount.account;
import static com.sunny.chattingmachine.domain.QPost.post;

@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    public CustomPostRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Post> search(PostSearchCondition postSearchCondition, Pageable pageable) {
        List<Post> content = queryFactory.selectFrom(post)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle())
                )
                .leftJoin(post.writer, account)

                .fetchJoin()
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory.selectFrom(post)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle())
                );

        return PageableExecutionUtils.getPage(content, pageable, () ->countQuery.fetch().size());
    }

    private BooleanExpression contentHasStr(String content) {
        return StringUtils.hasLength(content) ? post.content.contains(content) : null;
    }

    private BooleanExpression titleHasStr(String title) {
        return StringUtils.hasLength(title) ? post.title.contains(title) : null;
    }
}
