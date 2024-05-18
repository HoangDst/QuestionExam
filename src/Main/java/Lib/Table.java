package Lib;

/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    private List<String> headers = new ArrayList<>();
    private List<List<String>> data = new ArrayList<>();

    public Table(String... headers) {
        this.headers.addAll(Arrays.asList(headers));
    }

    public void addRow(String... row) {
        data.add(Arrays.asList(row));
    }

    private int getMaxSize(int column) {
        int maxSize = headers.get(column).length();
        for (List<String> row : data) {
            if (row.get(column).length() > maxSize)
                maxSize = row.get(column).length();
        }
        return maxSize;
    }

private String formatRow(List<String> row) {
    StringBuilder result = new StringBuilder();
    List<List<String>> formattedData = new ArrayList<>();

    // Chuẩn bị dữ liệu đã được định dạng
    for (int i = 0; i < row.size(); i++) {
        String cell = row.get(i);
        List<String> formattedCellLines = new ArrayList<>();

        // Tách dòng cho ô chứa xuống dòng
        if (cell.contains("\n")) {
            String[] lines = cell.split("\n");
            formattedCellLines.addAll(Arrays.asList(lines));
        } else {
            formattedCellLines.add(cell);
        }

        formattedData.add(formattedCellLines);
    }

    // Xác định số dòng dài nhất trong mỗi ô
    int maxRows = formattedData.stream()
            .mapToInt(List::size)
            .max()
            .orElse(0);

    // Tạo các dòng cho bảng
    for (int rowIdx = 0; rowIdx < maxRows; rowIdx++) {
        result.append("|");
        for (List<String> formattedCellLines : formattedData) {
            String cellLine = rowIdx < formattedCellLines.size() ? formattedCellLines.get(rowIdx) : "";
            int maxWidth = getMaxSize(formattedData.indexOf(formattedCellLines)) + 2;
            result.append(centerAlign(cellLine, maxWidth));
            result.append("|");
        }
        result.append("\n");
    }

    return result.toString();
}

    private String formatRule() {
        StringBuilder result = new StringBuilder();
        result.append("+");
        for (int i = 0; i < headers.size(); i++) {
            for (int j = 0; j < getMaxSize(i) + 2; j++) {
                result.append("-");
            }
            result.append("+");
        }
        result.append("\n");
        return result.toString();
        }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(formatRule());
        result.append(formatRow(headers));
        result.append(formatRule());

        if (!data.isEmpty()) {
            for (List<String> row : data) {
                result.append(formatRow(row));
                result.append(formatRule());
            }
        }

        return result.toString();
    }

    private String centerAlign(String input, int width) {
        int inputLength = input.length();
        int totalPadding = width - inputLength;

        if (totalPadding <= 0) {
            return input;
        }

        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        String format = "%" + leftPadding + "s%s%" + rightPadding + "s";
        return String.format(format, "", input, "");
    }
}

 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {
    private List<String> headers = new ArrayList<>();
    private List<List<String>> data = new ArrayList<>();
    private List<Boolean> columnAlignments = new ArrayList<>();

    public Table(String... headers) {
        this.headers.addAll(Arrays.asList(headers));

        for (int i = 0; i < headers.length; i++) {
            columnAlignments.add(true); // Set center alignment as default
        }
    }

    public void addRow(String... row) {
        data.add(Arrays.asList(row));
    }

    public void setColumnAlignment(int column, boolean center) {
        if (column >= 0 && column < headers.size()) {
            columnAlignments.set(column, center);
        }
    }

    private int getMaxSize(int column) {
        int maxSize = headers.get(column).length();
        for (List<String> row : data) {
            if (row.get(column).length() > maxSize)
                maxSize = row.get(column).length();
        }
        return maxSize;
    }

    private String formatRow(List<String> row) {
        StringBuilder result = new StringBuilder();
        List<List<String>> formattedData = new ArrayList<>();

        // Prepare formatted data
        for (int i = 0; i < row.size(); i++) {
            String cell = row.get(i);
            List<String> formattedCellLines = new ArrayList<>();

            // Split cell into lines if it contains line breaks
            if (cell.contains("\n")) {
                String[] lines = cell.split("\n");
                formattedCellLines.addAll(Arrays.asList(lines));
            } else {
                formattedCellLines.add(cell);
            }

            formattedData.add(formattedCellLines);
        }

        // Determine the maximum number of rows in each cell
        int maxRows = formattedData.stream()
                .mapToInt(List::size)
                .max()
                .orElse(0);

        // Create table rows
        for (int rowIdx = 0; rowIdx < maxRows; rowIdx++) {
            result.append("|");
            for (List<String> formattedCellLines : formattedData) {
                String cellLine = rowIdx < formattedCellLines.size() ? formattedCellLines.get(rowIdx) : "";
                int maxWidth = getMaxSize(formattedData.indexOf(formattedCellLines)) + 2;
                if (columnAlignments.get(formattedData.indexOf(formattedCellLines))) {
                    result.append(centerAlign(cellLine, maxWidth));
                } else {
                    result.append(leftAlign(cellLine, maxWidth));
                }
                result.append("|");
            }
            result.append("\n");
        }

        return result.toString();
    }

    private String formatRule() {
        StringBuilder result = new StringBuilder();
        result.append("+");
        for (int i = 0; i < headers.size(); i++) {
            for (int j = 0; j < getMaxSize(i) + 2; j++) {
                result.append("-");
            }
            result.append("+");
        }
        result.append("\n");
        return result.toString();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(formatRule());
        result.append(formatRow(headers));
        result.append(formatRule());

        if (!data.isEmpty()) {
            for (List<String> row : data) {
                result.append(formatRow(row));
                result.append(formatRule());
            }
        }

        return result.toString();
    }

    private String centerAlign(String input, int width) {
        int inputLength = input.length();
        int totalPadding = width - inputLength;

        if (totalPadding <= 0) {
            return input;
        }

        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;

        String format = "%" + leftPadding + "s%s%" + rightPadding + "s";
        return String.format(format, "", input, "");
    }

    private String leftAlign(String input, int width) {
        int inputLength = input.length();
        int totalPadding = width - inputLength;

        if (totalPadding <= 0) {
            return input;
        }

        String format = "%-" + width + "s";
        return String.format(format, input);
    }
}