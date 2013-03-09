package shared.parse.ui;

import shared.parse.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {

	protected EditText editTextEmail;
	protected EditText editTextPassword;
	
	protected TextView textViewMsg;
//	protected View viewFacebook;
//	protected View viewTwitter;
	protected View viewAnonymous;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		
		textViewMsg = (TextView) findViewById(R.id.textViewMsg);
		
//		viewFacebook = findViewById(R.id.buttonFacebook);
//		viewTwitter = findViewById(R.id.buttonTwitter);
		viewAnonymous = findViewById(R.id.buttonAnonymous);
		
		ParseUser user = ParseUser.getCurrentUser();
		if (user != null) {
			if (ParseAnonymousUtils.isLinked(user)) {
				editTextEmail.setText(user.getUsername());
			} else {
				editTextEmail.setText(user.getUsername());
			}
			
			String action = getIntent().getAction();
			if (Intent.ACTION_MAIN.equals(action) && user.isAuthenticated()) {
				onAuthenticated();
			}
		}
	}

	public void use(View view) {
		ParseUser user = ParseUser.getCurrentUser();
		if (user != null) {
			editTextEmail.setText(user.getUsername());
			if (user.isAuthenticated())
				onAuthenticated();
		}
	}
	
	public void login(View view) {
		String username = editTextEmail.getText().toString();
		String password = editTextPassword.getText().toString();
//		System.out.println(username + password);
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					// Hooray! The user is logged in.
					onAuthenticated();
				} else {
					// Signup failed. Look at the ParseException to see what
					// happened.
					textViewMsg.setText(e.getLocalizedMessage());
				}
			}
		});
	}

	public void resetPassword(View view) {
		String username = editTextEmail.getText().toString();

		ParseUser.requestPasswordResetInBackground(username, new RequestPasswordResetCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					textViewMsg.setText(R.string.reset_msg);
				} else {
					textViewMsg.setText(e.getLocalizedMessage());
				}
			}
		});
	}

	public void logout(View view) {
		ParseUser.logOut();
	}

	public void signup(View view) {
		String username = editTextEmail.getText().toString();
		String password = editTextPassword.getText().toString();
//		System.out.println(username + password);
		
		if (username.length() < 1) {
			textViewMsg.setText("");
			return;
		}
		
		ParseUser u = new ParseUser();
		u.setUsername(username);
		u.setPassword(password);
		u.setEmail(username);
		
		u.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					// Hooray! The user is logged in.
					onAuthenticated();
				} else {
					// Signup failed. Look at the ParseException to see what
					// happened.
					textViewMsg.setText(e.getLocalizedMessage());
				}
			}
		});
	}

	public void facebook(View view) {
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user == null) {
					Log.d("MyApp",
							"Uh oh. The user cancelled the Facebook login.");
					Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
				} else if (user.isNew()) {
					Log.d("MyApp",
							"User signed up and logged in through Facebook!");
					onAuthenticated();
				} else {
					Log.d("MyApp", "User logged in through Facebook!");
					onAuthenticated();
				}
			}
		});
	}

	public void twitter(View view) {
		ParseTwitterUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user == null) {
					Log.d("MyApp",
							"Uh oh. The user cancelled the Twitter login.");
					Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
				} else if (user.isNew()) {
					Log.d("MyApp",
							"User signed up and logged in through Twitter!");
					onAuthenticated();
				} else {
					Log.d("MyApp", "User logged in through Twitter!");
					onAuthenticated();
				}
			}
		});
	}

	public void anonymous(View view) {
		ParseAnonymousUtils.logIn(new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					Log.d("MyApp", "Anonymous login failed.");
					textViewMsg.setText(e.getLocalizedMessage());
				} else {
					Log.d("MyApp", "Anonymous user logged in.");
					onAuthenticated();
				}
			}
		});
	}

	protected void onAuthenticated() {
		finish();
	}
}
