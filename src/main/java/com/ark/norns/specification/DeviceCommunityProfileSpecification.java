package com.ark.norns.specification;

import com.ark.norns.entity.DeviceCommunityProfile;
import com.ark.norns.entity.DeviceCommunityProfile_;
import com.ark.norns.enumerated.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DeviceCommunityProfileSpecification {
    public static Specification<DeviceCommunityProfile> selectMenuFilter(Status status) {
        return new Specification<DeviceCommunityProfile>() {
            @Override
            public Predicate toPredicate(Root<DeviceCommunityProfile> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                Predicate predicate = builder.and();

                // APPLY FITLERS
                if (status != null) {
                    predicate = builder.and(predicate, builder.equal(root.get(DeviceCommunityProfile_.status), status));
                }
                return predicate;
            }
        };
    }
}
