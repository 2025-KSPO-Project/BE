package com.kspo.carefit.domain.exercise.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExerciseRecommendation is a Querydsl query type for ExerciseRecommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExerciseRecommendation extends EntityPathBase<ExerciseRecommendation> {

    private static final long serialVersionUID = 777679432L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExerciseRecommendation exerciseRecommendation = new QExerciseRecommendation("exerciseRecommendation");

    public final com.kspo.carefit.base.jpa.QBaseEntity _super = new com.kspo.carefit.base.jpa.QBaseEntity(this);

    public final EnumPath<com.kspo.carefit.domain.exercise.enums.ConditionType> conditionType = createEnum("conditionType", com.kspo.carefit.domain.exercise.enums.ConditionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath exerciseName = createString("exerciseName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAccepted = createBoolean("isAccepted");

    public final StringPath llmPrompt = createString("llmPrompt");

    public final StringPath llmResponse = createString("llmResponse");

    public final DatePath<java.time.LocalDate> recommendationDate = createDate("recommendationDate", java.time.LocalDate.class);

    public final StringPath sportName = createString("sportName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.kspo.carefit.damain.user.entity.QUser user;

    public QExerciseRecommendation(String variable) {
        this(ExerciseRecommendation.class, forVariable(variable), INITS);
    }

    public QExerciseRecommendation(Path<? extends ExerciseRecommendation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExerciseRecommendation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExerciseRecommendation(PathMetadata metadata, PathInits inits) {
        this(ExerciseRecommendation.class, metadata, inits);
    }

    public QExerciseRecommendation(Class<? extends ExerciseRecommendation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.kspo.carefit.damain.user.entity.QUser(forProperty("user")) : null;
    }

}

