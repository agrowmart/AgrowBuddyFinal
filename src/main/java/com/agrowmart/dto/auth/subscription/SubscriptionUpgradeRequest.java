package com.agrowmart.dto.auth.subscription;

import com.agrowmart.enums.SubscriptionPlan;

public record SubscriptionUpgradeRequest(SubscriptionPlan plan) {}