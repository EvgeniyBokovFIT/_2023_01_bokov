public class MultiplicationTablePrinter {

    private static final String VERTICAL_SEPARATOR = "|";
    private static final String HORIZONTAL_SEPARATOR = "-";
    private static final String ANGLE_SEPARATOR = "+";
    private static final String GAP = " ";

    public void printMultiplicationTable(Integer tableSize) {
        int firstColumnLength = IntegerUtils.getNumberOfDigits(tableSize);
        int mainLength = IntegerUtils.getNumberOfDigits(tableSize * tableSize);
        TableInfo tableInfo = new TableInfo(tableSize, firstColumnLength, mainLength);

        String separatorLine = getSeparatorLine(tableInfo);

        System.out.println(getArgumentsLine(tableInfo));
        System.out.println(separatorLine);

        for (int curMultiplier = 1; curMultiplier <= tableSize; curMultiplier++) {
            System.out.println(getTableLine(curMultiplier, tableInfo));
            System.out.println(separatorLine);
        }
    }

    private String getSeparatorLine(TableInfo tableInfo) {
        return HORIZONTAL_SEPARATOR.repeat(tableInfo.firstColumnLength()) +
                (getSeparatorCell(tableInfo.mainColumnsLength())).repeat(tableInfo.size());
    }

    private String getSeparatorCell(int length) {
        return ANGLE_SEPARATOR +
                HORIZONTAL_SEPARATOR.repeat(length);
    }

    private String getArgumentsLine(TableInfo tableInfo) {
        StringBuilder result = new StringBuilder();
        result.append(GAP.repeat(tableInfo.firstColumnLength()));

        for (int curNum = 1; curNum <= tableInfo.size(); curNum++) {
            int gapsCount = tableInfo.mainColumnsLength() - IntegerUtils.getNumberOfDigits(curNum);
            result.append(getTableCell(gapsCount, curNum));
        }

        return result.toString();
    }

    private String getTableLine(int multiplier, TableInfo tableInfo) {
        StringBuilder result = new StringBuilder();

        int gapsCountFirstColumn = tableInfo.firstColumnLength() - IntegerUtils.getNumberOfDigits(multiplier);
        result.append(GAP.repeat(gapsCountFirstColumn))
                .append(multiplier);

        for (int curNum = 1; curNum <= tableInfo.size(); curNum++) {
            int curCellValue = curNum * multiplier;
            int gapsCount = tableInfo.mainColumnsLength() - IntegerUtils.getNumberOfDigits(curCellValue);
            result.append(getTableCell(gapsCount, curCellValue));
        }

        return result.toString();
    }

    private String getTableCell(int gapsCount, int cellValue) {

        return VERTICAL_SEPARATOR +
                GAP.repeat(gapsCount) +
                cellValue;
    }
}
