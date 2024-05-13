package Questions;

public class Answer {
    private String content;
    private boolean status;
    public Answer() {}
    public Answer(String content, boolean status) {
        this.content = content;
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return content;
    }
}
