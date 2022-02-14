package com.smart.inventory.application.views.session;

import com.smart.inventory.application.data.services.owner.OwnerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout {


    String widthSize = "20em";


    public LoginView(OwnerService ownerService){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        var email = new TextField("Email");
        email.setWidth(widthSize);
        var password = new PasswordField("Password");
        password.setWidth(widthSize);
        var btnLogin = new Button("Login");
        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnLogin.setWidth(widthSize);
        btnLogin.addClickListener(buttonClickEvent ->{
            try {
                ownerService.authenticate(email.getValue(), password.getValue());
                UI.getCurrent().navigate("home");
            } catch (OwnerService.AuthException e) {
                Notification.show("Wrong Credentials");
            }
        });

        add(new H1("Welcome !"),
                email,
                password,
                btnLogin);
    }
}
