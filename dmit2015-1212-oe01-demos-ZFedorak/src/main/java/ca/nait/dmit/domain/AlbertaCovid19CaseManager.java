package ca.nait.dmit.domain;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbertaCovid19CaseManager {

    private static AlbertaCovid19CaseManager instance;

    public static AlbertaCovid19CaseManager getInstance() throws IOException {
        // https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples#thread-safe-singleton    if(instance == null){
        synchronized (AlbertaCovid19CaseManager.class) {
            if (instance == null) {
                instance = new AlbertaCovid19CaseManager();
            }
        }

        return instance;
    }

    @Getter
    private List<AlbertaCovid19Case> albertaCovid19CaseList = new ArrayList<>();

    private AlbertaCovid19CaseManager() throws IOException {
        try (var reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/data/covid-19-alberta-statistics-data.csv")))) {

            String lineText;
            // Declare a delimiter that looks for a comma inside the value
            final var DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
            // skip first line
            reader.readLine();
            var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            while ((lineText = reader.readLine()) != null) {
                // Create an object for each row in the file.
                AlbertaCovid19Case currentCase = new AlbertaCovid19Case();
                // -1 limit allows for any number of fields
                String[] values = lineText.split(DELIMITER, -1);
                // Column order of fields
                //0 - "Id"
                //1 - "Date reported"
                //2 - "Alberta Health Services Zone"
                //3 - "Gender"
                //4 - "Age group"
                //5 - "Case status"
                //6 - "Case type"
                currentCase.setId(Integer.parseInt(values[0].replaceAll("\"", "")));
                currentCase.setDateReported(LocalDate.parse(values[1].replaceAll("\"", ""), dateTimeFormatter));
                currentCase.setAhsZone(values[2].replaceAll("\"", ""));
                currentCase.setGender(values[3].replaceAll("\"", ""));
                currentCase.setAgeGroup(values[4].replaceAll("\"", ""));
                currentCase.setCaseStatus(values[5].replaceAll("\"", ""));
                currentCase.setCaseType(values[6].replaceAll("\"", ""));

                albertaCovid19CaseList.add(currentCase);
            }
        }
    }

    public List<String> findDistinctAhsZone() {
        return albertaCovid19CaseList
                .stream()
                //.map(AlbertaCovid19Case::getAhsZone)
                .map(item -> item.getAhsZone())
                .distinct()
                .filter(item -> item.isEmpty() == false)
                .sorted()
                .collect(Collectors.toList());
    }

    public long activeCaseCount() {
        return albertaCovid19CaseList
                .stream()
                .filter(item -> item.getCaseStatus().equalsIgnoreCase("Active"))
                .count();

    }

    public long activeCaseCountByAhsZone(String ahsZone) {
        return albertaCovid19CaseList
                .stream()
                .filter(item -> item.getCaseStatus().equalsIgnoreCase("Active"))
                .filter(item -> item.getAhsZone().equalsIgnoreCase(ahsZone))
                .count();
    }

    public long activeCaseCountByAhsZoneAndDateRange(String ahsZone, LocalDate fromDate, LocalDate toDate) {
        return albertaCovid19CaseList
                .stream()
                .filter(item -> item.getAhsZone().equalsIgnoreCase(ahsZone))
                .filter(item -> item.getDateReported().isBefore(fromDate) && !item.getDateReported().isAfter(toDate))
                .count();
    }
}
