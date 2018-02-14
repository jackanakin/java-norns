package com.ark.norns.specification;

import com.ark.norns.entity.DeviceAuthenticationProfile;
import com.ark.norns.entity.DeviceAuthenticationProfile_;
import com.ark.norns.enumerated.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DeviceAuthenticationProfileSpecification {
    public static Specification<DeviceAuthenticationProfile> selectMenuFilter(Status status) {
        return new Specification<DeviceAuthenticationProfile>() {
            @Override
            public Predicate toPredicate(Root<DeviceAuthenticationProfile> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                Predicate predicate = builder.and();

                // APPLY FITLERS
                if (status != null) {
                    predicate = builder.and(predicate, builder.equal(root.get(DeviceAuthenticationProfile_.status), status));
                }
                return predicate;
            }
        };
    }
}
