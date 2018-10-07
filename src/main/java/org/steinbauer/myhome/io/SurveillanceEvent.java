package org.steinbauer.myhome.io;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import org.steinbauer.myhome.Constants;

public class SurveillanceEvent {

	private String id;
	
	private Instant from;
	private Instant until;
	
	private List<SurveillanceImage> images;
	
	public SurveillanceEvent() {
		this.id = null;
		this.from = null;
		this.until = null;
		this.images = new LinkedList<>();
	}
	
	public String getId() {
		return this.id;
	}
	
	public Instant getFrom() {
		return this.from;
	}
	
	public Instant getUntil() {
		return this.until;
	}
	
	public boolean isEmpty() {
		return this.images.isEmpty();
	}
	
	public List<SurveillanceImage> getImages() {
		return this.images;
	}
	
	public int getSize() {
		return this.images.size();
	}
	
	public Duration shortestTemporalDistance(Instant instant) {
		if(this.isEmpty()) {
			return Duration.ZERO;
		}
		Duration currentDuration = null;
		for(SurveillanceImage i : this.images) {
			Duration d = Duration.between(i.getTimeStamp(), instant);
			if(currentDuration == null || d.compareTo(currentDuration) < 0) {
				currentDuration = d;
			}
		}
		return currentDuration;
	}
	
	public boolean canAcceptImage(SurveillanceImage image) {
		return this.isEmpty() || this.shortestTemporalDistance(image.getTimeStamp()).compareTo(Constants.MAX_IMAGE_DISTANCE_SEC) < 0;
	}
	
	public void add(SurveillanceImage image) {
		Instant imageTimeStamp = image.getTimeStamp();
		if(this.from == null || imageTimeStamp.isBefore(this.from)) {
			this.from = imageTimeStamp;
			this.id = this.from.toString();
		}
		if(this.until == null || imageTimeStamp.isAfter(this.until)) {
			this.until = imageTimeStamp;
		}
		this.images.add(image);
	}
	
	public boolean offer(SurveillanceImage image) {
		if(!this.canAcceptImage(image)) {
			return false;
		}
		this.add(image);
		return true;
	}

	@Override
	public String toString() {
		if(this.isEmpty()) { 
			return String.format("Empty %s", this.getClass().getSimpleName());
		}
		return String.format("%s - %s, %d images", this.from.toString(), this.until.toString(), this.images.size());
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SurveillanceEvent) {
			SurveillanceEvent other = (SurveillanceEvent) obj;
			return this.id.equals(other.id) &&
					this.from.equals(other.from) &&
					this.until.equals(other.until);
		}
		return false;
	}
	
	
	

}
