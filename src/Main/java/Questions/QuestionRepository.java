package Questions;

import DatabaseConfiguration.DBConnector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import Lib.Table;

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
        String typeF;
        while (true) {
            System.out.println("Enter question type (E/MC): ");
            typeF = scanner.nextLine();
            if (typeF.isEmpty() || typeF.equalsIgnoreCase("E") || typeF.equalsIgnoreCase("MC")) break;
            else System.out.println("Invalid type! Type must be 'E' or 'MC'");

        }
        if (!typeF.isEmpty()) columnFilter.put("Type", typeF);

        // Grade
        String gradeF;
        while (true) {
            try {
                System.out.println("Enter grade: ");
                gradeF = scanner.nextLine();
                if (gradeF.isEmpty()) break;
                int grade1 = Integer.parseInt(gradeF.trim());
                if (grade1 >= 1 && grade1 <= 12) break;
                else System.out.println("Invalid grade! Grade must be between 1 and 12");
            }
            catch (NumberFormatException e) {
                System.out.println("Must enter a integer value! Try again");
                scanner.nextLine();
            }
        }
        if (!gradeF.isEmpty()) {
            columnFilter.put("Grade", gradeF);
        }
        // Subject
        System.out.println("Enter Subject: ");
        String subjectF = scanner.nextLine();
        if (!subjectF.isEmpty()) columnFilter.put("Subject", subjectF);

        // Chapter
        System.out.println("Enter Chapter: ");
        String chapterF = scanner.nextLine();
        if (!chapterF.isEmpty()) columnFilter.put("Chapter", chapterF);

        // Difficulty
        String difficultyF;
        while (true) {
            try {
                System.out.println("Enter difficulty (1 for Easy / 2 for Medium / 3 for Hard): ");
                difficultyF = scanner.nextLine();
                if (difficultyF.isEmpty()) break;

                int difficulty1 = Integer.parseInt(difficultyF.trim());
                if (difficulty1 >= 1 && difficulty1 <= 3) break;
                else System.out.println("Invalid difficulty! Difficulty must be 1, 2 or 3");
            }
            catch (NumberFormatException e) {
                System.out.println("Must enter a integer value! Try again");
                scanner.nextLine();
            }

        }
        if (!difficultyF.isEmpty()) columnFilter.put("Difficulty", difficultyF);

        // Score
        String scoreF;
        while (true) {

            try {
                System.out.println("Enter score (Scale of 10): ");
                scoreF = scanner.nextLine();
                if (scoreF.isEmpty()) break;
                double score1 = Double.parseDouble(scoreF.trim());
                if (score1 >= 1 && score1 <= 10) {
                    break;
                } else {
                    System.out.println("Invalid score! Score must be 10-point scale");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Must enter a double value! Try again");
                scanner.nextLine();
            }
        }
        if (!scoreF.isEmpty()) columnFilter.put("Score", scoreF);


        if (columnFilter.size() > 0) {
            List<Question> filteredQuestions = new ArrayList<>();
            boolean foundMatchingQuestions = false;
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
                    foundMatchingQuestions = true;
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
                if (!foundMatchingQuestions) {
                    System.out.println("No question found");
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

    //chưa xử lý
    public void addQuestion() {
        Scanner scanner = new Scanner(System.in);
        List<Answer> answers = new ArrayList<>();

        String type;
        while (true) {
            System.out.println("Enter question type (E/MC): ");
            type = scanner.nextLine();
            if (type.equalsIgnoreCase("E") || type.equalsIgnoreCase("MC")) {
                break;
            } else {
                System.out.println("Invalid type! Type must be 'E' or 'MC'");
            }
        }

        int grade;
        while (true) {
            try {
                System.out.println("Enter grade: ");
                grade = scanner.nextInt();
                if (grade >= 1 && grade <= 12) break;
                else System.out.println("Invalid grade! Grade must be between 1 and 12");
            }
            catch (InputMismatchException e) {
                System.out.println("Must enter a integer value! Try again");
                scanner.nextLine();
            }
        }

        System.out.println("Enter subject: ");
        scanner.nextLine();
        String subject = scanner.nextLine();

        System.out.println("Enter chapter: ");
        String chapter = scanner.nextLine();

        int difficulty;
        while (true) {
            try {
                System.out.println("Enter difficulty (1 for Easy / 2 for Medium / 3 for Hard): ");
                difficulty = scanner.nextInt();
                if (difficulty >= 1 && difficulty <= 3) break;
                else System.out.println("Invalid difficulty! Difficulty must be 1, 2 or 3");
            }
            catch (InputMismatchException e) {
                System.out.println("Must enter a integer value! Try again");
                scanner.nextLine();
            }
        }

        scanner.nextLine();

        System.out.println("Enter question: ");
        String question = scanner.nextLine();

        if (type.equalsIgnoreCase("MC")) {
            int numChoice = 0;
            while (true) {
                try {
                    System.out.println("Enter number of choices: ");
                    numChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    if (numChoice > 0 && numChoice <= 10) break;
                    else System.out.println("Invalid number! Please try again");
                }
                catch (InputMismatchException e) {
                    System.out.println("Must enter a integer value! Try again");
                    scanner.nextLine();
                }
            }

            MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
            mcq.addAnswer(numChoice);
            answers = mcq.getAnswers();
            System.out.println("Enter suggestion: ");
            String suggestion = scanner.nextLine();

            double score;
            while(true) {
                try {
                    System.out.println("Enter score (Scale of 10): ");
                    score = scanner.nextDouble();
                    if (score >= 0 && score <= 10) break;
                    else System.out.println("Invalid score! Score must be 10-point scale");
                }
                catch (InputMismatchException e) {
                    System.out.println("Must enter a double value! Try again");
                    scanner.nextLine();
                }
            }

            scanner.nextLine(); // Consume the newline character

            mcq = new MultipleChoiceQuestion(nextId, grade, subject, chapter, difficulty,
                    question, answers, suggestion, score);
            questions.add(mcq);
            System.out.println("Multiple Choice Question added successfully!");
            nextId++;
        } else {
            System.out.println("Enter suggestion: ");
            String suggestion = scanner.nextLine();

            double score;
            while(true) {
                try {
                    System.out.println("Enter score (Scale of 10): ");
                    score = scanner.nextDouble();
                    if (score >= 0 && score <= 10) break;
                    else System.out.println("Invalid score! Score must be 10-point scale");
                }
                catch (InputMismatchException e) {
                    System.out.println("Must enter a double value! Try again");
                    scanner.nextLine();
                }
            }

            scanner.nextLine(); // Consume the newline character

            EssayQuestion eq = new EssayQuestion(nextId, grade, subject, chapter, difficulty,
                    question, suggestion, score);
            questions.add(eq);
            System.out.println("Essay Question added successfully!");
            nextId++;
        }

    }

    public void updateQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id) {
                Scanner scanner = new Scanner(System.in);

                int difficulty;
                while (true) {
                    try {
                        System.out.println("Enter difficulty (1 for Easy / 2 for Medium / 3 for Hard): ");
                        difficulty = scanner.nextInt();
                        scanner.nextLine();
                        if (difficulty >= 1 && difficulty <= 3) {
                            System.out.println("Question difficulty edited successfully!");
                            break;
                        }
                        else System.out.println("Invalid difficulty! Difficulty must be 1, 2 or 3");
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Must enter a integer value! Try again");
                        scanner.nextLine();
                    }
                }


                System.out.println("Enter new question: ");
                String question = scanner.nextLine();
                if (q instanceof MultipleChoiceQuestion) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                    int numChoice = 0;
                    while (true) {
                        try {
                            System.out.println("Enter number of choices: ");
                            numChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character

                            if (numChoice > 0 && numChoice <= 10){
                                System.out.println("Multiple question edited successfully!");
                                break;
                            }
                            else System.out.println("Invalid number! Please try again");
                        }
                        catch (InputMismatchException e) {
                            System.out.println("Must enter a integer value! Try again");
                            scanner.nextLine();
                        }
                    }
                    mcq.addAnswer(numChoice);
                }

                double score;
                while(true) {
                    try {
                        System.out.println("Enter score (Scale of 10): ");
                        score = scanner.nextDouble();
                        if (score >= 0 && score <= 10) {
                            System.out.println("Question score edited successfully!");
                            break;
                        }
                        else System.out.println("Invalid score! Score must be 10-point scale");
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Must enter a double value! Try again");
                        scanner.nextLine();
                    }
                }

                q.setQuestion(question);
                q.setDifficulty(difficulty);
                q.setScore(score);
                break;
            }
        }
    }

    public void deleteQuestion(int id) {
        for (Question q : questions) {
            if (q.getId() == id){
                questions.remove(q);
                System.out.println("Question deleted successfully!");
                break;
            }
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
            String query = "INSERT INTO questions (ID, Type, Grade, Subject, Chapter, Difficulty, Question, Suggestion, Score) " +
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
            if (!questions.isEmpty()) nextId = questions.get(questions.size() - 1).getId() + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void display(List<Question> questions) {
        Table table = new Table("ID", "Type", "Grade", "Subject",
                "Chapter", "Difficulty", "Question", "Suggestion", "Score");
        for (Question q : questions) {
            String id = Integer.toString(q.getId());
            String type = (q instanceof MultipleChoiceQuestion) ? "Multiple Choice" : "Essay";
            String grade = Integer.toString(q.getGrade());
            String subject = q.getSubject();
            String chapter = q.getChapter();
            String difficulty = Integer.toString(q.getDifficulty());
            String suggestion = q.getSuggestion();
            String score = Double.toString(q.getScore());
            String question = q.getQuestion();
            if (q instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                List<Answer> a = mcq.getAnswers();
                for (int j = 0; j < a.size(); j++) {
                    question += "\n" + (char)(j + 65) + ". " + a.get(j).toString();
                }
            }
            table.addRow(id, type, grade, subject, chapter, difficulty, question, suggestion, score);
        }
        System.out.println(table);
    }
}

