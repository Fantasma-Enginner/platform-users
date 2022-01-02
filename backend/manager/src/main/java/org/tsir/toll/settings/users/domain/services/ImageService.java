package org.tsir.toll.settings.users.domain.services;

public interface ImageService {

	void saveImage(long identifier, byte[] content);
	
	byte[] retrieveImage(long identifier, float scale);
	
}
