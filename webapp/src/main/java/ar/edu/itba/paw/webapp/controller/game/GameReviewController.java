package ar.edu.itba.paw.webapp.controller.game;
import java.net.URI;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ar.edu.itba.paw.interfaces.service.GameService;
import ar.edu.itba.paw.interfaces.service.PlatformService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.ScoreService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.entity.Game;
import ar.edu.itba.paw.model.entity.Platform;
import ar.edu.itba.paw.model.entity.Review;
import ar.edu.itba.paw.model.entity.Score;
import ar.edu.itba.paw.model.entity.User;
import ar.edu.itba.paw.model.exception.BadFormatException;
import ar.edu.itba.paw.webapp.dto.RegisterReviewDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDto;

@Path("games")
@Component
public class GameReviewController
{
	@Context
	private UriInfo uriInfo;
	
    @Autowired
    private Validator validator;
	
	@Autowired
	private UserService us;
	
	@Autowired
	private GameService gs;
	
	@Autowired
	private PlatformService ps;
	
	@Autowired
	private ScoreService ss;
	
    @Autowired
    private ReviewService revs;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GameReviewController.class);
    
	@POST
	@Path("/{gameId}/new_review")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response addReview(@Valid RegisterReviewDto registerReviewDto, @PathParam("gameId") long gameId) throws BadFormatException
	{
		User loggedUser = us.getLoggedUser();
		if(loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		Set<ConstraintViolation<RegisterReviewDto>> violations = validator.validate(registerReviewDto);
		if (!violations.isEmpty())
			return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationErrorDto(violations)).build();
		final Optional<Game> game = gs.findById(gameId);
		if(!game.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
        if(game.get().getPlatforms().size() == 0 || !game.get().hasReleased())
        	return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
		final Optional<Platform> platform = ps.findById(registerReviewDto.getPlatform());
		if(!platform.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		if(!game.get().getPlatforms().contains(platform.get()))
			return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();

		Optional<Score> optScore = ss.findScore(loggedUser, game.get());
		if(optScore.isPresent())
			ss.changeScore(registerReviewDto.getScore(), loggedUser, game.get());
		else
			ss.register(loggedUser, game.get(), registerReviewDto.getScore());
		final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(game.get().getId())).build();
		LOGGER.debug("Publishing review by {} for game {}", loggedUser.getUsername(), game.get().getId());
		revs.register(loggedUser, game.get(), platform.get(), registerReviewDto.getScore(), registerReviewDto.getBody(), LocalDate.now());
		return Response.created(uri).build();
	}
	
	@DELETE
	@Path("/reviews/{reviewId}")
	@Consumes(value = { MediaType.APPLICATION_JSON, })
	public Response removeReview(@PathParam("reviewId") long reviewId)
	{
		Optional<Review> review = revs.findReviewById(reviewId);
		if(!review.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();
		User loggedUser = us.getLoggedUser();
		if(loggedUser == null)
			return Response.status(Response.Status.UNAUTHORIZED).build();
		if(!loggedUser.getAdminStatus() && !review.get().getUser().equals(loggedUser))
			return Response.status(Response.Status.UNAUTHORIZED).build();
		LOGGER.debug("Deleting review of ID {}", review.get().getId());
		revs.deleteReview(review.get());
		return Response.noContent().build();
	}
    
	@GET
	@Path("/reviews/{reviewId}")
	@Produces(value = { MediaType.APPLICATION_JSON })
	public Response getReviewById(@PathParam("reviewId") long reviewId)
	{
		Optional<Review> review = revs.findReviewById(reviewId);
		if(!review.isPresent())
			return Response.status(Response.Status.NOT_FOUND.getStatusCode()).build();	
		return Response.ok(ReviewDto.fromReview(review.get(), uriInfo)).build();
	}
}
