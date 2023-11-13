package com.example.courseworkfx;

import javafx.beans.property.*;

/**
 * The `VideoFile` class represents a video file with various properties.
 */
public class VideoFile {

    // Properties using JavaFX properties for binding in UI
    private StringProperty name;
    private StringProperty fileLocation;
    private StringProperty fileFormat;
    private DoubleProperty fileDuration;
    private StringProperty videoCodec;
    private StringProperty audioCodec;
    private BooleanProperty hasSubtitles;
    private DoubleProperty videoSize;
    private StringProperty player;

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'hasSubtitles' property

    /**
     * Getter for the property indicating whether the video has subtitles.
     * @return The BooleanProperty indicating if the video has subtitles.
     */
    public BooleanProperty hasSubtitlesProperty() {
        return hasSubtitles;
    }

    /**
     * Getter for checking if the video has subtitles.
     * @return true if the video has subtitles, false otherwise.
     */
    public boolean ifHasSubtitles() {
        return hasSubtitles.get();
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'name' property

    /**
     * Getter for the name of the video.
     * @return The name of the video.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Setter for the name of the video.
     * @param name The new name for the video.
     */
    public void setName(String name) {
        this.name.set(name);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'fileLocation' property

    /**
     * Getter for the location of the video file.
     * @return The location of the video file.
     */
    public String getFileLocation() {
        return fileLocation.get();
    }

    /**
     * Setter for the location of the video file.
     * @param fileLocation The new location for the video file.
     */
    public void setFileLocation(String fileLocation) {
        this.fileLocation.set(fileLocation);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'fileFormat' property

    /**
     * Getter for the format of the video file.
     * @return The format of the video file.
     */
    public String getFileFormat() {
        return fileFormat.get();
    }

    /**
     * Setter for the format of the video file.
     * @param fileFormat The new format for the video file.
     */
    public void setFileFormat(String fileFormat) {
        this.fileLocation.set(fileFormat);  // Note: There seems to be a typo here, it should be setFileFormat
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'fileDuration' property

    /**
     * Getter for the duration of the video file.
     * @return The duration of the video file in minutes.
     */
    public Double getFileDuration() {
        return fileDuration.get();
    }

    /**
     * Setter for the duration of the video file.
     * @param fileDuration The new duration for the video file.
     */
    public void setFileDuration(Double fileDuration) {
        this.fileDuration.set(fileDuration);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'videoCodec' property

    /**
     * Getter for the video codec used in the video file.
     * @return The video codec used in the video file.
     */
    public String getVideoCodec() {
        return videoCodec.get();
    }

    /**
     * Setter for the video codec used in the video file.
     * @param videoCodec The new video codec for the video file.
     */
    public void setVideoCodec(String videoCodec) {
        this.videoCodec.set(videoCodec);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'audioCodec' property

    /**
     * Getter for the audio codec used in the video file.
     * @return The audio codec used in the video file.
     */
    public String getAudioCodec() {
        return audioCodec.get();
    }

    /**
     * Setter for the audio codec used in the video file.
     * @param audioCodec The new audio codec for the video file.
     */
    public void setAudioCodec(String audioCodec) {
        this.audioCodec.set(audioCodec);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'hasSubtitles' property

    /**
     * Getter for whether the video has subtitles or not.
     * @return "Yes" if the video has subtitles, "No" otherwise.
     */
    public String getHasSubtitles() {
        return hasSubtitles.get() ? "Yes" : "No";
    }

    /**
     * Setter for whether the video has subtitles or not.
     * @param hasSubtitles Boolean indicating whether the video has subtitles.
     */
    public void setHasSubtitles(Boolean hasSubtitles) {
        this.hasSubtitles.set(hasSubtitles);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'videoSize' property

    /**
     * Getter for the size of the video file.
     * @return The size of the video file in megabytes.
     */
    public Double getVideoSize() {
        return videoSize.get();
    }

    /**
     * Setter for the size of the video file.
     * @param videoSize The new size for the video file.
     */
    public void setVideoSize(Double videoSize) {
        this.videoSize.set(videoSize);
    }

    //------------------------------------------------------------------------------------------

    // Methods for accessing and modifying the 'player' property

    /**
     * Getter for the player used for video playback.
     * @return The player used for video playback.
     */
    public String getPlayer() {
        return player.get();
    }

    /**
     * Setter for the player used for video playback.
     * @param player The new player for video playback.
     */
    public void setPlayer(String player) {
        this.player.set(player);
    }

    //------------------------------------------------------------------------------------------

    // Default constructor for VideoFile class.
    public VideoFile() {}

    // Parameterized constructor for creating a VideoFile with specified properties.
    public VideoFile(String name, String fileLocation, String fileFormat, Double fileDuration,
                     String videoCodec, String audioCodec, Boolean hasSubtitles,
                     Double videoSize, String player) {
        this.name = new SimpleStringProperty(name);
        this.fileLocation = new SimpleStringProperty(fileLocation);
        this.fileFormat = new SimpleStringProperty(fileFormat);
        this.fileDuration = new SimpleDoubleProperty(fileDuration);
        this.videoCodec = new SimpleStringProperty(videoCodec);
        this.audioCodec = new SimpleStringProperty(audioCodec);
        this.hasSubtitles = new SimpleBooleanProperty(hasSubtitles);
        this.videoSize = new SimpleDoubleProperty(videoSize);
        this.player = new SimpleStringProperty(player);
    }

    // Copy constructor for creating a new VideoFile object by copying another VideoFile.
    public VideoFile(VideoFile other) {
        this.name = other.name;
        this.fileLocation = other.fileLocation;
        this.fileFormat = other.fileFormat;
        this.fileDuration = other.fileDuration;
        this.videoCodec = other.videoCodec;
        this.audioCodec = other.audioCodec;
        this.hasSubtitles = other.hasSubtitles;
        this.videoSize = other.videoSize;
        this.player = other.player;
    }

    /**
     * Converts a total number of minutes into a time format (HH:MM:SS).
     *
     * @param totalMinutes The total number of minutes to be converted.
     * @return A string representing the time in HH:MM:SS format.
     */
    public static String convertMinutesToTime(Double totalMinutes) {
        // Calculate hours, minutes, and seconds from total minutes.
        int hours = totalMinutes.intValue() / 60;  // Calculate hours by dividing total minutes by 60.
        int minutes = (int) (totalMinutes % 60);  // Calculate remaining minutes after extracting hours.
        int seconds = (int) ((totalMinutes - Math.floor(totalMinutes)) * 60);  // Calculate seconds from the remaining fraction.

        // Format the result as HH:MM:SS using String.format with two-digit placeholders.
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    /**
     * Override the toString() method to provide a string representation of the VideoFile object.
     * @return String representation of the VideoFile object.
     */
    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();

        // Build a string representation including various properties of the video.
        stringRepresentation.append(getName()).append(";");
        stringRepresentation.append(getFileLocation()).append(";");
        stringRepresentation.append(getFileFormat()).append(";");
        stringRepresentation.append(convertMinutesToTime(getFileDuration())).append(";");
        stringRepresentation.append(getVideoCodec()).append(";");
        stringRepresentation.append(getAudioCodec()).append(";");
        stringRepresentation.append(hasSubtitles.get() ? "Yes" : "No").append(";");
        stringRepresentation.append(getVideoSize()).append(";");
        stringRepresentation.append(getPlayer()).append(",\n");

        return stringRepresentation.toString();
    }
}