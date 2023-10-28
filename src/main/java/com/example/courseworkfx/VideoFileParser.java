package com.example.courseworkfx;

public class VideoFileParser {
    public static VideoFile parse(String line) {
        line = prepareLineForParsing(line);

        String[] parts = line.split(";");
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

            return new VideoFile(name, fileLocation, fileFormat, fileDuration, videoCodec, audioCodec,
                    hasSubtitles, videoSize, player);
        }
        return null;
    }

    public static Double parseTimeToMinutes(String time) {
        String[] parts = time.split(":");
        if (parts.length == 3) {
            try {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                double totalMinutes = hours * 60 + minutes + seconds / 60.0;
                return Math.round(totalMinutes * 1000) / 1000.0;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public static String prepareLineForParsing(String line) {
        line = line.trim();

        if (line.endsWith(",")) {
            line = line.substring(0, line.length() - 1);
        }

        return line;
    }
}
