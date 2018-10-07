package org.steinbauer.myhome.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.steinbauer.myhome.Constants;
import org.steinbauer.myhome.api.EventNotFoundException;
import org.steinbauer.myhome.image.GifSequenceWriter;

@Service
public class SurveillanceService {
	
	@Value("${myhome.surveillance.images}")
	private String imagePath;

	private SurveillanceImagesPath images;
	
	@PostConstruct
	public void init() {
		this.images = new SurveillanceImagesPath(this.imagePath);
	}
	
	public List<SurveillanceEvent> events() {
		return this.images.events();
	}
	
	public SurveillanceEvent eventById(String id) throws EventNotFoundException {
		return this.events().stream().filter(e -> e.getId().equals(id)).findAny().orElseThrow(() -> new EventNotFoundException(id));
	}

	public SurveillanceEvent eventByIndex(int index) throws EventNotFoundException {
		List<SurveillanceEvent> list = this.events();
		if(index < 0 || index >= list.size()) {
			throw new EventNotFoundException(index);
		}
		return list.get(index);
	}
	
	public BufferedImage loadSurveillanceImage(SurveillanceImage image) throws IOException {
		return ImageIO.read(image.getPath().toFile());
	}
	
	@Cacheable("originalImage")
	public byte[] getOriginalImage(SurveillanceEvent event, Integer imageIndex) throws IOException {
		SurveillanceImage image = event.getImages().get(imageIndex);
		BufferedImage bufferedImage = this.loadSurveillanceImage(image);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", bout);
		bout.flush();
		bout.close();;
		return bout.toByteArray();
	}

	@Cacheable("animation")
	public byte[] generateGif(SurveillanceEvent event) throws IIOException, IOException {
		if(event.isEmpty()) throw new RuntimeException(String.format("Cannot generate GIF from empty %s", event.getClass().getSimpleName()));
		List<SurveillanceImage> currentImages = event.getImages();
		SurveillanceImage firstSurveillanceImage = currentImages.get(0);
		BufferedImage firstBufferedImage = this.loadSurveillanceImage(firstSurveillanceImage);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(bout);
		GifSequenceWriter sequenceWriter = new GifSequenceWriter(imageOutputStream, firstBufferedImage.getType(), Constants.DELAY_BETWEEN_FRAMES, true);
		
		for(SurveillanceImage image : currentImages) {
			sequenceWriter.writeToSequence(this.loadSurveillanceImage(image));
		}
		
		sequenceWriter.close();
		imageOutputStream.close();
		bout.close();
		return bout.toByteArray();
	}

}
