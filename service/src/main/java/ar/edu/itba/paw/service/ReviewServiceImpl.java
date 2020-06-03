package ar.edu.itba.paw.service;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.model.Game;
import ar.edu.itba.paw.model.Platform;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	@Autowired
	private ReviewDao reviewDao;

	@Transactional
	@Override
	public Optional<Review> findReviewById(long review) {
		return reviewDao.findReviewById(review);
	}

	@Transactional
	@Override
	public List<Review> findGameReviews(Game game) {
		return reviewDao.findGameReviews(game);
	}

	@Transactional
	@Override
	public List<Review> findGameAndPlatformReviews(Game game, Platform platform) {
		return reviewDao.findGameAndPlatformReviews(game, platform);
	}

	@Transactional
	@Override
	public List<Review> findUserReviews(User user) {
		return reviewDao.findUserReviews(user);
	}

	@Transactional
	@Override
	public List<Review> findUserAndGameReviews(User user, Game game) {
		return reviewDao.findUserAndGameReviews(user, game);
	}

	@Transactional
	@Override
	public Optional<Review> changeReviewBody(long review, String body) {
		return reviewDao.changeReviewBody(review, body);
	}

	@Transactional
	@Override
	public List<Review> getAllReviews() {
		return reviewDao.getAllReviews();
	}

	@Transactional
	@Override
	public Review register(User user, Game game, Platform platform, int score, String body, LocalDate date) {
		return reviewDao.register(user, game, platform, score, body, date);
	}

}
