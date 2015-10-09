package uk.co.keithj.postcodes3.api;

public interface StoreProvider {

	void retrieve(String message);

	void store(String bucket, String key, String filename);
}
