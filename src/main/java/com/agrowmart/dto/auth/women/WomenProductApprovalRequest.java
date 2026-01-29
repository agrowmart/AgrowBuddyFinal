package com.agrowmart.dto.auth.women;

public record WomenProductApprovalRequest(
    String action,
    String rejectionReason
) {}