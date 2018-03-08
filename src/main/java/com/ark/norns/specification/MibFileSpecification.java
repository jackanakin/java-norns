package com.ark.norns.specification;

import com.ark.norns.dataStructure.MibFile;
import com.ark.norns.dataStructure.MibFile_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class MibFileSpecification {
    public static Specification<MibFile> translateVariableBindingIntoMibFileOid() {
        return new Specification<MibFile>() {
            @Override
            public Predicate toPredicate(Root<MibFile> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                Predicate predicate = builder.and();
                predicate = builder.and(predicate, builder.isNotNull(root.get(MibFile_.rootOID)));
                return predicate;
            }
        };
    }
}
