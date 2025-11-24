package com.kspo.carefit.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExerciseSchedule is a Querydsl query type for ExerciseSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExerciseSchedule extends EntityPathBase<ExerciseSchedule> {

    private static final long serialVersionUID = -1877894042L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExerciseSchedule exerciseSchedule = new QExerciseSchedule("exerciseSchedule");

    public final com.kspo.carefit.base.jpa.QBaseEntity _super = new com.kspo.carefit.base.jpa.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> durationMinutes = createNumber("durationMinutes", Integer.class);

    public final StringPath exerciseName = createString("exerciseName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final StringPath notes = createString("notes");

    public final DatePath<java.time.LocalDate> scheduledDate = createDate("scheduledDate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> scheduledTime = createTime("scheduledTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.kspo.carefit.damain.user.entity.QUser user;

    public QExerciseSchedule(String variable) {
        this(ExerciseSchedule.class, forVariable(variable), INITS);
    }

    public QExerciseSchedule(Path<? extends ExerciseSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExerciseSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExerciseSchedule(PathMetadata metadata, PathInits inits) {
        this(ExerciseSchedule.class, metadata, inits);
    }

    public QExerciseSchedule(Class<? extends ExerciseSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.kspo.carefit.damain.user.entity.QUser(forProperty("user")) : null;
    }

}

