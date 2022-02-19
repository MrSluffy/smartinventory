package com.smart.inventory.application.util;

import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Owner;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.Nonnull;

public class Utilities {

    public final Employer employer = VaadinSession.getCurrent().getAttribute(Employer.class);
    public final Owner owner = VaadinSession.getCurrent().getAttribute(Owner.class);
    public final Company company = VaadinSession.getCurrent().getAttribute(Company.class);

    public Utilities(){
    }

    @Nonnull
    public static Boolean caseInsensitiveContainsFilter(String value, String filter) {
        return value.toLowerCase().contains(filter.toLowerCase());
    }

}
