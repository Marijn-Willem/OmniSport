package com.sports.cache.util;

import java.time.LocalDateTime;

public record CacheListObject(String key, LocalDateTime timeCreated, LocalDateTime timeLastRetrieved) {}
