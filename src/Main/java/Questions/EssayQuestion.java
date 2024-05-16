package Questions;

public class EssayQuestion extends Question {
    public EssayQuestion() {}
    public EssayQuestion(int id, int grade, String subject, String chapter, int difficulty,
                         String question, String suggestion, double score) {
        super(id, "Essay", grade, subject, chapter, difficulty, question, suggestion, score);
    }

    public String toString() { 
        return "id = " + getId() +
                "\ntype = " + "Essay" +
                "\ngrade = " + getGrade() +
                "\nsubject = '" + getSubject() + '\'' +
                "\nchapter = '" + getChapter() + '\'' +
                "\ndifficulty = " + getDifficulty() +
                "\nquestion = '" + getQuestion() + '\'' +
                "\nsuggestion = '" + getSuggestion() + '\'' +
                "\nscore = " + getScore() +
                "\n}";
    }
}
