import api from './api';
import AuthService from "./authService";
import PaginationService from './paginationService';

const getGameReviews = async(gameId, limit) => {
  return getGameReviewsPage(gameId, 1, limit);
}

const getUserReviews = async(userId, limit) => {
  return getUserReviewsPage(userId, 1, limit);
}

const getGameReviewsPage = async(gameId, page, limit) => {
  try {
    const endpoint = `games/${gameId}/reviews?page=${page}&page_size=${limit}`;
    const response = await api.get(endpoint);
    return PaginationService.parseResponsePaginationHeaders(response);
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getUserReviewsPage = async(userId, page, limit) => {
  if(userId == null) {
    return [];
  }
  try {
    const endpoint = `users/${userId}/reviews?page=${page}&page_size=${limit}`;
    const response = await api.get(endpoint);
    return PaginationService.parseResponsePaginationHeaders(response);
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const getUserGameReviews = async(userId, gameId) => {
  if(userId == null) {
    return [];
  }
  try {
    const endpoint = `users/${userId}/reviews/${gameId}`;
    const response = await api.get(endpoint);
    return response.data;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const addReview = async(gameId, score, platform, body) => {
  try {
    const endpoint = `games/${gameId}/new_review`;
    const newReview = {
      "platform" : platform,
      "score" : score,
      "body" : body,
    }
    const response = await api.post(endpoint, newReview, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
    return response;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const deleteReview = async(reviewId) => {
  try {
    const endpoint = `games/reviews/${reviewId}`;
    const response = await api.delete(endpoint, { headers: { 'Content-Type': 'application/json' , authorization: AuthService.getToken()}});
    return response;
  } catch(err) {
    if(err.response) {
      return { status : err.response.status };
    } else {
      /* timeout */
    }
  }
}

const ReviewService = {
  getGameReviews     : getGameReviews,
  getGameReviewsPage : getGameReviewsPage,
  getUserGameReviews : getUserGameReviews,
  getUserReviews     : getUserReviews,
  getUserReviewsPage : getUserReviewsPage,
  addReview : addReview,
  deleteReview : deleteReview
}

export default ReviewService;