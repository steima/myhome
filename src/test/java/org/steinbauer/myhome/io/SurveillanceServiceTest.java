package org.steinbauer.myhome.io;

import java.io.IOException;

import javax.imageio.IIOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SurveillanceServiceTest {
	
	@Autowired
	private SurveillanceService surveillanceService;
	
	@Test
	public void testGenerateGifs() throws IIOException, IOException {
		for (SurveillanceEvent e : this.surveillanceService.events()) {
			log.info(e.toString());
			byte[] gifImageData = this.surveillanceService.generateGif(e);
			if(gifImageData != null) {
				log.info("  generated {} bytes of GIF data", gifImageData.length);
			}else {
				log.error("  could not generate GIF");
			}
		}
	}

}
