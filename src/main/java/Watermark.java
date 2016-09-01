import org.json.JSONObject;

/**
 * Created by syrovo01 on 26.08.2016.
 */
public class Watermark {
	private String content;
	private String title;
	private String author;
	private Topic topic;

	public Watermark(String content, String title, String author, Topic topic){
		this.content = content;
		this.title = title;
		this.author = author;
		this.topic = topic;
	}

	public Watermark(String content, String title, String author){
		this.content = content;
		this.title = title;
		this.author = author;
	}

	public String toString(){
		JSONObject obj = new JSONObject();
		obj.put("content", this.content);
		obj.put("title", this.title);
		obj.put("author", this.author);
		obj.put("topic", topic.toString());
		return obj.toString();
	}

}
