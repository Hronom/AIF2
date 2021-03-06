package io.aif.language.word.comparator;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class OptimizedComparatorWrapper implements IGroupComparator {

    private final IGroupComparator setComparator;

    OptimizedComparatorWrapper(IGroupComparator setComparator) {
        this.setComparator = setComparator;
    }

    @Override
    public double compare(final Collection<String> t1, final Collection<String> t2) {
        final Set<String> lowerCaseSet1 = new HashSet<>();
        final Set<String> lowerCaseSet2 = new HashSet<>();
        t1.forEach(token -> lowerCaseSet1.add(token.toLowerCase()));
        t2.forEach(token -> lowerCaseSet2.add(token.toLowerCase()));
        final double primitiveResult = Type.PRIMITIVE.getComparator().compare(lowerCaseSet1, lowerCaseSet2);
        if (primitiveResult == 1.) return primitiveResult;
        if (primitiveResult == .0) return primitiveResult;
        return setComparator.compare(lowerCaseSet1, lowerCaseSet2);
    }

}
