package com.example.thebigmoviebackend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Movie {
    private Double popularity;
    private Integer voteCount;
    private Boolean video;
    private String posterPath;
    private Integer ID;
    private Boolean adult;
    private String backgroundPath;
    private String originalLanguage;
    private String originalTitle;
    private List<Integer> genreIDs;
    private String title;
    private Double voteAverage;
    private String overview;
    private String releaseDate;
    private String uuid;

    public Movie() {
        this("");
    }

    public Movie(String title) {
        this(0.0, 0, false, "", 0, false, "", "", title, new ArrayList<Integer>(), title, 0.0, "", "",UUID.randomUUID().toString());
    }

    public Movie(Double popularity, Integer voteCount, Boolean video, String posterPath, Integer ID, Boolean adult, String backgroundPath, String originalLanguage, String originalTitle, List<Integer> genreIDs, String title, Double voteAverage, String overview, String releaseDate, String uuid) {
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.posterPath = posterPath;
        this.ID = ID;
        this.adult = adult;
        this.backgroundPath = backgroundPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.genreIDs = genreIDs;
        this.title = title;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.uuid = uuid;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<Integer> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(List<Integer> genreIDs) {
        this.genreIDs = genreIDs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUuid(){ return uuid;}

    public void setUuid(String uuid){
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return title + " (" + releaseDate + ")";
    }
}
