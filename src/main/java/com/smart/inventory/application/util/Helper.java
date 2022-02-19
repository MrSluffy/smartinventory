package com.smart.inventory.application.util;

import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Owner;
import com.vaadin.flow.server.VaadinSession;

public class Helper {

    public static final Employer employer = VaadinSession.getCurrent().getAttribute(Employer.class);
    public static final Owner owner = VaadinSession.getCurrent().getAttribute(Owner.class);
    public static final Company company = VaadinSession.getCurrent().getAttribute(Company.class);
}
