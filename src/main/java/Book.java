/**
 * Created by syrovo01 on 26.08.2016.
 */
public class Book extends Document{
	private Topic topic;


	public Book(String title, String author, Topic topic) {
		super(title, author);
		this.topic = topic;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
}
