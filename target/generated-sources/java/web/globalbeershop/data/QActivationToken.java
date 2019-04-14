package web.globalbeershop.data;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActivationToken is a Querydsl query type for ActivationToken
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QActivationToken extends EntityPathBase<ActivationToken> {

    private static final long serialVersionUID = 1142441866L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActivationToken activationToken = new QActivationToken("activationToken");

    public final DateTimePath<java.util.Date> expiryDate = createDateTime("expiryDate", java.util.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath token = createString("token");

    public final QUser user;

    public QActivationToken(String variable) {
        this(ActivationToken.class, forVariable(variable), INITS);
    }

    public QActivationToken(Path<? extends ActivationToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActivationToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActivationToken(PathMetadata metadata, PathInits inits) {
        this(ActivationToken.class, metadata, inits);
    }

    public QActivationToken(Class<? extends ActivationToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

