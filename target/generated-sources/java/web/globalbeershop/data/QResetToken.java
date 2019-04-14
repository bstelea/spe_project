package web.globalbeershop.data;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QResetToken is a Querydsl query type for ResetToken
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QResetToken extends EntityPathBase<ResetToken> {

    private static final long serialVersionUID = 273292419L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QResetToken resetToken = new QResetToken("resetToken");

    public final DateTimePath<java.util.Date> expiryDate = createDateTime("expiryDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath token = createString("token");

    public final QUser user;

    public QResetToken(String variable) {
        this(ResetToken.class, forVariable(variable), INITS);
    }

    public QResetToken(Path<? extends ResetToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QResetToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QResetToken(PathMetadata metadata, PathInits inits) {
        this(ResetToken.class, metadata, inits);
    }

    public QResetToken(Class<? extends ResetToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

