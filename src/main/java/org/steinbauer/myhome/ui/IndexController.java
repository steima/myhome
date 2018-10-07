package org.steinbauer.myhome.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.steinbauer.myhome.api.EventNotFoundException;
import org.steinbauer.myhome.io.SurveillanceService;

@Controller
public class IndexController {
	
	@Autowired
	private SurveillanceService service;

	@GetMapping({ "/", "/events" })
	public String events(Model model) {
		model.addAttribute("events", this.service.events());
		return "index";
	}
	
	@GetMapping("/events/{id}")
	public String event(Model model, @PathVariable String id) throws EventNotFoundException {
		model.addAttribute("event", this.service.eventById(id));
		return "event";
	}
	
}
