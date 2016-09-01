import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by syrovo01 on 26.08.2016.
 * this is an asyncronous watermark service used to watermark a set of documents, the service should return a ticket for each document
 * ticket can be used for polling the status of the document; if watermarking is finished the document can be retrieved with the ticket
 */
public class WatermarkService {
	final static Logger logger = Logger.getRootLogger();
	private static Map<UUID, Document> pipeLine = new HashMap<UUID, Document>();
	private static Gson g = new Gson();
	private static File outputFile = new File("output.json");


	/*public WatermarkService(){
		pipeLine = new HashMap<UUID, Document>();
	}*/

	public static void main(String[] args){
		Document document1 = new Book("title1", "author1", Topic.BUSINESS);
		Document document2 = new Book("title2", "author2", Topic.SCIENCE);
		Document journal1 = new Journal("journal1", "author3");

	}

	// add document to the queue and return its ticket
	public static UUID addDocument(Document document){
		// check if document was already watermarked
		if (document.getWatermark() != null){
			logger.info("Document " + document.getTitle() + " is watermarked already");
			return null;
		}
		// generate unique ID for a document
		UUID docID = UUID.randomUUID();
		//document.setUniqueID(docID);
		pipeLine.put(docID, document);
		return docID;
	}

	// check status of watermarking
	public static boolean isWatermarked(UUID uuid){
		if (pipeLine.get(uuid) == null){
			logger.info("No such document in the pipeline");
		}
		Document document = pipeLine.get(uuid);
		return document.getWatermark() != null;
	}

	public static Document getWatermarkedDocument(UUID uuid){
		if (isWatermarked(uuid)){
			return pipeLine.get(uuid);
		}
		return null;
	}

	// create watermark for a given document
	public static String watermarkDocument(Document document){
		Watermark watermark = null;
		if (document instanceof Book){
			watermark = new Watermark("book", document.getTitle(), document.getAuthor(), ((Book) document).getTopic());
		} else if (document instanceof Journal){
			watermark = new Watermark("journal", document.getTitle(), document.getAuthor());
		}
		document.setWatermark(watermark);
		return document.toString();
	}

	public static void runWatermarkService(){
		for (Map.Entry<UUID, Document> documentEntry : pipeLine.entrySet()){
			watermarkDocument(documentEntry.getValue());
		}

	}

}
