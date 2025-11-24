package com.kspo.carefit.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExercise is a Querydsl query type for Exercise
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExercise extends EntityPathBase<Exercise> {

    private static final long serialVersionUID = -1039350609L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExercise exercise = new QExercise("exercise");

    public final com.kspo.carefit.base.jpa.QBaseEntity _super = new com.kspo.carefit.base.jpa.QBaseEntity(this);

    public final EnumPath<com.kspo.carefit.domain.exercise.enums.ConditionType> conditionStatus = createEnum("conditionStatus", com.kspo.carefit.domain.exercise.enums.ConditionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> durationMinutes = createNumber("durationMinutes", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final DatePath<java.time.LocalDate> exerciseDate = createDate("exerciseDate", java.time.LocalDate.class);

    public final StringPath exerciseName = createString("exerciseName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isFromSchedule = createBoolean("isFromSchedule");

    public final BooleanPath isRecommended = createBoolean("isRecommended");

    public final StringPath notes = createString("notes");

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.kspo.carefit.damain.user.entity.QUser user;

    public QExercise(String variable) {
        this(Exercise.class, forVariable(variable), INITS);
    }

    public QExercise(Path<? extends Exercise> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExercise(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExercise(PathMetadata metadata, PathInits inits) {
        this(Exercise.class, metadata, inits);
    }

    public QExercise(Class<? extends Exercise> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.kspo.carefit.damain.user.entity.QUser(forProperty("user")) : null;
    }

}

