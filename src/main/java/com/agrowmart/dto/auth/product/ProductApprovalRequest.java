package com.agrowmart.dto.auth.product;

public record ProductApprovalRequest(
    String action,           // "APPROVE" / "REJECT" / "DELETE"
    String rejectionReason   // optional
) {}