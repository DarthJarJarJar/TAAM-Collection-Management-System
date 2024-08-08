package com.example.b07demosummer2024;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TestLoginPresenter {

  @Mock
  LoginFragmentView view;

  @Mock
  LoginFragmentModel model;

  LoginFragmentPresenter presenter;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    presenter = new LoginFragmentPresenter(view, model);

  }


  @Test
  public void testHandleLoginButtonClick_SuccessfulCase() {
    when(view.getUsername()).thenReturn("username");
    when(view.getPassword()).thenReturn("password");

    doAnswer(invocation -> {
      LoginListener listener = invocation.getArgument(2, LoginListener.class);
      listener.onLoginSuccess();
      return null;
    }).when(model).queryCredentials(eq("username"), eq("password"), any(LoginListener.class));

    presenter.handleLoginButtonClick();
    verify(view).showLoginSuccess();

  }


  @Test
  public void testHandleLoginButtonClick_FailureCase() {
    when(view.getUsername()).thenReturn("username");
    when(view.getPassword()).thenReturn("password");

    doAnswer(invocation -> {
      LoginListener listener = invocation.getArgument(2, LoginListener.class);
      listener.onLoginFailure("Invalid Credentials");
      return null;
    }).when(model).queryCredentials(eq("username"), eq("password"), any(LoginListener.class));

    presenter.handleLoginButtonClick();
    verify(view).showLoginFailure("Invalid Credentials");
  }


  @Test
  public void testHandleLoginButtonClick_NoUsernameProvided() {
    when(view.getUsername()).thenReturn("");
    when(view.getPassword()).thenReturn("password");

    presenter.handleLoginButtonClick();
    verify(view).showLoginFailure("Missing fields");
  }


  @Test
  public void testHandleLoginButtonClick_NoPasswordProvided() {
    when(view.getUsername()).thenReturn("username");
    when(view.getPassword()).thenReturn("");

    presenter.handleLoginButtonClick();
    verify(view).showLoginFailure("Missing fields");
  }


  @Test
  public void testHandleLoginButtonClick_EmptyFields() {
    when(view.getUsername()).thenReturn("");
    when(view.getPassword()).thenReturn("");

    presenter.handleLoginButtonClick();
    verify(view).showLoginFailure("Missing fields");
  }


}
