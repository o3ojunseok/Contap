package com.project.contap.model.hashtag;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QHashTag is a Querydsl query type for HashTag
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QHashTag extends EntityPathBase<HashTag> {

    private static final long serialVersionUID = -326015398L;

    public static final QHashTag hashTag = new QHashTag("hashTag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public QHashTag(String variable) {
        super(HashTag.class, forVariable(variable));
    }

    public QHashTag(Path<? extends HashTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHashTag(PathMetadata metadata) {
        super(HashTag.class, metadata);
    }

}

