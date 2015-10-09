package uk.co.keithj.postcodes3.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import uk.co.keithj.postcodes3.api.StoreProvider;

@RestController
public class UploadFileAction {

	@Autowired
	private StoreProvider storeProvider;

	@RequestMapping(value = "/upload")
	public @ResponseBody String upload(String bucket, String key, String filename) {

		storeProvider.store(bucket, key, filename);

		return "OK";
	}

}
