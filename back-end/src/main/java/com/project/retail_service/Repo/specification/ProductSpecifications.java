package com.project.retail_service.Repo.specification;

import com.project.retail_service.Model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    public static Specification<Product> hasCategory(String category) {
        if (isAbsent(category)) {
            return null;
        }
        String value = category.toLowerCase(Locale.ROOT);
        return (root, query, cb) -> cb.equal(cb.lower(root.get("category")), value);
    }

    public static Specification<Product> nameContains(String name) {
        if (isAbsent(name)) {
            return null;
        }
        String pattern = "%" + name.toLowerCase(Locale.ROOT) + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern);
    }

    public static Specification<Product> inStore(Long storeId) {
        if (storeId == null) {
            return null;
        }
        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }
            return cb.equal(root.join("inventory").get("store").get("id"), storeId);
        };
    }

    private static boolean isAbsent(String value) {
        return value == null || value.isBlank() || "null".equalsIgnoreCase(value);
    }
}
