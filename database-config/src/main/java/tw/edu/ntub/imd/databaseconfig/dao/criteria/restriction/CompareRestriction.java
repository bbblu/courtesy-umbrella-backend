package tw.edu.ntub.imd.databaseconfig.dao.criteria.restriction;


import tw.edu.ntub.imd.exception.IgnoreException;

import javax.annotation.Nonnull;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

public class CompareRestriction<E, V extends Comparable<? super V>> extends AbstractRestriction<E, V> {
    private V value;
    private Operator operator;

    public CompareRestriction(@Nonnull ExpressionSupplier<E, V> supplier, @Nonnull V value, Operator operator) {
        super(supplier);
        this.value = value;
        this.operator = operator;
    }

    @Override
    protected Predicate get(@Nonnull CriteriaBuilder builder, @Nonnull Expression<V> expression) {
        switch (operator) {
            case GREATER:
                return builder.greaterThan(expression, value);
            case GREATER_OR_EQUAL:
                return builder.greaterThanOrEqualTo(expression, value);
            case LESS:
                return builder.lessThan(expression, value);
            case LESS_OR_EQUAL:
                return builder.lessThanOrEqualTo(expression, value);
            default:
                throw new IgnoreException();
        }
    }

    protected enum Operator {
        GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL
    }
}
