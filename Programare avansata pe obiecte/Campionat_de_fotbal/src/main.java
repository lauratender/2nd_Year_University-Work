import java.util.*;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        int n = scanner.nextInt();

        String line;
        line = scanner.nextLine();

        HashMap<String, Integer> teams_score = new HashMap<>();
        HashMap<String, Integer> teams_goals1 = new HashMap<>(); //goluri date
        HashMap<String, Integer> teams_goals2 = new HashMap<>(); // goluri primite

        for (int i=0; i<n; i++) {
            line = scanner.nextLine();

            int firstSpaceIndex = line.indexOf(' ');
            String team1Name = line.substring(0, firstSpaceIndex);
            //System.out.println(team1Name);

            int secondSpaceIndex = line.indexOf(' ', firstSpaceIndex+1);
            int team1Scor = Integer.parseInt(line.substring(firstSpaceIndex+1, secondSpaceIndex));

            int thirdSpaceIndex = line.indexOf(' ', secondSpaceIndex+1);
            int forthSpaceIndex = line.indexOf(' ', thirdSpaceIndex+1);
            int team2Scor = Integer.parseInt(line.substring(thirdSpaceIndex+1, forthSpaceIndex));

            String team2Name = line.substring(forthSpaceIndex+1);

            // updatatez golurile marcate si incasate de echipa 1
            if(teams_score.containsKey(team1Name)) {
                teams_goals1.put(team1Name, teams_goals1.get(team1Name) + team1Scor);
                teams_goals2.put(team1Name, teams_goals2.get(team1Name) + team2Scor);
            }
            else{
                teams_goals1.put(team1Name, team1Scor);
                teams_goals2.put(team1Name, team2Scor);
            }

            // updatatez golurile marcate si incasate de echipa 2
            if(teams_score.containsKey(team2Name)) {
                teams_goals1.put(team2Name, teams_goals1.get(team2Name) + team2Scor);
                teams_goals2.put(team2Name, teams_goals2.get(team2Name) + team1Scor);
            }
            else{
                teams_goals1.put(team2Name, team2Scor);
                teams_goals2.put(team2Name, team1Scor);
            }

            if (team1Scor == team2Scor){ // remiza
                if(teams_score.containsKey(team1Name))
                    teams_score.put(team1Name, teams_score.get(team1Name) + 1);
                else
                    teams_score.put(team1Name, 1);

                if(teams_score.containsKey(team2Name))
                    teams_score.put(team2Name, teams_score.get(team2Name) + 1);
                else
                    teams_score.put(team2Name, 1);
            }

            if(team1Scor < team2Scor) { // castiga echipa 2
                if(!teams_score.containsKey(team1Name))
                    teams_score.put(team1Name, 0);
                if(teams_score.containsKey(team2Name))
                    teams_score.put(team2Name, teams_score.get(team2Name) + 3);
                else
                    teams_score.put(team2Name, 3);
            }

            if(team1Scor > team2Scor) { // castiga echipa 1
                if(!teams_score.containsKey(team2Name))
                    teams_score.put(team2Name, 0);
                if(teams_score.containsKey(team1Name))
                    teams_score.put(team1Name, teams_score.get(team1Name) + 3);
                else
                    teams_score.put(team1Name, 3);
            }
            //System.out.println(team2Name);
        }
        LinkedHashMap<String, Integer> sorted_team_scores = sortHashMapByValues(teams_score);
        Iterator iterator = sorted_team_scores.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)iterator.next();
            int scor = ((int)mapElement.getValue());
            int goluri1 = teams_goals1.get(mapElement.getKey());
            int goluri2 = teams_goals2.get(mapElement.getKey());
            System.out.println(mapElement.getKey() + " " + scor + " " + goluri1 + " " + goluri2);
        }
    }

    public static LinkedHashMap<String, Integer> sortHashMapByValues(HashMap<String, Integer> passedMap) {
        List<String> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Integer> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues, Collections.reverseOrder());
        //Collections.sort(mapKeys);

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();

        Iterator<Integer> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            int val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                int comp1 = passedMap.get(key);
                int comp2 = val;

                if (comp1 == comp2) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }
}
