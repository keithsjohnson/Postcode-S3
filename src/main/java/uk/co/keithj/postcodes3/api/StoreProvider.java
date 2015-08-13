package uk.co.keithj.postcodes3.api;

import org.springframework.core.io.Resource;

public interface StoreProvider {

	Resource retrieve(String url);

	void store(String key, String filename);
}
