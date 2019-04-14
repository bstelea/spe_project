package web.globalbeershop.data;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBeer is a Querydsl query type for Beer
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBeer extends EntityPathBase<Beer> {

    private static final long serialVersionUID = 471904649L;

    public static final QBeer beer = new QBeer("beer");

    public final NumberPath<Double> abv = createNumber("abv", Double.class);

    public final StringPath brewer = createString("brewer");

    public final StringPath continent = createString("continent");

    public final StringPath country = createString("country");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final StringPath name = createString("name");

    public final NumberPath<Double> price = createNumber("price", Double.class);

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final ListPath<Review, QReview> reviews = this.<Review, QReview>createList("reviews", Review.class, QReview.class, PathInits.DIRECT2);

    public final NumberPath<Integer> stock = createNumber("stock", Integer.class);

    public final StringPath type = createString("type");

    public QBeer(String variable) {
        super(Beer.class, forVariable(variable));
    }

    public QBeer(Path<? extends Beer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBeer(PathMetadata metadata) {
        super(Beer.class, metadata);
    }

}

