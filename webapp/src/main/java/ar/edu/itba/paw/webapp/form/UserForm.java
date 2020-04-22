package ar.edu.itba.paw.webapp.form;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import ar.edu.itba.paw.webapp.validators.FieldMatch;
import ar.edu.itba.paw.webapp.validators.UserUnique;

@FieldMatch(baseField = "password", matchField = "repeatPassword")
public class UserForm
{
	@Size(min = 6, max = 100)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	@UserUnique(username = "username")
	private String username;
	
	@Size(min = 6, max = 100)
	private String password;
	
	@Size(min = 6, max = 100)
	private String repeatPassword;
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getRepeatPassword()
	{
		return repeatPassword;
	}
	public void setRepeatPassword(String repeatPassword)
	{
		this.repeatPassword = repeatPassword;
	}
}
