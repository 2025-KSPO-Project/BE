package com.kspo.carefit.base.security.oauth2.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserOauth2Token is a Querydsl query type for UserOauth2Token
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserOauth2Token extends EntityPathBase<UserOauth2Token> {

    private static final long serialVersionUID = -1123058974L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserOauth2Token userOauth2Token = new QUserOauth2Token("userOauth2Token");

    public final StringPath accessToken = createString("accessToken");

    public final DateTimePath<java.time.Instant> accessTokenExpiresAt = createDateTime("accessTokenExpiresAt", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath provider = createString("provider");

    public final StringPath refreshToken = createString("refreshToken");

    public final DateTimePath<java.time.Instant> refreshTokenExpiresAt = createDateTime("refreshTokenExpiresAt", java.time.Instant.class);

    public final com.kspo.carefit.damain.user.entity.QUser user;

    public QUserOauth2Token(String variable) {
        this(UserOauth2Token.class, forVariable(variable), INITS);
    }

    public QUserOauth2Token(Path<? extends UserOauth2Token> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserOauth2Token(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserOauth2Token(PathMetadata metadata, PathInits inits) {
        this(UserOauth2Token.class, metadata, inits);
    }

    public QUserOauth2Token(Class<? extends UserOauth2Token> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.kspo.carefit.damain.user.entity.QUser(forProperty("user")) : null;
    }

}

