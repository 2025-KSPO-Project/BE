package com.kspo.carefit.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QConditionCheck is a Querydsl query type for ConditionCheck
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConditionCheck extends EntityPathBase<ConditionCheck> {

    private static final long serialVersionUID = 1285850692L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QConditionCheck conditionCheck = new QConditionCheck("conditionCheck");

    public final com.kspo.carefit.base.jpa.QBaseEntity _super = new com.kspo.carefit.base.jpa.QBaseEntity(this);

    public final DatePath<java.time.LocalDate> checkDate = createDate("checkDate", java.time.LocalDate.class);

    public final EnumPath<com.kspo.carefit.domain.exercise.enums.ConditionType> conditionType = createEnum("conditionType", com.kspo.carefit.domain.exercise.enums.ConditionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.kspo.carefit.damain.user.entity.QUser user;

    public QConditionCheck(String variable) {
        this(ConditionCheck.class, forVariable(variable), INITS);
    }

    public QConditionCheck(Path<? extends ConditionCheck> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QConditionCheck(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QConditionCheck(PathMetadata metadata, PathInits inits) {
        this(ConditionCheck.class, metadata, inits);
    }

    public QConditionCheck(Class<? extends ConditionCheck> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.kspo.carefit.damain.user.entity.QUser(forProperty("user")) : null;
    }

}

