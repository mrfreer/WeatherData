/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherdata90;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author David
 */
public class WeatherData90 {

    String dateTime;
    double temp;
    static String longestDate = "";
    static String shortestDate = "";

    public static double findShortest(ArrayList<WeatherData90> data) {
        int countCurLongest = 0, allLongest = 0;
        for (WeatherData90 w : data) {
            if (w.temp < 90) {
                countCurLongest++;
                if (countCurLongest < allLongest && countCurLongest > 30) {
                    shortestDate = w.dateTime;
                    allLongest = countCurLongest;
                }
            } else {
                countCurLongest = 0;
            }
        }
        return allLongest;
    }

    static ArrayList<LocalDate> dates = new ArrayList<>();
    public ArrayList<Long> findLongest(ArrayList<WeatherData90> data) {
        ArrayList<Long> streak = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startTime = LocalDate.of(1984, 12, 16), endTime = LocalDate.of(1984, 12, 16);
        boolean starting = true;
        for (WeatherData90 w : data) {
            if (w.temp < 88) {
                if (starting) {
                    startTime = LocalDate.parse(w.dateTime.substring(0, 8), formatter);
                }
                starting = false;
            } else {
                endTime = LocalDate.parse(w.dateTime.substring(0, 8), formatter);
                //LocalDate tempDateTime = LocalDate.from(startTime);
                long days = startTime.until( endTime, ChronoUnit.DAYS);
                if(days > 30 && (!startTime.isEqual(LocalDate.of(1984, 12, 16)))){   
                  streak.add(days);
                  dates.add(endTime);
                }
                startTime = LocalDate.of(1984, 12, 16);
                starting = true;
            }
        }

        return streak;
    }

    
    public WeatherData90(){
        
    }
    public WeatherData90(String dateTime, double temp) {
        this.dateTime = dateTime;
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "WeatherData90{" + "dateTime=" + dateTime + ", temp=" + temp + '}';
    }

    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        ArrayList<WeatherData90> weatherData = new ArrayList<>();
        String[] words = {"8555467310751dat.txt", "135577310754dat.txt"};
        for (int i = 0; i < 2; i++) {
            FileReader fileReader = new FileReader(words[i]);
            Scanner scanner = new Scanner(fileReader);
            scanner.nextLine();
            int nums = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split("\\s+");
                boolean toAdd = true;
                try {
                    Double.parseDouble(data[21]);
                } catch (Exception e) {
                    toAdd = false;
                }
                if (toAdd) {
                    weatherData.add(new WeatherData90(data[2], Double.parseDouble(data[21])));
                }
            }

        }
        ArrayList<Long> st = new WeatherData90().findLongest(weatherData);
        double averageStreak = 0.0;
        for(int i = 0; i < st.size(); i++){
            System.out.println("Streak " + st.get(i) + " Dates: " + dates.get(i) );
            averageStreak += st.get(i);
        }
        System.out.println("Average streak" + averageStreak / st.size());
        System.out.println("When?" + longestDate);

    }

}
