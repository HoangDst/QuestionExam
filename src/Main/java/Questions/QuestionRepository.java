package Questions;

import DatabaseConfiguration.DBConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QuestionRepository {
    private static int nextId = 1;
    private List<Question> questions;
    private DBConnector connector = new DBConnector();

    public QuestionRepository() {
        questions = new ArrayList<>();
        loadQuestions();
    }
    public List<Question> getQuestions() {
        return questions;
    }
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public List<Question> filter() {
        HashMap<String, String> columnFilter = new HashMap<String, String>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter values to filter (Enter to skip):");
        System.out.println("Question type (E/MC): ");
        String typeF = scanner.nextLine();
        if (!typeF.isEmpty()) columnFilter.put("Type", typeF);

        // Grade
        System.out.println("Grade: ");
        String gradeF = scanner.nextLine();
        if (!gradeF.isEmpty()) columnFilter.put("Grade", gradeF);

        // Subject
        System.out.println("Subject: ");
        String subjectF = scanner.nextLine();
        if (!subjectF.isEmpty()) columnFilter.put("Subject", subjectF);

        // Chapter
        System.out.println("Chapter: ");
        String chapterF = scanner.nextLine();
        if (!chapterF.isEmpty()) columnFilter.put("Chapter", chapterF);

        // Difficulty
        System.out.println("Difficulty: ");
        String difficultyF = scanner.nextLine();
        if (!difficultyF.isEmpty()) columnFilter.put("Difficulty", difficultyF);

        // Score
        System.out.println("Score: ");
        String scoreF = scanner.nextLine();
        if (!scoreF.isEmpty()) columnFilter.put("Score", scoreF);


        if (columnFilter.size() > 0) {
            List<Question> filteredQuestions = new ArrayList<>();
            int size = columnFilter.size();
            String query = "SELECT * FROM questions WHERE ";
            for (String c : columnFilter.keySet()) {
                query += c + " = ?";
                size--;
                if (size > 0) query += " AND ";
            }

            try (PreparedStatement preparedStatement = connector.getConnection().prepareStatement(query)) {
                int index = 1;
                for (String c : columnFilter.keySet()) {
                    preparedStatement.setString(index++, columnFilter.get(c));
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");
                    String type = resultSet.getString("Type");
                    int grade = resultSet.getInt("Grade");
                    String subject = resultSet.getString("Subject");
                    String chapter = resultSet.getString("Chapter");
                    int difficulty = resultSet.getInt("Difficulty");
                    String question = resultSet.getString("Question");
                    String suggestion = resultSet.getString("Suggestion");
                    double score = resultSet.getDouble("Score");
                    if (type.equalsIgnoreCase("MC")) {
                        List<Answer> answers = new ArrayList<>();
                        String answerQuery = "SELECT * FROM answers WHERE questionID = " + id;
                        try (PreparedStatement answerStatement = connector.getConnection().prepareStatement(answerQuery)) {
                            ResultSet answerSet = answerStatement.executeQuery();
                            while (answerSet.next()) {
                                String content = answerSet.getString("Content");
                                boolean status = answerSet.getBoolean("Status");
                                answers.add(new Answer(content, status));
                            }
                        }
                        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(id, grade, subject, chapter, difficulty,
                                question, answers, suggestion, score);
                        filteredQuestions.add(mcq);
                    } else {
                        EssayQuestion eq = new EssayQuestion(id, grade, subject, chapter, difficulty, question, suggestion, score);
                        filteredQuestions.add(eq);
                    }
                }
                return filteredQuestions;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return questions;
    }
    public boolean questionExist(int id) {
        for (Question q : questions) {
            if (q.getId() == id) return true;
        }
        return false;
    }
    public Question getQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) return q;
        }
        return null;
    }
    public void addQuestion() {
        Scanner scanner = new Scanner(System.in);
        List<Answer> answers = new ArrayList<>();
        System.out.println("Enter question type (E/MC): ");
        String type = scanner.nextLine();
        System.out.println("Enter grade: ");
        int grade = scanner.nextInt();
        System.out.println("Enter subject: ");
        scanner.nextLine(); // Clear residual newline character
        String subject = scanner.nextLine();
        System.out.println("Enter chapter: ");
        String chapter = scanner.nextLine();
        System.out.println("Enter difficulty: ");
        int difficulty = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        System.out.println("Enter question: ");
        String question = scanner.nextLine();

        if (type.equalsIgnoreCase("MC")) {
            int numChoice = 0;
            while (true) {
                System.out.println("Enter number of choices: ");
                numChoice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                if (numChoice > 0 && numChoice <= 10) {
                    break;
                } else {
                    System.out.println("Invalid number! Please try again");
                }
            }

            MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
            mcq.addAnswer(numChoice);
            answers = mcq.getAnswers();
            System.out.println("Enter suggestion: ");
            String suggestion = scanner.nextLine();
            System.out.println("Enter score: ");
            double score = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            mcq = new MultipleChoiceQuestion(nextId, grade, subject, chapter, difficulty,
                    question, answers, suggestion, score);
            questions.add(mcq);
            nextId++;
        } else {
            System.out.println("Enter suggestion: ");
            String suggestion = scanner.nextLine();
            System.out.println("Enter score: ");
            double score = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            EssayQuestion eq = new EssayQuestion(nextId, grade, subject, chapter, difficulty,
                    question, suggestion, score);
            questions.add(eq);
            nextId++;
        }

        scanner.close();
    }
    public void updateQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter new question: ");
                String question = scanner.nextLine();
                if (q instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                    int numChoice = 0;
                    while (true) {
                        System.out.println("Enter number of choices: ");
                        numChoice = scanner.nextInt();
                        if (numChoice > 0 && numChoice <= 10) break;
                        else System.out.println("Invalid number! Please try again");
                    }
                    mcq.addAnswer(numChoice);
                }
                q.setQuestion(question);
                break;
            }
        }
    }
    public void deleteQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) questions.remove(q);
            break;
        }
    }
    private void deleteDatabaseQuestions() {
        String query = "DELETE FROM answers";
        connector.execution(query);
        query = "DELETE FROM questions";
        connector.execution(query);
    }
    public void saveQuestions() {
        deleteDatabaseQuestions();
        for (Question q : questions) {
            String query = "INSERT INTO Questions (ID, Type, Grade, Subject, Chapter, Difficulty, Question, Suggestion, Score) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            List<Object> parameters = Arrays.asList(
                    q.getId(),
                    q.getType(),
                    q.getGrade(),
                    q.getSubject(),
                    q.getChapter(),
                    q.getDifficulty(),
                    q.getQuestion(),
                    q.getSuggestion(),
                    q.getScore()
            );
            connector.execution(query, parameters);
            if (q.getType().equalsIgnoreCase("MC")) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                mcq.saveAnswer();
            }
        }
    }
    public void loadQuestions() {
        if (!questions.isEmpty()) questions.clear();
        String query = "SELECT * FROM questions";
        try (PreparedStatement preparedStatement = connector.getConnection().prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String type = resultSet.getString("Type");
                int grade = resultSet.getInt("Grade");
                String subject = resultSet.getString("Subject");
                String chapter = resultSet.getString("Chapter");
                int difficulty = resultSet.getInt("Difficulty");
                String question = resultSet.getString("Question");
                String suggestion = resultSet.getString("Suggestion");
                double score = resultSet.getDouble("Score");
                if (type.equalsIgnoreCase("MC")) {
                    List<Answer> answers = new ArrayList<>();
                    String answerQuery = "SELECT * FROM answers WHERE questionID = " + id;
                    try (PreparedStatement answerStatement = connector.getConnection().prepareStatement(answerQuery)) {
                        ResultSet answerSet = answerStatement.executeQuery();
                        while (answerSet.next()) {
                            String content = answerSet.getString("Content");
                            boolean status = answerSet.getBoolean("Status");
                            answers.add(new Answer(content, status));
                        }
                    }
                    MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(id, grade, subject, chapter, difficulty,
                            question, answers, suggestion, score);
                    questions.add(mcq);
                } else {
                    EssayQuestion eq = new EssayQuestion(id, grade, subject, chapter, difficulty, question, suggestion, score);
                    questions.add(eq);
                }
            }
            if(!questions.isEmpty()) nextId = questions.get(questions.size() - 1).getId() + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

