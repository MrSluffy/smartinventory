package com.smart.inventory.application.views.session;

import com.smart.inventory.application.data.services.owner.OwnerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("Register")
public class RegisterView extends Composite {

    private final OwnerService ownerService;

    H2 head = new H2("Register");
    TextField fName = new TextField("First Name");
    TextField lName = new TextField("Last Name");
    EmailField email = new EmailField("Email");
    PasswordField password1 = new PasswordField("Password");
    PasswordField password2 = new PasswordField("Confirm Password");
    TextField company = new TextField("Company Name");
    Button btnRegister = new Button("Register");

    @Autowired
    public RegisterView(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    protected Component initContent() {

        HorizontalLayout line1 = new HorizontalLayout(fName, lName);
        HorizontalLayout line2 = new HorizontalLayout(password1, password2);
        HorizontalLayout line3 = new HorizontalLayout(email, company);

        btnRegister.addClickListener(event -> {
            register(fName.getValue(), lName.getValue(), email.getValue(), password1.getValue(), password2.getValue(), company.getValue());
        });

        return new VerticalLayout(head, line1, line2, line3, btnRegister);
    }

    private void register(String fname,
                          String lname,
                          String email,
                          String password1,
                          String password2,
                          String company) {

        if(fname.trim().isEmpty()){
            notif(fname);
        } else if(lname.trim().isEmpty()){
            notif(lname);
        } else if(email.trim().isEmpty()){
            notif(email);
        } else if(!password1.equals(password2)){
            Notification.show("Password don't match");
        } else if(password1.isEmpty()){
            notif(password1);
        } else if(company.trim().isEmpty()){
            notif(company);
        } else {
            ownerService.register(fname, lname, password1, email, company);
        }

    }

    private void notif(String label){
        Notification.show(label+" cannot be empty");
    }
}
