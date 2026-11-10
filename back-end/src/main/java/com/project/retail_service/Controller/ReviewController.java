package com.project.retail_service.Controller;

import com.project.retail_service.Model.Customer;
import com.project.retail_service.Model.Review;
import com.project.retail_service.Repo.CustomerRepository;
import com.project.retail_service.Repo.ReviewRepository;
import com.project.retail_service.dto.request.ReviewDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;

    public ReviewController(ReviewRepository reviewRepository, CustomerRepository customerRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> findByStoreIdAndProductId(@PathVariable("storeId") long storeId,
                                                         @PathVariable("productId") long productId) {
        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);
        return Map.of("reviews", reviews.stream().map(r ->
                new ReviewDTO(r.getComment(), 
                        r.getRating(), 
                        customerRepository.findById(r.getCustomerId()).orElse(new Customer()).getName())
                ).toList());
    }
}
