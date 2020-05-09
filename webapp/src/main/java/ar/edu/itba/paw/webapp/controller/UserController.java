package ar.edu.itba.paw.webapp.controller;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.service.BacklogCookieHandlerService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.ForgotPasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;

@Controller
public class UserController
{
	@Autowired
	private UserService us;
	
	@Autowired
	private BacklogCookieHandlerService backlogCookieHandlerService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping(value = "/create", method = { RequestMethod.GET })
	public ModelAndView registerForm(@ModelAttribute("registerForm") final UserForm registerForm) 
	{
		return new ModelAndView("register");
	}
	
	@RequestMapping(value = "/create", method = { RequestMethod.POST })
	public ModelAndView register(@Valid @ModelAttribute("registerForm") final UserForm registerForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) 
	{
		if (errors.hasErrors())
			return registerForm(registerForm);
		LOGGER.debug("Creating new user {} with email {}.", registerForm.getUsername(), registerForm.getEmail());
		final User u = us.register(registerForm.getUsername(), registerForm.getPassword(), registerForm.getEmail(), LocaleContextHolder.getLocale());
		LOGGER.debug("User {} successfully created.", registerForm.getUsername());
		authWithAuthManager(request, u.getUsername(), registerForm.getPassword());
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/login")
	public ModelAndView login()
	{
		return new ModelAndView("login");
	}
	
	@RequestMapping("/login_error")
	public ModelAndView loginError()
	{
		ModelAndView mav = new ModelAndView("login");
		mav.addObject("error", true);
		return mav;
	}
	
	@RequestMapping("/users/{id}")
	public ModelAndView userProfile(@PathVariable("id") long id, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		final ModelAndView mav = new ModelAndView("userProfile");
		User visitedUser = us.findByIdWithDetails(id).orElseThrow(UserNotFoundException::new);
		User loggedUser = us.getLoggedUser();
		if(loggedUser == null)
		{
			backlogCookieHandlerService.updateWithBacklogDetails(visitedUser.getBacklog(), backlog);
		}
		mav.addObject("user", visitedUser);
		return mav;
	}
	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndReturnToUserProfile(@PathVariable("id") long id, @RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/users/{id}");
	}
	
	@RequestMapping("/profile")
	public ModelAndView visitOwnProfile()
	{
		final ModelAndView mav = new ModelAndView("userProfile");
		User u = us.getLoggedUser();
		mav.addObject("user", us.findByIdWithDetails(u.getId()).get());
		return mav;
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public ModelAndView addToBacklogAndReturnToUserProfile(@RequestParam long gameId, HttpServletResponse response, @CookieValue(value="backlog", defaultValue="") String backlog)
	{
		backlog = backlogCookieHandlerService.toggleBacklog(gameId, response, backlog);
		return new ModelAndView("redirect:/profile");
	}
	
	@RequestMapping(value = "/forgotPassword", method = { RequestMethod.GET })
	public ModelAndView forgotPassword(@ModelAttribute("forgotPasswordForm") final ForgotPasswordForm forgotPasswordForm) 
	{
		return new ModelAndView("forgotPassword");
	}
	
	@RequestMapping(value = "/forgotPassword", method = { RequestMethod.POST })
	public ModelAndView forgotPassword(@Valid @ModelAttribute("forgotPasswordForm") final ForgotPasswordForm forgotPasswordForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) 
	{
		if (errors.hasErrors())
			return forgotPassword(forgotPasswordForm);
		User u = us.findByEmail(forgotPasswordForm.getEmail()).orElseThrow(UserNotFoundException::new);
		String token = UUID.randomUUID().toString();
		LOGGER.debug("Creating Password Reset Token for {}.", u.getUsername());
		us.createPasswordResetTokenForUser(u, token);
		final ModelAndView mav = new ModelAndView("forgotPassword");
		mav.addObject("emailSent", true);
		return mav;
	}
	
	@RequestMapping("/changePassword")
	public ModelAndView showChangePasswordPage(@ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm, @RequestParam("token") String token)
	{
	    String result = us.validatePasswordResetToken(token);
	    if(result != null)
	    {
	    	LOGGER.debug("Password Reset Token is invalid. Redirecting to 404 page");
	    	return new ModelAndView("redirect:/error404");
	    }
	    else
	    {
	    	final ModelAndView mav = new ModelAndView("updatePassword");
	        mav.addObject("token", token);
	        return mav;
	    }
	}
	
	@RequestMapping(value="/changePassword", method = { RequestMethod.POST })
	public ModelAndView savePassword(@Valid @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	{
		String result = us.validatePasswordResetToken(changePasswordForm.getToken());
		if(errors.hasErrors())
			return showChangePasswordPage(changePasswordForm, changePasswordForm.getToken());
	    if(result != null)
	    {
	    	LOGGER.debug("Password Reset Token is invalid. Redirecting to 404 page");
	    	return new ModelAndView("redirect:/error404");
	    }
	    User user = us.getUserByPasswordResetToken(changePasswordForm.getToken()).orElseThrow(UserNotFoundException::new);
	    LOGGER.debug("Updating password for user {}.", user.getUsername());
        us.changeUserPassword(user, changePasswordForm.getPassword());
        LOGGER.debug("Password successfully updated for user {}.", user.getUsername());
        LOGGER.debug("Updating password for user {}.", user.getUsername());
        authWithAuthManager(request, user.getUsername(), changePasswordForm.getPassword());
        us.updateLocale(user, LocaleContextHolder.getLocale());
        return new ModelAndView("redirect:/");
	}

	public void authWithAuthManager(HttpServletRequest request, String username, String password)
	{
	    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
	    authToken.setDetails(new WebAuthenticationDetails(request));
	    Authentication authentication = authenticationManager.authenticate(authToken);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
        LOGGER.debug("User {} automatically logged in.", username);
	}
}
