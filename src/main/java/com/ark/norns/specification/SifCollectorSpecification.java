package com.ark.norns.specification;

import com.ark.norns.entity.SifCollector;
import com.ark.norns.entity.SifCollector_;
import com.ark.norns.enumerated.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SifCollectorSpecification {
    public static Specification<SifCollector> selectMenuFilter(Status status) {
        return new Specification<SifCollector>() {
            @Override
            public Predicate toPredicate(Root<SifCollector> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                Predicate predicate = builder.and();

                // APPLY FITLERS
                if (status != null) {
                    predicate = builder.and(predicate, builder.equal(root.get(SifCollector_.status), status));
                }
                return predicate;
            }
        };
    }
}
