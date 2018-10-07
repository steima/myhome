package org.steinbauer.myhome.io;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.Value;

@Value
public class SurveillanceImagesPath {

	private Path path;
	
	public SurveillanceImagesPath(String pathName) {
		this.path = Paths.get(pathName);
	}
	
	public List<SurveillanceImage> images() {
		LinkedList<SurveillanceImage> list = new LinkedList<>();
		File[] files = this.path.toFile().listFiles();
		Arrays.sort(files);
		for(File f : files) {
			SurveillanceImage image = new SurveillanceImage(f.toPath());
			if(image.isImage() && image.getTimeStamp() != null) {
				list.add(image);
			}
		}
		return list;
	}

	public List<SurveillanceEvent> events() {
		LinkedList<SurveillanceEvent> events = new LinkedList<>();
		
		SurveillanceEvent currentEvent = new SurveillanceEvent();
		for(SurveillanceImage image : this.images()) {
			if(!currentEvent.offer(image)) {
				events.add(currentEvent);
				currentEvent = new SurveillanceEvent();
			}
		}
		if(!currentEvent.isEmpty()) {
			events.add(currentEvent);
		}
		
		return events;
	}
	
}
