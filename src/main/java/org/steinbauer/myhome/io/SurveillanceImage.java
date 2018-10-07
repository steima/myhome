package org.steinbauer.myhome.io;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import org.steinbauer.myhome.Constants;

import lombok.Value;

@Value
public class SurveillanceImage {

	private Path path;
	
	public String getFileName() {
		return this.path.getFileName().toString();
	}
	
	public boolean isImage() {
		return this.getFileName().endsWith(".jpg");
	}
	
	public Instant getTimeStamp() {
		String digitsOnly = this.getFileName().replaceAll("\\D", "");
		if(digitsOnly.length() != 14) {
			return null;
		}
		try {
			return new SimpleDateFormat(Constants.REVERSED_DATE_PATTERN).parse(digitsOnly).toInstant();
		} catch (ParseException e) {
			return null;
		}
	}
	
}
