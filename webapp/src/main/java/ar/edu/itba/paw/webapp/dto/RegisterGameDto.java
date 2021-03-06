package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validators.anotation.Base64Image;

import java.util.List;

import javax.validation.constraints.Size;

public class RegisterGameDto {
	
	public RegisterGameDto() {}
	
    @Size(min = 1)
	private String              title;

    @Size(max = 15000)
    private String              description;

	@Base64Image
    private String      		cover;

    private String              trailer;
    private List<Long>          platforms;
    private List<Long>          developers;
    private List<Long>          publishers;
    private List<Long>          genres;

    private List<RegisterReleaseDto>	releaseDates;
    
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getTrailer() {
		return trailer;
	}
	
	public void setTrailer(String trailer) {
		this.trailer = trailer;
	}
	
	public List<Long> getPlatforms() {
		return platforms;
	}
	
	public void setPlatforms(List<Long> platforms) {
		this.platforms = platforms;
	}
	
	public List<Long> getDevelopers() {
		return developers;
	}
	
	public void setDevelopers(List<Long> developers) {
		this.developers = developers;
	}
	
	public List<Long> getPublishers() {
		return publishers;
	}
	
	public void setPublishers(List<Long> publishers) {
		this.publishers = publishers;
	}
	
	public List<Long> getGenres() {
		return genres;
	}
	
	public void setGenres(List<Long> genres) {
		this.genres = genres;
	}

	public List<RegisterReleaseDto> getReleaseDates() {
		return releaseDates;
	}

	public void setReleaseDates(List<RegisterReleaseDto> releaseDates) {
		this.releaseDates = releaseDates;
	}
}
