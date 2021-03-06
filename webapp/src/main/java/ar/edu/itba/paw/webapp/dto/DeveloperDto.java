package ar.edu.itba.paw.webapp.dto;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.model.entity.Developer;

public class DeveloperDto {
	private long id;
	private String name;
	private String logo;
	private URI games_url;
	
	public static DeveloperDto fromDeveloper(Developer dev, UriInfo uriInfo)
	{
		final DeveloperDto dto = new DeveloperDto();
		dto.id = dev.getId();
		dto.name = dev.getName();
		dto.logo = dev.getLogo();
		dto.games_url = uriInfo.getBaseUriBuilder().path("developers").path(String.valueOf(dto.id)).path("games").build();
		return dto;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public URI getGames_url() {
		return games_url;
	}

	public void setGames_url(URI games_url) {
		this.games_url = games_url;
	}
}
