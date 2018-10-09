package org.steinbauer.myhome;

import java.time.Duration;

public interface Constants {

	static String REVERSED_DATE_PATTERN = "yyyyMMddHHmmss";
	static Duration MAX_IMAGE_DISTANCE_SEC = Duration.ofSeconds(5);
	static int DELAY_BETWEEN_FRAMES = 500;
	
}
