public class Util {
    public static final int DAY = 24 * 60 * 60;

    public static int convertStringToMinutes(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        if (hours >= 24) {
            hours -= 24;
        }
        int minutes = Integer.parseInt(time.substring(2, 4));
        return hours * 60 + minutes;
    }

    public static int calculateWaitTime(int currentTime, int departureTime) {
        int currTime = currentTime % DAY;
        int depTime = departureTime % DAY;
        if (departureTime < currentTime) {
            departureTime += DAY;
        }
        return departureTime - currentTime;
    }

    public static int calculateTimeOfDay(int minutes) {
        return minutes % DAY;
    }
}
