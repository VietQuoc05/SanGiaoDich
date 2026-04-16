package com.ecommerce.supplier;

import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRequestRepository requestRepo;
    private final UserRepository userRepo;

    // 👤 USER gửi request
    public String requestBecomeSupplier(String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 check đã request chưa
        if (requestRepo.findByUserId(user.getId()).isPresent()) {
            return "You already requested";
        }

        SupplierRequest request = SupplierRequest.builder()
                .user(user)
                .status("PENDING")
                .build();

        requestRepo.save(request);

        // 🔥 update flag
        user.setSupplierRequest(true);
        userRepo.save(user);

        return "Request submitted";
    }

    // 👑 ADMIN xem list
    public List<SupplierRequest> getAllRequests() {
        return requestRepo.findAll();
    }

    // 👑 ADMIN approve
    public String approve(Long requestId) {

        SupplierRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus("APPROVED");

        User user = request.getUser();

        // 🔥 FIX ROLE
        user.setRole("ROLE_SUPPLIER");

        // reset flag
        user.setSupplierRequest(false);

        userRepo.save(user);
        requestRepo.save(request);

        return "Approved";
    }

    // 👑 ADMIN reject
    public String reject(Long requestId) {

        SupplierRequest request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus("REJECTED");

        User user = request.getUser();
        user.setSupplierRequest(false);

        userRepo.save(user);
        requestRepo.save(request);

        return "Rejected";
    }
}