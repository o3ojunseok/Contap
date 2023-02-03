package com.project.contap.common.util;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QTimeStamped is a Querydsl query type for TimeStamped
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QTimeStamped extends EntityPathBase<TimeStamped> {

    private static final long serialVersionUID = -1044745183L;

    public static final QTimeStamped timeStamped = new QTimeStamped("timeStamped");

    public final DateTimePath<java.time.LocalDateTime> insertDt = createDateTime("insertDt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedDt = createDateTime("modifiedDt", java.time.LocalDateTime.class);

    public QTimeStamped(String variable) {
        super(TimeStamped.class, forVariable(variable));
    }

    public QTimeStamped(Path<? extends TimeStamped> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTimeStamped(PathMetadata metadata) {
        super(TimeStamped.class, metadata);
    }

}

