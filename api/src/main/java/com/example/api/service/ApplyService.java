package com.example.api.service;

import com.example.api.domain.Coupon;
import com.example.api.repository.CouponCountRepository;
import com.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }

    // 쿠폰 발급
    public void apply(Long userId) {
        // lock을 활용 하면 성능상 이슈가 발생한다.(lock을 걸어야 하는 범위가 넓어서)
        // redis의 incr명령어를 활용하여 발급된 쿠폰의 갯수를 제어한다.
        Long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));
    }
}
