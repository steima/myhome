package org.steinbauer.myhome.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.steinbauer.myhome.io.SurveillanceEvent;
import org.steinbauer.myhome.io.SurveillanceService;

@RestController
@RequestMapping("/api")
public class SurveillanceController {
	
	@Autowired
	private SurveillanceService service;

	@GetMapping(value="/events", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<SurveillanceEvent> events() {
		return this.service.events();
	}
	
	@GetMapping(value="/events/{id}", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public SurveillanceEvent event(@PathVariable String id) throws EventNotFoundException {
		return this.service.eventById(id);
	}
	
	@GetMapping(value="/events/{id}/original/{imageIndex}.jpg", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] originalImage(@PathVariable String id, @PathVariable Integer imageIndex) throws EventNotFoundException, IOException {
		SurveillanceEvent event = this.event(id);
		return this.service.getOriginalImage(event, imageIndex);
	}
	
	@GetMapping(value="/events/{id}/keyframe.jpg", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] keyFrame(@PathVariable String id) throws EventNotFoundException, IOException {
		SurveillanceEvent event = this.event(id);
		int keyFrameIndex = event.getSize() / 2;
		return this.service.getOriginalImage(event, keyFrameIndex);
	}
	
	@GetMapping(value="/events/{id}/animation.gif", produces=MediaType.IMAGE_GIF_VALUE)
	public byte[] animation(@PathVariable String id) throws EventNotFoundException, IOException {
		SurveillanceEvent event = this.event(id);
		return this.service.generateGif(event);
	}
	
}
