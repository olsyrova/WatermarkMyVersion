import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by syrovo01 on 30.08.2016.
 */
public class Main {
	final static Logger logger = Logger.getRootLogger();
	private static final String resourcesPath = "C:\\Users\\syrovo01\\Projects\\word2vecTest\\output.json";
	private static Gson g = new Gson();

	public static void main(String[] args) throws IOException {
		//String jsonString = Files.toString(new File(resourcesPath), Charsets.UTF_8);
		//Document[] documents = g.fromJson(jsonString, Document[].class);
		final List<UUID> listOfIDs = new ArrayList<UUID>();
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(resourcesPath),
							"UTF-8"
					)
			);
			Document document = readJson(br);
			while (document != null) {
				document = readJson(br);
				UUID id = WatermarkService.addDocument(document);
				listOfIDs.add(id);
			}
			// wait till service completes its work
			Thread.sleep(5000);
			// request watermarked documents
			for (UUID id : listOfIDs) {
				if (WatermarkService.isWatermarked(id)) {
					Document watermarkedDocument = WatermarkService.getWatermarkedDocument(id);
					logger.info(watermarkedDocument.getWatermark().toString());
				}
			}
		}  catch (IOException ioe){
			ioe.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private static Document readJson(BufferedReader reader) throws IOException {
		final StringBuilder reconstructedJson = new StringBuilder();
		String line = reader.readLine();
		while (line != null && line != "[" && line != "]") {
			reconstructedJson.append(line);
			if ("},".equals(line.trim())) {
				JsonParser parser = new JsonParser();
				JsonObject obj = parser.parse(reconstructedJson.toString()).getAsJsonObject();
				if (obj.get("topic") != null){
					return g.fromJson(reconstructedJson.toString(), Book.class);
				} else {
					return g.fromJson(reconstructedJson.toString(), Journal.class);
				}
			}
			line = reader.readLine();
		}
		return null;
	}

}
