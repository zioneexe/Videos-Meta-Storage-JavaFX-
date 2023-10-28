package com.example.courseworkfx;

import javafx.beans.property.*;

public class VideoFile {

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

    public BooleanProperty hasSubtitlesProperty() { return hasSubtitles; }
    public boolean ifHasSubtitles() { return hasSubtitles.get(); }
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getFileLocation() {
        return fileLocation.get();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation.set(fileLocation);
    }

    public String getFileFormat() {
        return fileFormat.get();
    }

    public void setFileFormat(String fileFormat) {
        this.fileLocation.set(fileFormat);
    }

    public Double getFileDuration() {
        return fileDuration.get();
    }

    public void setFileDuration(Double fileDuration) {
        this.fileDuration.set(fileDuration);
    }

    public String getVideoCodec() {
        return videoCodec.get();
    }

    public void setVideoCodec(String videoCodec) {
        this.videoCodec.set(videoCodec);
    }

    public String getAudioCodec() {
        return audioCodec.get();
    }

    public void setAudioCodec(String audioCodec) {
        this.audioCodec.set(audioCodec);
    }

    public String getHasSubtitles() { return hasSubtitles.get() ? "Yes" : "No"; }

    public void setHasSubtitles(Boolean hasSubtitles) {
        this.hasSubtitles.set(hasSubtitles);
    }

    public Double getVideoSize() {
        return videoSize.get();
    }

    public void setVideoSize(Double videoSize) {
        this.videoSize.set(videoSize);
    }

    public String getPlayer() {
        return player.get();
    }

    public void setPlayer(String player) {
        this.player.set(player);
    }

    //------------------------------------------------------------------------------------------

    public VideoFile() {}

    public VideoFile
            (String name, String fileLocation, String fileFormat, Double fileDuration,
             String videoCodec, String audioCodec, Boolean hasSubtitles,
             Double videoSize, String player) {
        this.name = new SimpleStringProperty(name);
        this.fileLocation = new SimpleStringProperty(fileLocation);
        this.fileFormat = new SimpleStringProperty(fileFormat);
        this.fileDuration = new SimpleDoubleProperty(fileDuration);
        this.videoCodec = new SimpleStringProperty(videoCodec);
        this.audioCodec = new SimpleStringProperty(audioCodec);;
        this.hasSubtitles = new SimpleBooleanProperty(hasSubtitles);
        this.videoSize = new SimpleDoubleProperty(videoSize);
        this.player = new SimpleStringProperty(player);
    }

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

    @Override
    public String toString() {
        StringBuilder stringRepresentation = new StringBuilder();

        stringRepresentation.append("[ Video: \"").append(name).append("\" ]\n");
        stringRepresentation.append("\t-location: \"").append(fileLocation).append("\"\n");
        stringRepresentation.append("\t-format: \"").append(fileFormat).append("\"\n");
        stringRepresentation.append("\t-duration: \"").append(fileDuration).append("\"\n");
        stringRepresentation.append("\t-video codec: \"").append(videoCodec).append("\"\n");
        stringRepresentation.append("\t-audio codec: \"").append(audioCodec).append("\"\n");
        stringRepresentation.append("\t-subtitles are: \"").append(hasSubtitles.get() ? "" : "not ").append("present!\n");
        stringRepresentation.append("\t-size: \"").append(videoSize).append("\"\n");
        stringRepresentation.append("\t-player: \"").append(player).append("\"\n");

        return stringRepresentation.toString();

    }

}
