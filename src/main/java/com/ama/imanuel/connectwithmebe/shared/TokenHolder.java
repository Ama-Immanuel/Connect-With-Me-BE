package com.ama.imanuel.connectwithmebe.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenHolder {
    String token;
    long timeExpired;
}
