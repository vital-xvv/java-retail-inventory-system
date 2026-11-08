package com.project.code.Repo;

import com.project.code.Model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Review findByStoreIdAndProductId(Long storeId, Long productId);
}
