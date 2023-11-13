package com.example.courseworkfx;

/**
 * The `VideoFileParser` class provides static methods to parse and prepare data related to video files.
 */
public class VideoFileParser {

    /**
     * Parses a string into a VideoFile object.
     *
     * @param line The input string containing video file information.
     * @return A VideoFile object if parsing is successful, otherwise returns null.
     */
    public static VideoFile parse(String line) {
        // Prepare the line for parsing by removing unnecessary characters.
        line = prepareLineForParsing(line);

        // Split the line into parts using ';' as a delimiter.
        String[] parts = line.split(";");

        // Check if there are exactly 9 parts, representing the expected number of properties for a VideoFile.
        if (parts.length == 9) {
            String name = parts[0];
            String fileLocation = parts[1];
            String fileFormat = parts[2];
            Double fileDuration = parseTimeToMinutes(parts[3]);
            String videoCodec = parts[4];
            String audioCodec = parts[5];
            Boolean hasSubtitles = parts[6].equals("Yes");
            Double videoSize = Double.parseDouble(parts[7]);
            String player = parts[8];

            // Create and return a new VideoFile object with the parsed properties.
            return new VideoFile(name, fileLocation, fileFormat, fileDuration, videoCodec, audioCodec,
                    hasSubtitles, videoSize, player);
        }
        // Return null if the parsing is unsuccessful.
        return null;
    }

    /**
     * Parses a time string in the format "hh:mm:ss" to minutes.
     *
     * @param time The time string to be parsed.
     * @return The time in minutes if parsing is successful, otherwise returns null.
     */
    public static Double parseTimeToMinutes(String time) {
        // Split the time string into hours, minutes, and seconds.
        String[] parts = time.split(":");

        // Check if there are exactly 3 parts, representing the expected time format.
        if (parts.length == 3) {
            try {
                // Convert hours, minutes, and seconds to total minutes.
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                double totalMinutes = hours * 60 + minutes + seconds / 60.0;

                // Round to three decimal places and return the result.
                return Math.round(totalMinutes * 1000) / 1000.0;
            } catch (NumberFormatException e) {
                // Return null if any parsing error occurs.
                return null;
            }
        }
        // Return null if the time format is not as expected.
        return null;
    }

    /**
     * Prepares a line for parsing by trimming and removing trailing commas.
     *
     * @param line The input line to be prepared.
     * @return The prepared line.
     */
    public static String prepareLineForParsing(String line) {
        // Trim leading and trailing whitespaces.
        line = line.trim();

        // Remove trailing commas if present.
        if (line.endsWith(",")) {
            line = line.substring(0, line.length() - 1);
        }

        return line;
    }
}
