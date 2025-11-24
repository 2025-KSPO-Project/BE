package com.kspo.carefit.damain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1064471581L;

    public static final QUser user = new QUser("user");

    public final NumberPath<Integer> disabilityCode = createNumber("disabilityCode", Integer.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath role = createString("role");

    public final NumberPath<Integer> sidoCode = createNumber("sidoCode", Integer.class);

    public final NumberPath<Integer> sigunguCode = createNumber("sigunguCode", Integer.class);

    public final ListPath<Integer, NumberPath<Integer>> sportsCode = this.<Integer, NumberPath<Integer>>createList("sportsCode", Integer.class, NumberPath.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public final ListPath<com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token, com.kspo.carefit.base.security.oauth2.entity.QUserOauth2Token> userOauth2Token = this.<com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token, com.kspo.carefit.base.security.oauth2.entity.QUserOauth2Token>createList("userOauth2Token", com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token.class, com.kspo.carefit.base.security.oauth2.entity.QUserOauth2Token.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

