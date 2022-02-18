package com.smart.inventory.application.data.services.owner;

import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Owner;
import com.smart.inventory.application.data.repository.ICompanyRepository;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IOwnerRepository;
import com.smart.inventory.application.exeptions.AuthException;
import com.smart.inventory.application.exeptions.ValueExeptionHandler;
import com.smart.inventory.application.views.MainLayout;
import com.smart.inventory.application.views.menu.account.AccountView;
import com.smart.inventory.application.views.menu.dashboard.DashboardView;
import com.smart.inventory.application.views.menu.ingredient.IngredientView;
import com.smart.inventory.application.views.menu.item.ItemView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
@Service
public class OwnerService implements IOwnerService {

    public record AuthorizedRoute(String route,
                                  String name,
                                  Class<? extends Component> view) {

    }


    private final IOwnerRepository ownerRepository;

    private final IEmployerRepository employerRepository;

    private final ICompanyRepository companyRepository;

    @Autowired
    public OwnerService(IOwnerRepository ownerRepository,
                        IEmployerRepository employerRepository,
                        ICompanyRepository companyRepository) {
        this.ownerRepository = ownerRepository;
        this.employerRepository = employerRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void authenticate(String email, String password, String companyName) throws AuthException {
        Owner owner = ownerRepository.getByEmail(email);
        Employer employer = employerRepository.getByEmail(email);
        Company company = companyRepository.getCompanyByName(companyName);
        if(company == null){
            throw new AuthException();
        }
        if(!companyName.trim().isEmpty()){
            VaadinSession.getCurrent().setAttribute(Company.class, company);
        }
        if (owner != null && owner.verifyPassword(password) && company.getOwner().getId().equals(owner.getId())) {
            VaadinSession.getCurrent().setAttribute(Owner.class, owner);
            createRoutes(owner.getRoles());
        } else if(employer != null && employer.verifyPassword(password) && employer.getEmplyrCompany().getName().equals(companyName)){
            VaadinSession.getCurrent().setAttribute(Employer.class, employer);
            createRoutes(employer.getRoles());
        } else {
            throw new AuthException();
        }

    }

    @Override
    public void register(String fname,
                         String lname,
                         String password,
                         String email,
                         String companyName) {

        Owner owner = new Owner(
                fname,
                lname,
                email,
                password,
                Role.CMP_OWNER);

        Company company = new Company();
        company.setName(companyName);
        company.getOwnerInCompany().add(owner);
        company.setOwner(owner);
        companyRepository.save(company);
        if(ownerRepository.findOwnerByEmail(email).isPresent()){
            throw new ValueExeptionHandler(email +" email is already taken");
        } else {
            ownerRepository.save(owner);
        }
    }

    @Override
    public Optional<Owner> getOwnerEmail(String value){
        return ownerRepository.findOwnerByEmail(value);
    }

    private void createRoutes(Role roles) {
        getAuthorizedRoutes(roles)
                .forEach(authorizedRoute ->
                        RouteConfiguration.forSessionScope()
                                .setRoute(authorizedRoute.route,
                                        authorizedRoute.view(),
                                        MainLayout.class));
    }

    public List<AuthorizedRoute> getAuthorizedRoutes(Role role) {

        var routes = new ArrayList<AuthorizedRoute>();

        String company = VaadinSession.getCurrent().getAttribute(Company.class).getName().trim().toLowerCase();

        if (role.equals(Role.CMP_OWNER)) {
            routes.add(new AuthorizedRoute(company + "/item", "Item Stock", ItemView.class));
            routes.add(new AuthorizedRoute(company +"/costing", "Costing", IngredientView.class));
            routes.add(new AuthorizedRoute(company +"/employer", "My Employer", AccountView.class));
        } else if (role.equals(Role.EMPLOYER)) {
            routes.add(new AuthorizedRoute(company + "/employer/item", "Item Stock", ItemView.class));
        } else if (role.equals(Role.ADMIN)) {
            routes.add(new AuthorizedRoute("admin/dashboard", "Dashboard", DashboardView.class));
            routes.add(new AuthorizedRoute("admin/item", "Item Stock", ItemView.class));
            routes.add(new AuthorizedRoute("admin/costing", "Costing", IngredientView.class));
        }
        return routes;
    }
}
